package com.luminn.firebase.security;


import com.luminn.firebase.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;

public class JWTUtilTool {
    public static final long VALIDITY_FOR_30DAYS = 30 * 24 * 60 * 60;
    public static final long VALID_FOR_24HRS     =      24 * 60 * 60;
    public static final long VALID_FOR_1HR     =             60 * 60;

    public static final String AUTHORITIES_KEY = "auth";

    JWTUtilTool(){

    }

    public static String generateJwt(User user, ZonedDateTime validity, String secretKey) {

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim(AUTHORITIES_KEY, user.getRole())
                .claim("email", user.getEmail())
                .claim("ownerId", user.getId())
                //.claim("state", user.status.name())
                //.claim("ownerId", user.ownedId)
                //.claim("expectedPin", user.pin)
                //.claim("pinFails", user.pinFailedCount)
                //.signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(Date.from(validity.toInstant()))
                .compact();
    }

    public static Authentication getAuthentication(String token, String secretKey) {
        User user = getUserFromJwtToken(token,secretKey);
        return new PreAuthenticatedAuthenticationToken(user, null, Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
    }

    public static User getUserFromJwtToken(String token, String secretKey) {

        final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        final String userId = claims.getSubject();
        final String userRole = (String) claims.get(AUTHORITIES_KEY);
        final String email = (String) claims.get("email");
        final String ownerId = (String) claims.get("ownerId");

        User user = new User();
        user.setId((userId));
        user.setRole(userRole);
        user.setEmail(email);


        /*return User.builder()
                .id(userId)
                .role(AuthRole.valueOf(userRole))
                .status(status)
                .email(email)
                .ownedId(ownerId)
                .pin(pin)
                .pinFailedCount(numPinFails)
                .build();*/
        return user;

    }

    public static String createToken(User user, String secretKey) {

        final ZonedDateTime validity = ZonedDateTime.now(ZoneId.of("UTC")).plusSeconds(VALIDITY_FOR_30DAYS);

        return generateJwt(user, validity, secretKey);
    }

}

package com.luminn.firebase.security;


import com.luminn.view.StatusResponse;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    //equals "SECRET" - just a regular string that is displayable.
    @Value("${ABCDE}")
    protected String ABCDE;

    public static final long VALID_FOR_1HR     =             60 * 60;

    //#12 hours * 60 mins * 60 secs=43200 secs - without use
    //T_VALIDITY=43200
    protected long VALIDITY = 43200;

    private JwtUtils() {
    }

    //returns jwt token string if success.
    // Otherwise, thr ows UnsupportedEncodingException
    public String createJwt(String id, String role, String name) throws UnsupportedEncodingException {
        String compactJwt = null;
        try {

            compactJwt = Jwts.builder()
                    .setSubject(name)
                    .setId((id))
                    .claim("name", name)
                    .claim("role", role)
                    .signWith(SignatureAlgorithm.HS512,
                            ABCDE.getBytes("UTF-8"))
                    .setExpiration(getValidityDate())
                    .compact();
        } catch (UnsupportedEncodingException ex) {
            throw ex;
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return compactJwt;
    }

    private Date getValidityDate() {
        final ZonedDateTime validity = ZonedDateTime.now(ZoneId.of("UTC")).plusSeconds(VALIDITY);
        Date validityDate = Date.from(validity.toInstant());
        return validityDate;
    }

    public String getProlongedJwtValidityDate(String jwt) throws UnsupportedEncodingException {
        Map payLoad = null;
        String modifiedJwtWithNewValidityDate = null;
        //the Jwts.parser will throw the ExpiredJwtException, if the jwt token has expired.
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(ABCDE.getBytes("UTF-8"))
                .parseClaimsJws(jwt);

        Date expDate0 = getValidityDate();
        modifiedJwtWithNewValidityDate = Jwts.builder()
                .setClaims(claims.getBody())
                .setExpiration(expDate0)
                .signWith(SignatureAlgorithm.HS512,
                        ABCDE.getBytes("UTF-8"))
                .compact();
//        System.out.println("AGE: expiration date returned: " + expDate0.toString());


        String subject = claims.getBody().getSubject();
        String id = claims.getBody().getId();
        Date expirationDate = claims.getBody().getExpiration();
        String name = (String) claims.getBody().get("name");
        String role = (String) claims.getBody().get("role");

        payLoad = new HashMap<String, String>();
        payLoad.put("subject", subject);
        payLoad.put("id", id);
        payLoad.put("expirationDate", expirationDate);
        payLoad.put("name", name);
        payLoad.put("role", role);

        return modifiedJwtWithNewValidityDate;
    }
    public Authentication getAuthentication(String token) throws UnsupportedEncodingException {
        //String token = tokenWithBearer.replace("Bearer ", "");
        final Claims claims = Jwts.parser()
                .setSigningKey(ABCDE.getBytes("UTF-8")).parseClaimsJws(token).getBody();
        final String name = claims.getSubject();
        final String userId = (claims.getId());
        final String userRole = claims.get("role").toString();


        final List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(userRole));
        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(userId != null ? userId.toString() : null));

        return new PreAuthenticatedAuthenticationToken(userId,
                null,
                simpleGrantedAuthorityList);

        //           Collections.singletonList(new SimpleGrantedAuthority(userRole)));
    }

    public String getRole(String token) throws UnsupportedEncodingException {
        final Claims claims = Jwts.parser()
                .setSigningKey(ABCDE.getBytes("UTF-8")).parseClaimsJws(token).getBody();
        return claims.get("role").toString();
    }

    public String getId(String token) throws UnsupportedEncodingException {
        final Claims claims = Jwts.parser()
                .setSigningKey(ABCDE.getBytes("UTF-8")).parseClaimsJws(token).getBody();
        return claims.getId().toString();
    }

    public void getJwt(String id, String role, String name, StatusResponse statusResponse) {
        String compactJwt = "";
        try {
            compactJwt = createJwt(
                    id,
                    role,
                    name);
            statusResponse.setStatus(true);
            statusResponse.setJwt(compactJwt);
        } catch (UnsupportedEncodingException | JwtException ex) {
            //TODO should i catch JwtException which is a RuntimeException. Seems OK since i put
            //TODO    error in the statusResponse.
            String msg = "JwtUtils.getJwt() :" + ex.getMessage();
            log.error(msg);
            statusResponse.setMessage(msg);
            statusResponse.setStatus(false);
        }
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ABCDE)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(ABCDE).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

}

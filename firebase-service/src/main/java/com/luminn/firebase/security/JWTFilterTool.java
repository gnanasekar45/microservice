package com.luminn.firebase.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JWTFilterTool extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(JWTFilterTool.class);

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    private final String secretKey;

    public JWTFilterTool(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            final Optional<String> jwtToken = extractToken(httpServletRequest);
            jwtToken.ifPresent(s -> {
                final Authentication authentication = JWTUtilTool.getAuthentication(s, secretKey);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ExpiredJwtException e) {
            log.debug("Security exception for user {} - {}. Expired token.", null, e.getMessage());
            //CK
            //log.debug("Security exception for user {} - {}. Expired token.", e.getClaims().getSubject(), e.getMessage());
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token expired!");
        } catch (JwtException e) {
            log.debug("Authentication token is invalid. {}", e.getMessage());
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token is invalid!");
        }
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            final String jwtToken = bearerToken.substring(BEARER.length(), bearerToken.length());
            return Optional.of(jwtToken);
        }
        return Optional.empty();
    }
}

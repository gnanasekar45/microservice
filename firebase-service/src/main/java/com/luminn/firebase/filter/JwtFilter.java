package com.luminn.firebase.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.luminn.firebase.security.JwtUtils;
import com.luminn.firebase.util.Path;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JwtFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private JwtUtils jwtUtils;

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    private List<String> excludedURLs;
    private List<String> adminURLs;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String[] excluded = filterConfig.getInitParameter("excludedURLs").split(";");
        String[] admin = filterConfig.getInitParameter("adminURLs").split(";");
        excludedURLs = new ArrayList<String>();
        for (int i = 0; i < excluded.length; i++) {
            excludedURLs.add(excluded[i]);
        }
        adminURLs = new ArrayList<String>();
        for (int i = 0; i < admin.length; i++) {
            adminURLs.add(admin[i]);
        }
        ApplicationContext app =  WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        jwtUtils= app.getBean(JwtUtils.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String requestURL = httpServletRequest.getRequestURL().toString();
            Optional<String> jwtToken = extractToken(httpServletRequest);

            //look if requestURL is an excluded url
            boolean isExcludedURL = false;
            //Is url in excludedURLs list ? ie. Is the url excluded from having an Authorization header ie. a jwtToken.
            for (int i = 0; i < excludedURLs.size(); i++) {
                if (requestURL.indexOf(excludedURLs.get(i)) > -1) {
                    isExcludedURL = true;
                    //call the next filter in the chain. Don't do any checks for the jwt.
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }

            //look if requestURL is an admin url, then check if the user has an admin role.
            boolean isAdminURL = false;
            for (int i = 0; i < adminURLs.size(); i++) {
                if (requestURL.indexOf(adminURLs.get(i)) > -1) {
                    isAdminURL = true;
                    boolean isAdminURLWithAdminRole = false;
                    String role = null;
                    String id = null;

                    //if jwtToken is null for the swagger url then fill jwtToken if there is a paramToken.
                    if (!jwtToken.isPresent() && adminURLs.get(i).equals("/swagger-ui.html")) {
                        String paramToken = httpServletRequest.getParameter("l3");
                        if (paramToken== null || paramToken.isEmpty()) {
                            //TODO - if the user is logged in we can get the user id. Ask Chandra how.
                            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token is invalid!");
                            log.warn("A possible rogue user is trying to use the adminUrl={} ", requestURL);
                            return;
                        } else {
                            jwtToken = Optional.of(paramToken);
                        }
                    }

                    if (jwtToken.isPresent()) {
                        role = jwtUtils.getRole(jwtToken.get());
                        id = jwtUtils.getId(jwtToken.get());
                        if (role.equals(Path.ROLE.ADMIN)) {
                            //if the url is an admin url and the jwtToken of the user has a role of Admin then true.
                            isAdminURLWithAdminRole = true;
                        }
                    }
                    //is an admin url being accessed without the user having the ADMIN role or without a jwtToken.
                    if (isAdminURL && !isAdminURLWithAdminRole) {
                        ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "User does not have access!");
                        log.warn("UserId={} trying to use the adminUrl={} with role={} ", id, requestURL, role);
                        return;
                    }
                    log.info("UserId={} invoking the adminUrl={} with role={} ", id, requestURL, role);
                    break;
                }
            }

            //at this point: isExcludedURL = false &&
            // (isAdminURL = false  || (isAdminURL=true && isAdminURLWithAdminRole=true))
            // ie. requestURL is not an excluded url and
            // (it is not an admin url or (it is and admin url with an admin role)
            if (jwtToken.isPresent()) {
                final Authentication authentication = jwtUtils.getAuthentication(jwtToken.get());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                HttpServletResponse response = ((HttpServletResponse) servletResponse);
                response.setHeader("Age", jwtUtils.getProlongedJwtValidityDate(jwtToken.get()));
            } else {
                log.warn("Authentication token is empty for invocation of the url={}", requestURL);
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token is empty!");
                return;
            }

            filterChain.doFilter(servletRequest, servletResponse);

        } catch (ExpiredJwtException e) {
            //log.info("Security exception for user {} - {}. Expired token.", e.getClaims().getSubject(), e.getMessage());
            log.info("Security exception for user {} - {}. Expired token.",
                    e.getMessage());
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token expired!");
        } catch (JwtException e) {
            log.error("Authentication token is invalid. {}", e.getMessage());
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token is invalid!");
        } catch (UnsupportedEncodingException e) {
            log.error("Jwt Signing Key with unsupported encoding exception. {}", e.getMessage());
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Jwt Signing Key with unsupported encoding exception!");
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

    @Override
    public void destroy() {
    }
}

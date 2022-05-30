package com.luminn.firebase.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    //@Autowired
    //private CustomUserDetailsService customUserDetailsService;

    @Autowired
    JwtUtils jwtProvider;


    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

   /* @Autowired
    AuthenticationManager authenticationManager;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //String jwt = getJwtFromRequest(request);

            Optional<String> jwtToken = extractToken(request);
            String requestURL = request.getRequestURL().toString();

            /*if (header == null || !header.startsWith(TOKEN_PREFIX)) {
                chain.doFilter(request, response);
                return;
            }*/

           // if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            if(jwtToken.isPresent()){
                //Long userId = jwtProvider.getUserIdFromJWT(jwt);

                /*
                    Note that you could also encode the user's username and roles inside JWT claims
                    and create the UserDetails object by parsing those claims from the JWT.
                    That would avoid the following database hit. It's completely up to you.
                 */
               /* UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);*/


                final Authentication authentications = jwtProvider.getAuthentication(jwtToken.get());
                SecurityContextHolder.getContext().setAuthentication(authentications);

                //jwtToken.ifPresent(s -> {
                //final Authentication authentication = JWTUtilTool.getAuthentication(s, secretKey);
                //SecurityContextHolder.getContext().setAuthentication(authentication);
                 //   });

                //at this point: isExcludedURL = false &&
                // (isAdminURL = false  || (isAdminURL=true && isAdminURLWithAdminRole=true))
                // ie. requestURL is not an excluded url and
                // (it is not an admin url or (it is and admin url with an admin role)
                if (jwtToken.isPresent()) {
                    final Authentication authentication = jwtProvider.getAuthentication(jwtToken.get());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    //response = ((HttpServletResponse) response);
                    //response.setHeader("Age", jwtProvider.getProlongedJwtValidityDate(jwtToken.get()));
                } else {
                    log.warn("Authentication token is empty for invocation of the url={}", requestURL);
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token is empty!");
                    return;
                }
            }
        }
        catch (ExpiredJwtException e) {
            //CK
            //log.info("Security exception for user {} - {}. Expired token.", e.getClaims().getSubject(), e.getMessage());
            log.info("Security exception for user {} - {}. Expired token.", null, e.getMessage());
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token expired!");
        } catch (JwtException e) {
            log.error("Authentication token is invalid. {}", e.getMessage());
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token is invalid!");
        } catch (UnsupportedEncodingException e) {
            log.error("Jwt Signing Key with unsupported encoding exception. {}", e.getMessage());
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Jwt Signing Key with unsupported encoding exception!");
        }catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
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

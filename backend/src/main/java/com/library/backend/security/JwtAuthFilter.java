package com.library.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userdetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().contains("/api/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){ // bearer: the token will be sent in the header and the header's (getHeader(AUTHORIZATION)) format starts with "Bearer "
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7); // "Bearer ".length == 7, we start after "Bearer "
        userEmail = jwtService.extractUsername(jwt);
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){ //securityContextHolder object that contains principal authorities ... (explanation int the first section)
            UserDetails userDetails = userdetailsService.loadUserByUsername(userEmail); // we implemented this method in the userDetailsServiceImpl class (findByEmail)
            if(jwtService.isTokenValid(jwt, userDetails)){ // validate the user token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //UserPasswordAuthenticationToken used by spring security to check and update the security context holder

                authToken.setDetails( //we need to build/extract some details from the request (the request in the parameters of this big function)
                        new WebAuthenticationDetailsSource().buildDetails(request) // building the details from the request
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);//once everything/the checks are done we update the SecurityContextHolder (aka (manually) authenticating the user)
            }
        }
        filterChain.doFilter(request, response); //call the rest of the filter chain
    }
}

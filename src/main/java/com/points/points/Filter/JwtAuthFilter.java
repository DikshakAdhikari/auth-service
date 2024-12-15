package com.points.points.Filter;


//Before access of any controller method request should come to filter first i.e. Filter is the guy which will intercept your request before reaching controller.
// Inside filter we write logic to extract the token from the header and then get the username from the token and validate the user from db and do all type of validation.
// validation involves all type of validation like token validation means token is correct or not or token isn't expired checks

import com.points.points.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter { //Use this JwtAuthFilter before using username and password filter or any of your filter, we'll define this thing in SecurityConfig i.e. we don't want form login instead we'll use jwt token based auth flow.

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //from request we'll get the authorization header for token and then will extract info like username etc.
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7); //Means just remove the 1st 7 characters inorder to remove Bearer
            System.out.println(token);
            username = jwtService.extractUsername(token);
            System.out.println("filterr "+ username);
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); //userDetails contains information like username, password, isEnable etc etc.
            System.out.println("userdetails "+ userDetails);
            if(jwtService.validateToken(token, userDetails)) {// Checking token is validated and also not expired
                //If it is valid create authentication object and set to a SecurityContextHolder
                System.out.println("ddddddddddddddddddddddddd");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

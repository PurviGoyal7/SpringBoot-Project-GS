package com.example.growsimpleeintern.Security;

import com.example.growsimpleeintern.Model.User;
import com.example.growsimpleeintern.Services.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService userService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/user/check") || path.startsWith("/user/login") || path.startsWith("/user/registration")
                || path.startsWith("/addMovie") || path.startsWith("/movieAvgRating/{id}");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Request Token = Bearer + actual JwtToken
        final String requestToken = request.getHeader("Authorization");

        String Id = null;
        String jwtToken = null;

        if (requestToken != null && requestToken.startsWith("Bearer ")) {
            jwtToken = requestToken.substring(7);
            try {
                Id = this.jwtTokenHelper.getUserIdFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            } catch (Exception e) {
                System.out.println("Invalid Token");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }

        //Validating token
        if (Id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = userService.loadUserById(Id);
            if (jwtTokenHelper.validateToken(jwtToken, userDetails)) {  //Everything's working fine
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken( userDetails, null, null);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.out.println("Invalid JWT Token");
            }
        } else {
            System.out.println("UserName is NULL or Context is Not NULL");
        }

        //forwarding the request
        filterChain.doFilter(request, response);
    }
}

package com.example.demo.common.security.filter;

import com.example.demo.common.security.JwtService;
import com.example.demo.common.security.UsuarioLogado;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        // Evita loop de redirecionamento.
        if (path.equals("trocar-senha")) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContext context = SecurityContextHolder.getContext();

        String authorization = request.getHeader("Authorization");

        if (authorization == null || context.getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorization.startsWith("Bearer ") || authorization.length() <= 7) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authorization.substring(7);

        String username = jwtService.extractUsername(jwtToken);
        if (username != null
                && jwtService.isTokenValid(jwtToken)) {

            UsuarioLogado user = (UsuarioLogado) userDetailsService.loadUserByUsername(username);

//            if (user.isPrimeiroAcesso()) {
//                response.sendRedirect("/trocar-senha");
//                return;
//            }

            var authToken =
                    new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));

            context.setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);

    }


}

package com.financeos.auth.security;

import com.financeos.auth.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Extrai o header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Se não tem token — segue sem autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrai o token removendo o prefixo "Bearer "
        String token = authHeader.substring(7);

        try {
            // 4. Extrai o email do token
            String email = jwtService.extrairEmail(token);

            // 5. Se tem email e ainda não está autenticado nessa requisição
            if (email != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                // 6. Busca o usuário no banco
                UserDetails usuario = usuarioRepository
                        .findByEmail(email)
                        .orElseThrow();

                // 7. Valida o token
                if (jwtService.isTokenValido(token, usuario)) {

                    // 8. Cria o objeto de autenticação
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    usuario,
                                    null,
                                    usuario.getAuthorities()
                            );

                    // 9. Adiciona detalhes da requisição
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // 10. Registra no SecurityContext
                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.warn("Erro ao processar token JWT: {}", e.getMessage());
        }

        // 11. Passa para o próximo filter ou Controller
        filterChain.doFilter(request, response);
    }
}
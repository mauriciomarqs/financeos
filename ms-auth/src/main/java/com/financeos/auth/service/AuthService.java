package com.financeos.auth.service;

import com.financeos.auth.dto.LoginDTO;
import com.financeos.auth.dto.RegisterDTO;
import com.financeos.auth.dto.TokenDTO;
import com.financeos.auth.model.Role;
import com.financeos.auth.model.Usuario;
import com.financeos.auth.repository.UsuarioRepository;
import com.financeos.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final int MAX_TENTATIVAS = 5;
    private static final int MINUTOS_BLOQUEIO = 30;

    // ── Cadastro ──────────────────────────────────────────

    public TokenDTO register(RegisterDTO dto) {
        // verifica se email já existe
        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email ja cadastrado");
        }

        // monta o usuário com senha hasheada
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(Role.ROLE_USER)
                .build();

        repository.save(usuario);
        log.info("Usuario cadastrado: {}", dto.getEmail());

        // já loga o usuário após o cadastro
        String token = jwtService.gerarToken(usuario);
        return buildTokenDTO(token, usuario);
    }

    // ── Login ─────────────────────────────────────────────

    public TokenDTO login(LoginDTO dto) {
        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha invalidos"));

        // verifica se está bloqueado
        if (!usuario.isAccountNonLocked()) {
            throw new RuntimeException(
                    "Conta bloqueada. Tente novamente em " +
                            MINUTOS_BLOQUEIO + " minutos."
            );
        }

        // verifica a senha
        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            registrarTentativaFalha(usuario);
            throw new RuntimeException("Email ou senha invalidos");
        }

        // login bem sucedido — zera tentativas
        usuario.setTentativasLogin(0);
        usuario.setBloqueadoAte(null);
        repository.save(usuario);

        log.info("Login realizado: {}", dto.getEmail());

        String token = jwtService.gerarToken(usuario);
        return buildTokenDTO(token, usuario);
    }

    // ── Contrato do Spring Security ───────────────────────

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario nao encontrado: " + email
                ));
    }

    // ── Métodos privados ──────────────────────────────────

    private void registrarTentativaFalha(Usuario usuario) {
        int tentativas = usuario.getTentativasLogin() + 1;
        usuario.setTentativasLogin(tentativas);

        if (tentativas >= MAX_TENTATIVAS) {
            usuario.setBloqueadoAte(
                    LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEIO)
            );
            log.warn("Conta bloqueada por {} tentativas: {}",
                    tentativas, usuario.getEmail());
        }

        repository.save(usuario);
    }

    private TokenDTO buildTokenDTO(String token, Usuario usuario) {
        return new TokenDTO(
                token,
                "Bearer",
                usuario.getEmail(),
                usuario.getRole().name(),
                jwtService.extrairExpiracaoMs(token)
        );
    }
}
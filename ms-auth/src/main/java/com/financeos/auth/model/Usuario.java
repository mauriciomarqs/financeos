package com.financeos.auth.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @Builder.Default
    private Boolean ativo = true;

    // ── Controle de segurança ──────────────────────────────

    @Builder.Default
    private Integer tentativasLogin = 0;      // tentativas de login erradas

    private LocalDateTime bloqueadoAte;        // null = não bloqueado

    private LocalDateTime senhaExpiradaEm;     // null = nunca expira

    private LocalDateTime contaExpiradaEm;     // null = nunca expira

    // ── Métodos exigidos pelo UserDetails ──────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonLocked() {
        // está desbloqueada se bloqueadoAte for nulo OU já passou
        return bloqueadoAte == null || LocalDateTime.now().isAfter(bloqueadoAte);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // credenciais válidas se senhaExpiradaEm for nula OU ainda não passou
        return senhaExpiradaEm == null || LocalDateTime.now().isBefore(senhaExpiradaEm);
    }

    @Override
    public boolean isAccountNonExpired() {
        // conta válida se contaExpiradaEm for nula OU ainda não passou
        return contaExpiradaEm == null || LocalDateTime.now().isBefore(contaExpiradaEm);
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }

}

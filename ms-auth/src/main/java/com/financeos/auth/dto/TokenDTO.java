package com.financeos.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {
    private String token;
    private String tipo;
    private String email;
    private String role;
    private Long expiracao;
}

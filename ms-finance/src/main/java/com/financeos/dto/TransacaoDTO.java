package com.financeos.dto;

import com.financeos.model.TipoTransacao;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TransacaoDTO {
    private UUID id;

    @NotBlank(message = "Descricao e obrigatoria")
    private String descricao;

    @NotNull(message = "Valor e obrigatorio")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "Data e obrigatoria")
    private LocalDate data;

    @NotNull(message = "Tipo e obrigatorio")
    private TipoTransacao tipo;

    private String categoria;
    private Boolean pago;
    private Boolean recorrente;
}

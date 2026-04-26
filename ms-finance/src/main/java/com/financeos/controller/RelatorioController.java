package com.financeos.controller;

import com.financeos.model.Transacao;
import com.financeos.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final TransacaoService service;

    @GetMapping("/api/relatorios/mensal")
    public List<Transacao> relatorioMensal(@RequestParam String mes) {
        return service.buscarPorMes(mes);
    }

    @GetMapping("/api/relatorios/categorias")
    public Map<String, BigDecimal> gastosPorCategoria(@RequestParam String mes) {
        return service.gastosPorCategoria(mes);
    }

    @GetMapping("/api/fluxo-caixa")
    public Map<String, BigDecimal> fluxoCaixa(@RequestParam String mes) {
        return service.fluxoCaixa(mes);
    }
}

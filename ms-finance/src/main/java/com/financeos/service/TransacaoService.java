package com.financeos.service;

import com.financeos.dto.TransacaoDTO;
import com.financeos.model.Transacao;
import com.financeos.model.TipoTransacao;
import com.financeos.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository repository;

    public List<Transacao> listarTodas() {
        return repository.findAll();
    }

    public Optional<Transacao> buscarPorId(UUID id) {
        return repository.findById(id);
    }

    public Transacao criar(TransacaoDTO dto) {
        Transacao transacao = Transacao.builder()
                .descricao(dto.getDescricao())
                .valor(dto.getValor())
                .data(dto.getData())
                .tipo(dto.getTipo())
                .categoria(dto.getCategoria())
                .pago(dto.getPago() != null ? dto.getPago() : false)
                .recorrente(dto.getRecorrente() != null ? dto.getRecorrente() : false)
                .build();
        return repository.save(transacao);
    }

    public Optional<Transacao> atualizar(UUID id, TransacaoDTO dto) {
        return repository.findById(id).map(t -> {
            t.setDescricao(dto.getDescricao());
            t.setValor(dto.getValor());
            t.setData(dto.getData());
            t.setTipo(dto.getTipo());
            t.setCategoria(dto.getCategoria());
            t.setPago(dto.getPago());
            return repository.save(t);
        });
    }

    public boolean deletar(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Transacao> buscarPorMes(String mes) {
        YearMonth ym = YearMonth.parse(mes);
        return repository.findByDataBetweenOrderByDataDesc(ym.atDay(1), ym.atEndOfMonth());
    }

    public Map<String, BigDecimal> gastosPorCategoria(String mes) {
        YearMonth ym = YearMonth.parse(mes);
        List<Object[]> resultados = repository.findGastosPorCategoria(ym.atDay(1), ym.atEndOfMonth());
        Map<String, BigDecimal> mapa = new LinkedHashMap<>();
        for (Object[] row : resultados) {
            mapa.put((String) row[0], (BigDecimal) row[1]);
        }
        return mapa;
    }

    public Map<String, BigDecimal> fluxoCaixa(String mes) {
        YearMonth ym = YearMonth.parse(mes);
        List<Transacao> receitas = repository.findByTipoAndDataBetween(TipoTransacao.RECEITA, ym.atDay(1), ym.atEndOfMonth());
        List<Transacao> despesas = repository.findByTipoAndDataBetween(TipoTransacao.DESPESA, ym.atDay(1), ym.atEndOfMonth());
        BigDecimal totalR = receitas.stream().map(Transacao::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalD = despesas.stream().map(Transacao::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, BigDecimal> fluxo = new LinkedHashMap<>();
        fluxo.put("receitas", totalR);
        fluxo.put("despesas", totalD);
        fluxo.put("saldo", totalR.subtract(totalD));
        return fluxo;
    }
}

package com.financeos.repository;

import com.financeos.model.TipoTransacao;
import com.financeos.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
    List<Transacao> findByDataBetweenOrderByDataDesc(LocalDate inicio, LocalDate fim);
    List<Transacao> findByTipoAndDataBetween(TipoTransacao tipo, LocalDate inicio, LocalDate fim);

    @Query("SELECT t.categoria, SUM(t.valor) FROM Transacao t " +
    "WHERE t.tipo = 'DESPESA' " +
    "AND t.data BETWEEN :inicio AND :fim " +
    "GROUP By t.categoria")
    List<Object[]> findGastosPorCategoria(LocalDate inicio, LocalDate fim);
}

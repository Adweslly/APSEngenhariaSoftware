package com.aps.dados.repositorio;

import com.aps.domain.model.Processo;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.RedeProcessos;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

public class RepositorioProcesso {

    private final Map<Long, Processo> processos;
    private final Map<Long, Fluxo> fluxos;
    private final Map<Long, RedeProcessos> redes;
    private Long nextProcessoId;
    private Long nextFluxoId;
    private Long nextRedeId;

    public RepositorioProcesso() {
        this.processos = new HashMap<>();
        this.fluxos = new HashMap<>();
        this.redes = new HashMap<>();
        this.nextProcessoId = 1L;
        this.nextFluxoId = 1L;
        this.nextRedeId = 1L;
    }

    public Processo save(Processo processo) {
        if (processo.getId() == null) {
            processo.setId(nextProcessoId++);
        }
        processos.put(processo.getId(), processo);
        return processo;
    }

    public Optional<Processo> findById(Long id) {
        return Optional.ofNullable(processos.get(id));
    }

    public List<Processo> findAll() {
        return new ArrayList<>(processos.values());
    }

    public void delete(Long id) {
        processos.remove(id);
    }

    public Fluxo save(Fluxo fluxo) {
        if (fluxo.getId() == null) {
            fluxo.setId(nextFluxoId++);
        }
        fluxos.put(fluxo.getId(), fluxo);
        return fluxo;
    }

    public List<Fluxo> findAllFluxos() {
        return new ArrayList<>(fluxos.values());
    }

    public RedeProcessos save(RedeProcessos rede) {
        if (rede.getId() == null) {
            rede.setId(nextRedeId++);
        }
        redes.put(rede.getId(), rede);
        return rede;
    }

    public Optional<RedeProcessos> findRedeById(Long id) {
        return Optional.ofNullable(redes.get(id));
    }

    public List<RedeProcessos> findAllRedes() {
        return new ArrayList<>(redes.values());
    }

    public void deleteRede(Long id) {
        redes.remove(id);
    }
}
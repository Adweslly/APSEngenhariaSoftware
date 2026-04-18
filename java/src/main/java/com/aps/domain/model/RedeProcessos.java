package com.aps.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedeProcessos {
    private Long id;
    private String nome;
    private String descricao;
    private List<Processo> processos;
    private List<Fluxo> fluxos;
    private LocalDateTime dataCriacao;

    public RedeProcessos() {
        this.processos = new ArrayList<>();
        this.fluxos = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
    }

    public RedeProcessos(Long id, String nome) {
        this.id = id;
        this.nome = nome;
        this.processos = new ArrayList<>();
        this.fluxos = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
    }

    public void addProcesso(Processo processo) {
        this.processos.add(processo);
    }

    public void addFluxo(Fluxo fluxo) {
        this.fluxos.add(fluxo);
    }

    public Map<Processo, Double> calcularEmergia() {
        Map<Processo, Double> resultados = new HashMap<>();
        for (Processo processo : processos) {
            double emergiaTotal = 0;
            for (Fluxo fluxo : processo.getInputs()) {
                emergiaTotal += fluxo.getCustoEmergia();
            }
            resultados.put(processo, emergiaTotal);
        }
        return resultados;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public List<Processo> getProcessos() { return processos; }
    public void setProcessos(List<Processo> processos) { this.processos = processos; }
    public List<Fluxo> getFluxos() { return fluxos; }
    public void setFluxos(List<Fluxo> fluxos) { this.fluxos = fluxos; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
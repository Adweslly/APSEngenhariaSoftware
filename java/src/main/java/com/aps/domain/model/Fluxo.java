package com.aps.domain.model;

public class Fluxo {
    private Long id;
    private double quantidade;
    private TipoRecurso tipoRecurso;
    private double transformidade;
    private double custoEmergia;
    private Processo origem;
    private Processo destino;

    public Fluxo() {}

    public Fluxo(Long id, double quantidade, TipoRecurso tipoRecurso, Processo origem, Processo destino) {
        this.id = id;
        this.quantidade = quantidade;
        this.tipoRecurso = tipoRecurso;
        this.transformidade = tipoRecurso.getTransformidade();
        this.origem = origem;
        this.destino = destino;
    }

    public void calcularCustoEmergia() {
        this.custoEmergia = this.quantidade * this.transformidade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }
    public TipoRecurso getTipoRecurso() { return tipoRecurso; }
    public void setTipoRecurso(TipoRecurso tipoRecurso) { this.tipoRecurso = tipoRecurso; }
    public double getTransformidade() { return transformidade; }
    public void setTransformidade(double transformidade) { this.transformidade = transformidade; }
    public double getCustoEmergia() { return custoEmergia; }
    public void setCustoEmergia(double custoEmergia) { this.custoEmergia = custoEmergia; }
    public Processo getOrigem() { return origem; }
    public void setOrigem(Processo origem) { this.origem = origem; }
    public Processo getDestino() { return destino; }
    public void setDestino(Processo destino) { this.destino = destino; }
}
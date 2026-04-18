package com.aps.web.dto;

public class FluxoDTO {
    private Long id;
    private double quantidade;
    private Long tipoRecursoId;
    private Long origemId;
    private Long destinoId;

    public FluxoDTO() {}

    public FluxoDTO(Long id, double quantidade, Long tipoRecursoId, Long origemId, Long destinoId) {
        this.id = id;
        this.quantidade = quantidade;
        this.tipoRecursoId = tipoRecursoId;
        this.origemId = origemId;
        this.destinoId = destinoId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }
    public Long getTipoRecursoId() { return tipoRecursoId; }
    public void setTipoRecursoId(Long tipoRecursoId) { this.tipoRecursoId = tipoRecursoId; }
    public Long getOrigemId() { return origemId; }
    public void setOrigemId(Long origemId) { this.origemId = origemId; }
    public Long getDestinoId() { return destinoId; }
    public void setDestinoId(Long destinoId) { this.destinoId = destinoId; }
}
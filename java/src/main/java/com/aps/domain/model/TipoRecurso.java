package com.aps.domain.model;

import com.aps.domain.enums.CategoriaRecurso;

public class TipoRecurso {
    private Long id;
    private String nome;
    private String unidade;
    private CategoriaRecurso categoria;
    private double transformidade;

    public TipoRecurso() {}

    public TipoRecurso(Long id, String nome, String unidade, CategoriaRecurso categoria, double transformidade) {
        this.id = id;
        this.nome = nome;
        this.unidade = unidade;
        this.categoria = categoria;
        this.transformidade = transformidade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public CategoriaRecurso getCategoria() { return categoria; }
    public void setCategoria(CategoriaRecurso categoria) { this.categoria = categoria; }
    public double getTransformidade() { return transformidade; }
    public void setTransformidade(double transformidade) { this.transformidade = transformidade; }
}
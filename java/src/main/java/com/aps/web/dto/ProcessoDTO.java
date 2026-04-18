package com.aps.web.dto;

import com.aps.domain.enums.TipoProcesso;

public class ProcessoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private TipoProcesso tipo;
    private String categoria;

    public ProcessoDTO() {}

    public ProcessoDTO(Long id, String nome, String descricao, TipoProcesso tipo, String categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public TipoProcesso getTipo() { return tipo; }
    public void setTipo(TipoProcesso tipo) { this.tipo = tipo; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
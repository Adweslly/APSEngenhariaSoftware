package com.aps.domain.model;

import com.aps.domain.enums.TipoProcesso;
import java.util.ArrayList;
import java.util.List;

public class Processo {
    private Long id;
    private String nome;
    private String descricao;
    private TipoProcesso tipo;
    private String categoria;
    private List<Fluxo> inputs;
    private List<Fluxo> outputs;

    public Processo() {
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public Processo(Long id, String nome, TipoProcesso tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public void addInput(Fluxo fluxo) {
        this.inputs.add(fluxo);
    }

    public void addOutput(Fluxo fluxo) {
        this.outputs.add(fluxo);
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
    public List<Fluxo> getInputs() { return inputs; }
    public void setInputs(List<Fluxo> inputs) { this.inputs = inputs; }
    public List<Fluxo> getOutputs() { return outputs; }
    public void setOutputs(List<Fluxo> outputs) { this.outputs = outputs; }
}
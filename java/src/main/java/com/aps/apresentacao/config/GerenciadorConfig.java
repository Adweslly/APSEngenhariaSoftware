package com.aps.apresentacao.config;

import java.util.HashMap;
import java.util.Map;

public class GerenciadorConfig {

    private static GerenciadorConfig instancia;
    private Map<String, String> configuracoes;

    private GerenciadorConfig() {
        this.configuracoes = new HashMap<>();
        configurarDefaults();
    }

    public static GerenciadorConfig getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorConfig();
        }
        return instancia;
    }

    private void configurarDefaults() {
        configuracoes.put("formatoSaida", "JSON");
        configuracoes.put("precisaoDecimal", "4");
        configuracoes.put("validarAlgebra", "true");
        configuracoes.put("maxProcessos", "1000");
        configuracoes.put("maxFluxos", "10000");
    }

    public String get(String chave) {
        return configuracoes.get(chave);
    }

    public void set(String chave, String valor) {
        configuracoes.put(chave, valor);
    }

    public Map<String, String> getTodas() {
        return new HashMap<>(configuracoes);
    }
}
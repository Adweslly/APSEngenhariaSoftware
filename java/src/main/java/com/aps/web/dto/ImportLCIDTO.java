package com.aps.web.dto;

public class ImportLCIDTO {
    private String conteudo;
    private String formato;

    public ImportLCIDTO() {}

    public ImportLCIDTO(String conteudo, String formato) {
        this.conteudo = conteudo;
        this.formato = formato;
    }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }
}
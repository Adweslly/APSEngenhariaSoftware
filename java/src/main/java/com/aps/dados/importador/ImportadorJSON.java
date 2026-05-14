package com.aps.dados.importador;

import com.aps.domain.enums.TipoProcesso;
import com.aps.domain.model.Processo;
import com.aps.domain.model.Fluxo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class ImportadorJSON implements ImportadorLCI {

    private final ObjectMapper objectMapper;

    public ImportadorJSON() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<Processo> importarProcessos(String conteudo) {
        List<Processo> processos = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(conteudo);
            JsonNode processosNode = root.get("processos");
            if (processosNode != null && processosNode.isArray()) {
                for (JsonNode node : processosNode) {
                    Processo processo = new Processo();
                    processo.setId(node.has("id") ? node.get("id").asLong() : null);
                    processo.setNome(node.get("nome").asText());
                    processo.setDescricao(node.has("descricao") ? node.get("descricao").asText() : "");
                    processo.setTipo(TipoProcesso.valueOf(node.get("tipo").asText()));
                    processo.setCategoria(node.has("categoria") ? node.get("categoria").asText() : "");
                    processos.add(processo);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar processos: " + e.getMessage(), e);
        }
        return processos;
    }

    @Override
    public List<Fluxo> importarFluxos(String conteudo) {
        List<Fluxo> fluxos = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(conteudo);
            JsonNode fluxosNode = root.get("fluxos");
            if (fluxosNode != null && fluxosNode.isArray()) {
                for (JsonNode node : fluxosNode) {
                    Fluxo fluxo = new Fluxo();
                    fluxo.setId(node.has("id") ? node.get("id").asLong() : null);
                    fluxo.setQuantidade(node.get("quantidade").asDouble());
                    
                    // We set IDs here, but the service will need to resolve them into actual objects
                    // Using placeholders for now if needed, or just relying on the Service to handle IDs
                    fluxos.add(fluxo);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar fluxos: " + e.getMessage(), e);
        }
        return fluxos;
    }

    @Override
    public boolean validarFormato(String conteudo) {
        try {
            JsonNode root = objectMapper.readTree(conteudo);
            return root.has("processos") || root.has("fluxos");
        } catch (Exception e) {
            return false;
        }
    }
}
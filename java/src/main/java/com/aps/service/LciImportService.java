package com.aps.service;

import com.aps.dados.importador.ImportadorJSON;
import com.aps.dados.importador.ImportadorLCI;
import com.aps.dados.repositorio.FluxoRepository;
import com.aps.dados.repositorio.ProcessoRepository;
import com.aps.dados.repositorio.TipoRecursoRepository;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.Processo;
import com.aps.web.dto.ImportLCIDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LciImportService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private FluxoRepository fluxoRepository;

    @Autowired
    private TipoRecursoRepository tipoRecursoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void importar(ImportLCIDTO dto) {
        if ("JSON".equalsIgnoreCase(dto.getFormato())) {
            importarJSON(dto.getConteudo());
        } else {
            throw new RuntimeException("Formato não suportado: " + dto.getFormato());
        }
    }

    private void importarJSON(String conteudo) {
        try {
            JsonNode root = objectMapper.readTree(conteudo);
            Map<Long, Processo> mapaIds = new HashMap<>();

            // 1. Importar Processos
            JsonNode processosNode = root.get("processos");
            if (processosNode != null && processosNode.isArray()) {
                for (JsonNode node : processosNode) {
                    Processo p = new Processo();
                    p.setNome(node.get("nome").asText());
                    p.setDescricao(node.has("descricao") ? node.get("descricao").asText() : "");
                    p.setTipo(com.aps.domain.enums.TipoProcesso.valueOf(node.get("tipo").asText()));
                    p.setCategoria(node.has("categoria") ? node.get("categoria").asText() : "");
                    
                    Processo salvo = processoRepository.save(p);
                    if (node.has("id")) {
                        mapaIds.put(node.get("id").asLong(), salvo);
                    }
                }
            }

            // 2. Importar Fluxos
            JsonNode fluxosNode = root.get("fluxos");
            if (fluxosNode != null && fluxosNode.isArray()) {
                for (JsonNode node : fluxosNode) {
                    Fluxo f = new Fluxo();
                    f.setQuantidade(node.get("quantidade").asDouble());
                    
                    if (node.has("tipoRecursoId")) {
                        tipoRecursoRepository.findById(node.get("tipoRecursoId").asLong())
                                .ifPresent(f::setTipoRecurso);
                    }
                    
                    if (node.has("origemId")) {
                        f.setOrigem(mapaIds.get(node.get("origemId").asLong()));
                    }
                    
                    if (node.has("destinoId")) {
                        f.setDestino(mapaIds.get(node.get("destinoId").asLong()));
                    }
                    
                    if (f.getTipoRecurso() != null) {
                        f.setTransformidade(f.getTipoRecurso().getTransformidade());
                        f.calcularCustoEmergia();
                    }
                    
                    fluxoRepository.save(f);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro na importação JSON: " + e.getMessage(), e);
        }
    }
}

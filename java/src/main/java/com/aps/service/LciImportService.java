package com.aps.service;

import com.aps.dados.repositorio.FluxoRepository;
import com.aps.dados.repositorio.ProcessoRepository;
import com.aps.dados.repositorio.TipoRecursoRepository;
import com.aps.domain.enums.CategoriaRecurso;
import com.aps.domain.enums.TipoFonte;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.Processo;
import com.aps.domain.model.TipoRecurso;
import com.aps.web.dto.ImportLCIDTO;
import com.aps.web.dto.ResultadoDTO;
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

    @Autowired
    private EmergyCalculationService calculationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public List<ResultadoDTO> importar(ImportLCIDTO dto) {
        if ("JSON".equalsIgnoreCase(dto.getFormato())) {
            importarJSON(dto.getConteudo());
            return calculationService.calcularTudo();
        }

        throw new RuntimeException("Formato nao suportado: " + dto.getFormato());
    }

    private void importarJSON(String conteudo) {
        try {
            JsonNode root = objectMapper.readTree(conteudo);
            Map<Long, Processo> mapaIds = new HashMap<>();
            Map<Long, TipoRecurso> tiposRecursoImportados = new HashMap<>();

            JsonNode processosNode = root.get("processos");
            if (processosNode != null && processosNode.isArray()) {
                for (JsonNode node : processosNode) {
                    Processo processo = new Processo();
                    processo.setNome(node.get("nome").asText());
                    processo.setDescricao(node.has("descricao") ? node.get("descricao").asText() : "");
                    processo.setTipo(com.aps.domain.enums.TipoProcesso.valueOf(node.get("tipo").asText().toUpperCase()));
                    processo.setCategoria(node.has("categoria") ? node.get("categoria").asText() : "");

                    Processo salvo = processoRepository.save(processo);
                    if (node.has("id")) {
                        mapaIds.put(node.get("id").asLong(), salvo);
                    }
                }
            }

            JsonNode fluxosNode = root.get("fluxos");
            if (fluxosNode != null && fluxosNode.isArray()) {
                for (JsonNode node : fluxosNode) {
                    Fluxo fluxo = new Fluxo();
                    fluxo.setQuantidade(node.get("quantidade").asDouble());
                    fluxo.setTipoRecurso(resolverTipoRecurso(node, mapaIds, tiposRecursoImportados));

                    if (node.has("origemId")) {
                        fluxo.setOrigem(mapaIds.get(node.get("origemId").asLong()));
                    }

                    if (node.has("destinoId")) {
                        fluxo.setDestino(mapaIds.get(node.get("destinoId").asLong()));
                    }

                    if (fluxo.getTipoRecurso() != null) {
                        fluxo.setTransformidade(fluxo.getTipoRecurso().getTransformidade());
                    } else if (node.has("transformidade")) {
                        fluxo.setTransformidade(node.get("transformidade").asDouble());
                    }

                    if (fluxo.getTransformidade() > 0) {
                        fluxo.calcularCustoEmergia();
                    }

                    fluxoRepository.save(fluxo);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro na importacao JSON: " + e.getMessage(), e);
        }
    }

    private TipoRecurso resolverTipoRecurso(JsonNode node, Map<Long, Processo> mapaIds, Map<Long, TipoRecurso> tiposRecursoImportados) {
        if (!node.has("tipoRecursoId")) {
            return null;
        }

        Long idLocal = node.get("tipoRecursoId").asLong();
        TipoRecurso existenteNaImportacao = tiposRecursoImportados.get(idLocal);
        if (existenteNaImportacao != null) {
            return existenteNaImportacao;
        }

        TipoRecurso novoTipoRecurso = new TipoRecurso();
        novoTipoRecurso.setNome(node.has("nome") ? node.get("nome").asText() : "Recurso Importado " + idLocal);
        novoTipoRecurso.setTransformidade(node.has("transformidade") ? node.get("transformidade").asDouble() : 1.0);
        novoTipoRecurso.setTipoFonte(inferirTipoFonte(node, mapaIds));
        novoTipoRecurso.setCategoria(CategoriaRecurso.ENERGIA);

        TipoRecurso salvo = tipoRecursoRepository.save(novoTipoRecurso);
        tiposRecursoImportados.put(idLocal, salvo);
        return salvo;
    }

    private TipoFonte inferirTipoFonte(JsonNode node, Map<Long, Processo> mapaIds) {
        if (node.has("origemId")) {
            Processo origem = mapaIds.get(node.get("origemId").asLong());
            if (origem != null && origem.getCategoria() != null) {
                String categoria = origem.getCategoria().toUpperCase();
                if (categoria.contains("NAO_RENOVAVEL")) {
                    return TipoFonte.NAO_RENOVAVEL;
                }
                if (categoria.contains("RENOVAVEL")) {
                    return TipoFonte.RENOVAVEL;
                }
                if (categoria.contains("COMPRADO") || categoria.contains("SERVICO")) {
                    return TipoFonte.COMPRADO;
                }
            }
        }

        return TipoFonte.COMPRADO;
    }
}

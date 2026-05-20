package com.aps.service;

import com.aps.dados.repositorio.ProcessoRepository;
import com.aps.dados.repositorio.ResultadoEmergiaRepository;
import com.aps.domain.enums.TipoProcesso;
import com.aps.domain.model.Processo;
import com.aps.domain.model.ResultadoEmergia;
import com.aps.processamento.algoritmo.CalculadorEmergia;
import com.aps.web.dto.ResultadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmergyCalculationService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ResultadoEmergiaRepository resultadoRepository;

    @Autowired
    private CalculadorEmergia calculador;

    @Transactional
    public List<ResultadoDTO> calcularTudo() {
        List<Processo> processos = processoRepository.findAll();
        Map<Processo, ResultadoEmergia> resultadosEntity = calculador.calcular(processos);
        Optional<Processo> processoFinal = identificarProcessoFinal(resultadosEntity);

        resultadoRepository.deleteAll();

        return resultadosEntity.entrySet().stream()
                .map(entry -> {
                    ResultadoEmergia saved = resultadoRepository.save(entry.getValue());
                    boolean isProcessoFinal = processoFinal.map(p -> p.equals(entry.getKey())).orElse(false);
                    return toDTO(saved, isProcessoFinal);
                })
                .collect(Collectors.toList());
    }

    private Optional<Processo> identificarProcessoFinal(Map<Processo, ResultadoEmergia> resultados) {
        Optional<Processo> comProdutoFinal = resultados.entrySet().stream()
                .filter(entry -> entry.getKey().getTipo() == TipoProcesso.TRANSFORMACAO)
                .filter(entry -> entry.getKey().getOutputs().stream().anyMatch(output -> output.getDestino() == null))
                .max((a, b) -> Double.compare(a.getValue().getEmergiaTotal(), b.getValue().getEmergiaTotal()))
                .map(Map.Entry::getKey);

        if (comProdutoFinal.isPresent()) {
            return comProdutoFinal;
        }

        return resultados.entrySet().stream()
                .filter(entry -> entry.getKey().getTipo() == TipoProcesso.TRANSFORMACAO)
                .max((a, b) -> Double.compare(a.getValue().getEmergiaTotal(), b.getValue().getEmergiaTotal()))
                .map(Map.Entry::getKey);
    }

    private ResultadoDTO toDTO(ResultadoEmergia entity, boolean processoFinal) {
        ResultadoDTO dto = new ResultadoDTO();
        dto.setId(entity.getId());
        dto.setProcessoId(entity.getProcesso().getId());
        dto.setProcessoNome(entity.getProcesso().getNome());
        dto.setEmergiaTotal(entity.getEmergiaTotal());
        dto.setEmergiaDireta(entity.getEmergiaDireta());
        dto.setEmergiaIndireta(entity.getEmergiaIndireta());
        dto.setTransformidade(entity.getTransformidade());
        dto.setRenovavel(entity.getRenovavel());
        dto.setNaoRenovavel(entity.getNaoRenovavel());
        dto.setComprado(entity.getComprado());
        dto.setProcessoFinal(processoFinal);
        dto.setDataCalculo(entity.getDataCalculo());
        dto.setEyr(entity.getEyr());
        dto.setElr(entity.getElr());
        dto.setEsi(entity.getEsi());

        return dto;
    }
}

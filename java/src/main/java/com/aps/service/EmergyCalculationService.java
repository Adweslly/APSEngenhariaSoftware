package com.aps.service;

import com.aps.dados.repositorio.ProcessoRepository;
import com.aps.dados.repositorio.ResultadoEmergiaRepository;
import com.aps.domain.enums.TipoFonte;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.Processo;
import com.aps.domain.model.ResultadoEmergia;
import com.aps.processamento.algoritmo.CalculadorEmergia;
import com.aps.web.dto.ResultadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

        return resultadosEntity.entrySet().stream()
                .map(entry -> {
                    Processo p = entry.getKey();
                    ResultadoEmergia res = entry.getValue();
                    
                    // Calcula os indicadores para este processo
                    calcularIndicadores(p, res, resultadosEntity);
                    
                    ResultadoEmergia saved = resultadoRepository.save(res);
                    return toDTO(saved);
                })
                .collect(Collectors.toList());
    }

    private void calcularIndicadores(Processo p, ResultadoEmergia res, Map<Processo, ResultadoEmergia> todosResultados) {
        double R = 0; // Recursos Renováveis
        double N = 0; // Recursos Não Renováveis
        double F = 0; // Recursos Comprados/Serviços (Feedback)

        for (Fluxo input : p.getInputs()) {
            double emergiaDoFluxo = 0;
            TipoFonte fonte = TipoFonte.COMPRADO;

            // 1. Calcular a emergia do fluxo
            if (input.getOrigem() == null || input.getOrigem().getTipo() == com.aps.domain.enums.TipoProcesso.RECURSO) {
                emergiaDoFluxo = input.getQuantidade() * input.getTransformidade();
            } else {
                ResultadoEmergia resOrigem = todosResultados.get(input.getOrigem());
                if (resOrigem != null) {
                    double totalSaidaOrigem = input.getOrigem().getOutputs().stream().mapToDouble(Fluxo::getQuantidade).sum();
                    if (totalSaidaOrigem > 0) {
                        emergiaDoFluxo = resOrigem.getEmergiaTotal() * (input.getQuantidade() / totalSaidaOrigem);
                    }
                }
            }

            // 2. Identificar a fonte (Prioridade: TipoRecurso > Categoria do Processo Origem)
            if (input.getTipoRecurso() != null && input.getTipoRecurso().getTipoFonte() != null) {
                fonte = input.getTipoRecurso().getTipoFonte();
            } else if (input.getOrigem() != null && input.getOrigem().getCategoria() != null) {
                String cat = input.getOrigem().getCategoria().toUpperCase();
                if (cat.contains("RENOVAVEL")) fonte = TipoFonte.RENOVAVEL;
                else if (cat.contains("NAO_RENOVAVEL")) fonte = TipoFonte.NAO_RENOVAVEL;
                else if (cat.contains("COMPRADO") || cat.contains("SERVICO")) fonte = TipoFonte.COMPRADO;
            }

            // 3. Acumular
            if (fonte == TipoFonte.RENOVAVEL) R += emergiaDoFluxo;
            else if (fonte == TipoFonte.NAO_RENOVAVEL) N += emergiaDoFluxo;
            else F += emergiaDoFluxo;
        }

        double Y = res.getEmergiaTotal(); 
        if (Y <= 0) Y = R + N + F; // Fallback se o motor falhou mas as entradas existem
        
        // EYR = Y / F (Emergy Yield Ratio)
        double eyr = F > 0 ? Y / F : (R + N > 0 ? 10.0 : 0.0);
        
        // ELR = (F + N) / R (Environmental Loading Ratio)
        double elr = R > 0 ? (F + N) / R : (F + N > 0 ? 10.0 : 0.0);
        
        // ESI = EYR / ELR (Emergy Sustainability Index)
        double esi = elr > 0 ? eyr / elr : 0;

        res.setEyr(eyr);
        res.setElr(elr);
        res.setEsi(esi);
        res.setEmergiaTotal(Y);
        res.setEmergiaDireta(R + N + F);
    }

    private ResultadoDTO toDTO(ResultadoEmergia entity) {
        ResultadoDTO dto = new ResultadoDTO();
        dto.setId(entity.getId());
        dto.setProcessoId(entity.getProcesso().getId());
        dto.setProcessoNome(entity.getProcesso().getNome());
        dto.setEmergiaTotal(entity.getEmergiaTotal());
        dto.setEmergiaDireta(entity.getEmergiaDireta());
        dto.setEmergiaIndireta(entity.getEmergiaIndireta());
        dto.setTransformidade(entity.getTransformidade());
        dto.setDataCalculo(entity.getDataCalculo());
        dto.setEyr(entity.getEyr());
        dto.setElr(entity.getElr());
        dto.setEsi(entity.getEsi());
        
        return dto;
    }
}

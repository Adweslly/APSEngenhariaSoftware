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
                    calcularIndicadores(p, res);
                    
                    ResultadoEmergia saved = resultadoRepository.save(res);
                    return toDTO(saved);
                })
                .collect(Collectors.toList());
    }

    private void calcularIndicadores(Processo p, ResultadoEmergia res) {
        double R = 0; // Recursos Renováveis
        double N = 0; // Recursos Não Renováveis
        double F = 0; // Recursos Comprados/Serviços (Feedback)

        for (Fluxo input : p.getInputs()) {
            if (input.getOrigem() == null && input.getTipoRecurso() != null) {
                double emergia = input.getQuantidade() * input.getTransformidade();
                TipoFonte fonte = input.getTipoRecurso().getTipoFonte();
                
                if (fonte == TipoFonte.RENOVAVEL) R += emergia;
                else if (fonte == TipoFonte.NAO_RENOVAVEL) N += emergia;
                else if (fonte == TipoFonte.COMPRADO) F += emergia;
            }
            // Para fluxos internos, seria necessário propagar as proporções de R, N e F.
            // No MVP, tratamos fluxos internos apenas como parte da propagação do cálculo principal.
        }

        double Y = res.getEmergiaTotal(); // Rendimento (Yield)
        
        // EYR = Y / F (Emergy Yield Ratio)
        double eyr = F > 0 ? Y / F : 0;
        
        // ELR = (F + N) / R (Environmental Loading Ratio)
        double elr = R > 0 ? (F + N) / R : 0;
        
        // ESI = EYR / ELR (Emergy Sustainability Index)
        double esi = elr > 0 ? eyr / elr : 0;

        res.setEyr(eyr);
        res.setElr(elr);
        res.setEsi(esi);
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

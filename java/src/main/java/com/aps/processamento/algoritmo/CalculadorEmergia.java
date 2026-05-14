package com.aps.processamento.algoritmo;

import com.aps.domain.model.Processo;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.ResultadoEmergia;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CalculadorEmergia {

    private static final int MAX_ITERATIONS = 100;
    private static final double CONVERGENCE_THRESHOLD = 0.0001;

    public Map<Processo, ResultadoEmergia> calcular(List<Processo> processos) {
        Map<Processo, Double> emergiaAcumulada = new HashMap<>();
        Map<Processo, ResultadoEmergia> resultados = new HashMap<>();

        // Inicialização
        for (Processo p : processos) {
            emergiaAcumulada.put(p, 0.0);
            resultados.put(p, new ResultadoEmergia(p));
        }

        // Cálculo iterativo (trata loops e propagação)
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            Map<Processo, Double> proximaEmergia = new HashMap<>();
            double maxDiff = 0;

            for (Processo p : processos) {
                double emergiaEntrada = 0;

                // Debug: Verificando entradas do processo
                List<Fluxo> inputs = p.getInputs();
                
                for (Fluxo input : inputs) {
                    if (input.getOrigem() == null) {
                        // Entrada externa (Recurso Primário)
                        emergiaEntrada += input.getQuantidade() * input.getTransformidade();
                    } else {
                        // Entrada interna (Propagação de outro processo)
                        Processo origem = input.getOrigem();
                        double emergiaTotalOrigem = emergiaAcumulada.getOrDefault(origem, 0.0);
                        
                        // Se a origem for um RECURSO sem origem própria, tratamos como fonte primária
                        if (origem.getTipo() == com.aps.domain.enums.TipoProcesso.RECURSO && 
                            origem.getInputs().isEmpty()) {
                            emergiaEntrada += input.getQuantidade() * input.getTransformidade();
                        } else {
                            // Split: recebe emergia proporcional à energia que flui
                            double energiaTotalSaidaOrigem = origem.getOutputs().stream()
                                    .mapToDouble(Fluxo::getQuantidade).sum();
                            
                            if (energiaTotalSaidaOrigem > 0) {
                                double proporcao = input.getQuantidade() / energiaTotalSaidaOrigem;
                                emergiaEntrada += emergiaTotalOrigem * proporcao;
                            }
                        }
                    }
                }

                double anterior = emergiaAcumulada.get(p);
                double diff = Math.abs(emergiaEntrada - anterior);
                maxDiff = Math.max(maxDiff, diff);
                proximaEmergia.put(p, emergiaEntrada);
            }

            emergiaAcumulada = proximaEmergia;

            // Convergência
            if (maxDiff < CONVERGENCE_THRESHOLD && iter > 0) break;
        }

        // Finaliza os resultados
        for (Processo p : processos) {
            ResultadoEmergia res = resultados.get(p);
            double emergiaTotal = emergiaAcumulada.get(p);
            
            double energiaTotalSaida = p.getOutputs().stream()
                    .mapToDouble(Fluxo::getQuantidade).sum();
            
            res.setEmergiaTotal(emergiaTotal);
            res.setTransformidade(energiaTotalSaida > 0 ? emergiaTotal / energiaTotalSaida : 0);
            res.setEmergiaDireta(emergiaTotal); // Simplificado no MVP
        }

        return resultados;
    }
}

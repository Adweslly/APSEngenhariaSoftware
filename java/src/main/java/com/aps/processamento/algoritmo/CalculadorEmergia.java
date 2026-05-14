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

                for (Fluxo input : p.getInputs()) {
                    if (input.getOrigem() == null) {
                        // Entrada externa, utiliza a transformidade base
                        emergiaEntrada += input.getQuantidade() * input.getTransformidade();
                    } else {
                        // Entrada interna, utiliza a emergia calculada da origem
                        Processo origem = input.getOrigem();
                        double emergiaTotalOrigem = emergiaAcumulada.get(origem);
                        
                        if (origem.isCoproduto()) {
                            // Co-produto: recebe a emergia total da origem
                            emergiaEntrada += emergiaTotalOrigem;
                        } else {
                            // Split (Divisão): recebe emergia proporcional
                            double energiaTotalSaida = origem.getOutputs().stream()
                                    .mapToDouble(Fluxo::getQuantidade).sum();
                            if (energiaTotalSaida > 0) {
                                emergiaEntrada += emergiaTotalOrigem * (input.getQuantidade() / energiaTotalSaida);
                            }
                        }
                    }
                }

                double diff = Math.abs(emergiaEntrada - emergiaAcumulada.get(p));
                maxDiff = Math.max(maxDiff, diff);
                proximaEmergia.put(p, emergiaEntrada);
            }

            emergiaAcumulada = proximaEmergia;

            if (maxDiff < CONVERGENCE_THRESHOLD && iter > 0) {
                break;
            }
        }

        // Finaliza os resultados
        for (Processo p : processos) {
            ResultadoEmergia res = resultados.get(p);
            double emergiaTotal = emergiaAcumulada.get(p);
            
            double energiaTotalSaida = p.getOutputs().stream()
                    .mapToDouble(Fluxo::getQuantidade).sum();
            
            res.setEmergiaTotal(emergiaTotal);
            res.setTransformidade(energiaTotalSaida > 0 ? emergiaTotal / energiaTotalSaida : 0);
            
            // Para o MVP, simplificamos direta/indireta
            // O cálculo real requer rastreamento detalhado dos tipos de fonte
            res.setEmergiaDireta(emergiaTotal); // Simplificado
            res.setEmergiaIndireta(0);
        }

        return resultados;
    }
}

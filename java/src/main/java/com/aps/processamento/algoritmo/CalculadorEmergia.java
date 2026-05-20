package com.aps.processamento.algoritmo;

import com.aps.domain.enums.TipoFonte;
import com.aps.domain.enums.TipoProcesso;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.Processo;
import com.aps.domain.model.ResultadoEmergia;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CalculadorEmergia {

    private static final int MAX_ITERATIONS = 100;
    private static final double CONVERGENCE_THRESHOLD = 0.0001;

    public Map<Processo, ResultadoEmergia> calcular(List<Processo> processos) {
        Map<Object, EstadoEmergia> estadoAtual = new HashMap<>();

        for (Processo processo : processos) {
            estadoAtual.put(chaveProcesso(processo), EstadoEmergia.vazio());
        }

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            Map<Object, EstadoEmergia> proximoEstado = new HashMap<>();
            double maxDiff = 0;

            for (Processo processo : processos) {
                EstadoEmergia calculado = calcularProcesso(processo, estadoAtual);
                EstadoEmergia anterior = estadoAtual.get(chaveProcesso(processo));

                maxDiff = Math.max(maxDiff, Math.abs(calculado.total() - anterior.total()));
                proximoEstado.put(chaveProcesso(processo), calculado);
            }

            estadoAtual = proximoEstado;

            if (maxDiff < CONVERGENCE_THRESHOLD && iter > 0) {
                break;
            }
        }

        Map<Processo, ResultadoEmergia> resultados = new HashMap<>();
        for (Processo processo : processos) {
            EstadoEmergia estado = estadoAtual.get(chaveProcesso(processo));
            ResultadoEmergia resultado = new ResultadoEmergia(processo);
            double total = estado.total();
            double totalSaidas = processo.getOutputs().stream()
                    .mapToDouble(Fluxo::getQuantidade)
                    .sum();

            resultado.setRenovavel(estado.renovavel);
            resultado.setNaoRenovavel(estado.naoRenovavel);
            resultado.setComprado(estado.comprado);
            resultado.setEmergiaTotal(total);
            resultado.setEmergiaDireta(estado.direta);
            resultado.setEmergiaIndireta(Math.max(0, total - estado.direta));
            resultado.setTransformidade(totalSaidas > 0 ? total / totalSaidas : 0);
            resultado.setEyr(estado.comprado > 0 ? total / estado.comprado : 0);
            resultado.setElr(estado.renovavel > 0 ? (estado.naoRenovavel + estado.comprado) / estado.renovavel : 0);
            resultado.setEsi(resultado.getElr() > 0 ? resultado.getEyr() / resultado.getElr() : 0);

            resultados.put(processo, resultado);
        }

        return resultados;
    }

    private EstadoEmergia calcularProcesso(Processo processo, Map<Object, EstadoEmergia> estadoAtual) {
        EstadoEmergia estado = EstadoEmergia.vazio();

        if (processo.getTipo() == TipoProcesso.RECURSO && processo.getInputs().isEmpty()) {
            for (Fluxo output : processo.getOutputs()) {
                double emergia = output.getQuantidade() * output.getTransformidade();
                estado.adicionar(classificarFonte(output), emergia);
                estado.direta += emergia;
            }
            return estado;
        }

        for (Fluxo input : processo.getInputs()) {
            Processo origem = input.getOrigem();

            if (origem == null || origem.getTipo() == TipoProcesso.RECURSO) {
                double emergia = input.getQuantidade() * input.getTransformidade();
                estado.adicionar(classificarFonte(input), emergia);
                estado.direta += emergia;
                continue;
            }

            EstadoEmergia estadoOrigem = estadoAtual.getOrDefault(chaveProcesso(origem), EstadoEmergia.vazio());
            double totalSaidaOrigem = origem.getOutputs().stream()
                    .mapToDouble(Fluxo::getQuantidade)
                    .sum();

            if (totalSaidaOrigem > 0) {
                double proporcao = input.getQuantidade() / totalSaidaOrigem;
                estado.renovavel += estadoOrigem.renovavel * proporcao;
                estado.naoRenovavel += estadoOrigem.naoRenovavel * proporcao;
                estado.comprado += estadoOrigem.comprado * proporcao;
            }
        }

        return estado;
    }

    private TipoFonte classificarFonte(Fluxo fluxo) {
        if (fluxo.getTipoRecurso() != null && fluxo.getTipoRecurso().getTipoFonte() != null) {
            return fluxo.getTipoRecurso().getTipoFonte();
        }

        Processo origem = fluxo.getOrigem();
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

        return TipoFonte.COMPRADO;
    }

    private Object chaveProcesso(Processo processo) {
        return processo.getId() != null ? processo.getId() : processo;
    }

    private static class EstadoEmergia {
        private double renovavel;
        private double naoRenovavel;
        private double comprado;
        private double direta;

        private static EstadoEmergia vazio() {
            return new EstadoEmergia();
        }

        private double total() {
            return renovavel + naoRenovavel + comprado;
        }

        private void adicionar(TipoFonte fonte, double emergia) {
            if (fonte == TipoFonte.RENOVAVEL) {
                renovavel += emergia;
            } else if (fonte == TipoFonte.NAO_RENOVAVEL) {
                naoRenovavel += emergia;
            } else {
                comprado += emergia;
            }
        }
    }
}

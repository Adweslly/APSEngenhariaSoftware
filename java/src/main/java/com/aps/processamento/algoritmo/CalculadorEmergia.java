package com.aps.processamento.algoritmo;

import com.aps.domain.model.Processo;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.RedeProcessos;
import com.aps.domain.model.ResultadoEmergia;
import java.util.HashMap;
import java.util.Map;

public class CalculadorEmergia {

    public Map<Processo, ResultadoEmergia> calcular(RedeProcessos rede) {
        Map<Processo, ResultadoEmergia> resultados = new HashMap<>();

        for (Processo processo : rede.getProcessos()) {
            ResultadoEmergia resultado = calcularProcesso(processo);
            resultados.put(processo, resultado);
        }

        return resultados;
    }

    private ResultadoEmergia calcularProcesso(Processo processo) {
        ResultadoEmergia resultado = new ResultadoEmergia(processo);

        double emergiaDireta = 0;
        double emergiaIndireta = 0;

        for (Fluxo fluxo : processo.getInputs()) {
            fluxo.calcularCustoEmergia();
            emergiaDireta += fluxo.getCustoEmergia();
        }

        double emergiaTotal = emergiaDireta + emergiaIndireta;

        double energiaSaida = processo.getOutputs().stream()
            .mapToDouble(Fluxo::getQuantidade)
            .sum();

        double transformidade = energiaSaida > 0 ? emergiaTotal / energiaSaida : 0;

        resultado.setEmergiaDireta(emergiaDireta);
        resultado.setEmergiaIndireta(emergiaIndireta);
        resultado.setEmergiaTotal(emergiaTotal);
        resultado.setTransformidade(transformidade);

        return resultado;
    }
}
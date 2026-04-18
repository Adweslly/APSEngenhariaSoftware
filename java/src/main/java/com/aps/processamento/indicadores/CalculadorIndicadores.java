package com.aps.processamento.indicadores;

import com.aps.domain.model.Processo;
import com.aps.domain.model.RedeProcessos;
import com.aps.domain.model.ResultadoEmergia;
import java.util.Map;

public class CalculadorIndicadores {

    public double calcularELR(RedeProcessos rede, Map<Processo, ResultadoEmergia> resultados) {
        double emergiaRenovavel = 0;
        double emergiaNaoRenovavel = 0;

        for (Processo processo : rede.getProcessos()) {
            ResultadoEmergia resultado = resultados.get(processo);
            if (resultado != null) {
                if (processo.getCategoria() != null &&
                    processo.getCategoria().equals("RENOVAVEL")) {
                    emergiaRenovavel += resultado.getEmergiaTotal();
                } else {
                    emergiaNaoRenovavel += resultado.getEmergiaTotal();
                }
            }
        }

        if (emergiaRenovavel == 0) {
            return Double.POSITIVE_INFINITY;
        }

        return emergiaNaoRenovavel / emergiaRenovavel;
    }

    public double calcularEYR(RedeProcessos rede, Map<Processo, ResultadoEmergia> resultados) {
        double emergiaTotal = 0;
        double emergiaEntrada = 0;

        for (Processo processo : rede.getProcessos()) {
            ResultadoEmergia resultado = resultados.get(processo);
            if (resultado != null) {
                emergiaTotal += resultado.getEmergiaTotal();
                emergiaEntrada += resultado.getEmergiaDireta();
            }
        }

        if (emergiaEntrada == 0) {
            return Double.POSITIVE_INFINITY;
        }

        return emergiaTotal / emergiaEntrada;
    }

    public double calcularESI(double EYR, double ELR) {
        if (ELR == 0) {
            return Double.POSITIVE_INFINITY;
        }
        return EYR / ELR;
    }
}
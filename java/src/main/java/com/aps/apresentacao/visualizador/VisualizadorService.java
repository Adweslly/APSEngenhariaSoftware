package com.aps.apresentacao.visualizador;

import com.aps.domain.model.Processo;
import java.util.List;

public class VisualizadorService implements VisualizadorResultado {

    @Override
    public String gerarGraficoProcessos(List<Processo> processos) {
        StringBuilder sb = new StringBuilder();
        sb.append("Grafico de Processos\n");
        sb.append("==================\n");
        for (Processo processo : processos) {
            sb.append(processo.getNome()).append(": ").append(processo.getTipo()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String gerarDiagramaFluxos() {
        return "Diagrama de Fluxos";
    }
}
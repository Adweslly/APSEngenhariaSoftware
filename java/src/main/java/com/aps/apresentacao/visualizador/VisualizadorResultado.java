package com.aps.apresentacao.visualizador;

import com.aps.domain.model.Processo;
import java.util.List;

public interface VisualizadorResultado {
    String gerarGraficoProcessos(List<Processo> processos);
    String gerarDiagramaFluxos();
}
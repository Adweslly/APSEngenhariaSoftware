package com.aps.apresentacao.relatorio;

import com.aps.domain.model.Processo;
import com.aps.domain.model.ResultadoEmergia;
import com.aps.domain.model.RedeProcessos;
import java.util.Map;

public interface GeradorRelatorio {
    byte[] gerarPDF(RedeProcessos rede, Map<Processo, ResultadoEmergia> resultados);
    String gerarCSV(RedeProcessos rede, Map<Processo, ResultadoEmergia> resultados);
    String gerarJSON(RedeProcessos rede, Map<Processo, ResultadoEmergia> resultados);
}
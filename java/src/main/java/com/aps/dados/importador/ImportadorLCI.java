package com.aps.dados.importador;

import com.aps.domain.model.Processo;
import com.aps.domain.model.Fluxo;
import java.util.List;

public interface ImportadorLCI {
    List<Processo> importarProcessos(String conteudo);
    List<Fluxo> importarFluxos(String conteudo);
    boolean validarFormato(String conteudo);
}
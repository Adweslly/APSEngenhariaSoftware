package com.aps.dados.transformador;

import com.aps.domain.model.Processo;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.RedeProcessos;
import java.util.List;

public interface TransformadorDados {
    RedeProcessos transformar(String dados);
    List<Processo> transformarProcessos(String dados);
    List<Fluxo> transformarFluxos(String dados);
}
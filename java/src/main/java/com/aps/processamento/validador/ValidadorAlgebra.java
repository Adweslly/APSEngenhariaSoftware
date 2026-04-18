package com.aps.processamento.validador;

import com.aps.domain.model.Processo;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.RedeProcessos;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

public class ValidadorAlgebra {

    public boolean validar(RedeProcessos rede) {
        if (rede.getProcessos() == null || rede.getProcessos().isEmpty()) {
            return false;
        }
        return validarFluxos(rede.getFluxos()) && validarContagemDupla(rede);
    }

    private boolean validarFluxos(List<Fluxo> fluxos) {
        if (fluxos == null) {
            return true;
        }
        for (Fluxo fluxo : fluxos) {
            if (fluxo.getOrigem() == null || fluxo.getDestino() == null) {
                return false;
            }
            if (fluxo.getQuantidade() < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean validarContagemDupla(RedeProcessos rede) {
        Set<String> conexoes = new HashSet<>();

        for (Fluxo fluxo : rede.getFluxos()) {
            String chave = fluxo.getOrigem().getId() + "->" + fluxo.getDestino().getId();
            if (conexoes.contains(chave)) {
                return false;
            }
            conexoes.add(chave);
        }

        return true;
    }

    public List<String> getErros(RedeProcessos rede) {
        return new java.util.ArrayList<>();
    }
}
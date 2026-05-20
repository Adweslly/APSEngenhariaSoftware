package com.aps.processamento.algoritmo;

import com.aps.domain.enums.CategoriaRecurso;
import com.aps.domain.enums.TipoFonte;
import com.aps.domain.enums.TipoProcesso;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.Processo;
import com.aps.domain.model.ResultadoEmergia;
import com.aps.domain.model.TipoRecurso;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

class CalculadorEmergiaTest {

    private final CalculadorEmergia calculador = new CalculadorEmergia();

    @Test
    void deveCalcularBiodieselComComponentesConsistentes() {
        Processo natureza = processo("Natureza (Sol/Chuva)", TipoProcesso.RECURSO, "RENOVAVEL");
        Processo minas = processo("Minas de Fosfato/Nitrogenio", TipoProcesso.RECURSO, "NAO_RENOVAVEL");
        Processo mercado = processo("Mercado de Energia/Servicos", TipoProcesso.RECURSO, "COMPRADO");
        Processo fazenda = processo("Fazenda de Soja", TipoProcesso.TRANSFORMACAO, "PROCESSO");
        Processo usina = processo("Usina de Biodiesel", TipoProcesso.TRANSFORMACAO, "PROCESSO");

        conectar(natureza, fazenda, 5.0e10, recurso("Energia Solar/Chuva", 1.0, TipoFonte.RENOVAVEL));
        conectar(minas, fazenda, 500.0, recurso("Insumos NPK", 3.8e12, TipoFonte.NAO_RENOVAVEL));
        conectar(mercado, fazenda, 1200.0, recurso("Combustivel Trator", 1.1e11, TipoFonte.COMPRADO));
        conectar(fazenda, usina, 5000.0, null);
        conectar(mercado, usina, 2500.0, recurso("Eletricidade Industrial", 1.8e11, TipoFonte.COMPRADO));
        conectar(mercado, usina, 300.0, recurso("Mao de Obra Industrial", 2.5e12, TipoFonte.COMPRADO));
        saidaFinal(usina, 4500.0);
        saidaFinal(usina, 500.0);

        Map<Processo, ResultadoEmergia> resultados = calculador.calcular(List.of(natureza, minas, mercado, fazenda, usina));

        ResultadoEmergia resultadoFazenda = resultados.get(fazenda);
        assertThat(resultadoFazenda.getRenovavel()).isCloseTo(5.0e10, withinPercentage(0.000001));
        assertThat(resultadoFazenda.getNaoRenovavel()).isCloseTo(1.9e15, withinPercentage(0.000001));
        assertThat(resultadoFazenda.getComprado()).isCloseTo(1.32e14, withinPercentage(0.000001));
        assertThat(resultadoFazenda.getEmergiaTotal()).isCloseTo(2.03205e15, withinPercentage(0.000001));

        ResultadoEmergia resultadoUsina = resultados.get(usina);
        assertThat(resultadoUsina.getEmergiaTotal()).isCloseTo(3.23205e15, withinPercentage(0.000001));
        assertThat(resultadoUsina.getEmergiaDireta()).isCloseTo(1.2e15, withinPercentage(0.000001));
        assertThat(resultadoUsina.getEmergiaIndireta()).isCloseTo(2.03205e15, withinPercentage(0.000001));
        assertThat(resultadoUsina.getEmergiaDireta()).isLessThan(resultadoUsina.getEmergiaTotal());
        assertThat(resultadoUsina.getEyr()).isCloseTo(3.23205e15 / 1.332e15, withinPercentage(0.000001));
        assertThat(resultadoUsina.getElr()).isCloseTo((1.9e15 + 1.332e15) / 5.0e10, withinPercentage(0.000001));
        assertThat(resultadoUsina.getEsi()).isGreaterThan(0);
    }

    private Processo processo(String nome, TipoProcesso tipo, String categoria) {
        Processo processo = new Processo();
        processo.setNome(nome);
        processo.setTipo(tipo);
        processo.setCategoria(categoria);
        return processo;
    }

    private TipoRecurso recurso(String nome, double transformidade, TipoFonte tipoFonte) {
        TipoRecurso recurso = new TipoRecurso();
        recurso.setNome(nome);
        recurso.setCategoria(CategoriaRecurso.ENERGIA);
        recurso.setTransformidade(transformidade);
        recurso.setTipoFonte(tipoFonte);
        return recurso;
    }

    private void conectar(Processo origem, Processo destino, double quantidade, TipoRecurso tipoRecurso) {
        Fluxo fluxo = new Fluxo();
        fluxo.setQuantidade(quantidade);
        fluxo.setOrigem(origem);
        fluxo.setDestino(destino);
        fluxo.setTipoRecurso(tipoRecurso);
        fluxo.setTransformidade(tipoRecurso != null ? tipoRecurso.getTransformidade() : 0);
        origem.addOutput(fluxo);
        destino.addInput(fluxo);
    }

    private void saidaFinal(Processo origem, double quantidade) {
        Fluxo fluxo = new Fluxo();
        fluxo.setQuantidade(quantidade);
        fluxo.setOrigem(origem);
        origem.addOutput(fluxo);
    }
}

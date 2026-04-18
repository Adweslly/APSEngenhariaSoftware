package com.aps.web.controller;

import com.aps.domain.model.RedeProcessos;
import com.aps.domain.model.Processo;
import com.aps.domain.model.ResultadoEmergia;
import com.aps.dados.repositorio.RepositorioProcesso;
import com.aps.processamento.algoritmo.CalculadorEmergia;
import com.aps.processamento.validador.ValidadorAlgebra;
import com.aps.processamento.indicadores.CalculadorIndicadores;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/redes")
public class RedeProcessosController {

    private final RepositorioProcesso repositorio;
    private final CalculadorEmergia calculador;
    private final ValidadorAlgebra validador;
    private final CalculadorIndicadores indicadores;

    public RedeProcessosController() {
        this.repositorio = new RepositorioProcesso();
        this.calculador = new CalculadorEmergia();
        this.validador = new ValidadorAlgebra();
        this.indicadores = new CalculadorIndicadores();
    }

    @GetMapping
    public List<RedeProcessos> getAll() {
        return repositorio.findAllRedes();
    }

    @GetMapping("/{id}")
    public RedeProcessos getById(@PathVariable Long id) {
        return repositorio.findRedeById(id).orElse(null);
    }

    @PostMapping
    public RedeProcessos create(@RequestBody RedeProcessos rede) {
        return repositorio.save(rede);
    }

    @PostMapping("/{id}/calcular")
    public Map<Processo, ResultadoEmergia> calcular(@PathVariable Long id) {
        return repositorio.findRedeById(id)
            .filter(validador::validar)
            .map(calculador::calcular)
            .orElse(null);
    }

    @PostMapping("/{id}/calcular-elr")
    public Map<String, Double> calcularELR(@PathVariable Long id) {
        return repositorio.findRedeById(id)
            .filter(validador::validar)
            .map(rede -> {
                Map<Processo, ResultadoEmergia> resultados = calculador.calcular(rede);
                double elr = indicadores.calcularELR(rede, resultados);
                return Map.of("elr", elr);
            })
            .orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repositorio.deleteRede(id);
    }
}
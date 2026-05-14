package com.aps.web.controller;

import com.aps.domain.model.RedeProcessos;
import com.aps.dados.repositorio.RedeProcessosRepository;
import com.aps.service.EmergyCalculationService;
import com.aps.web.dto.ResultadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/redes")
public class RedeProcessosController {

    @Autowired
    private RedeProcessosRepository repository;

    @Autowired
    private EmergyCalculationService calculationService;

    @GetMapping
    public List<RedeProcessos> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RedeProcessos> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RedeProcessos create(@RequestBody RedeProcessos rede) {
        return repository.save(rede);
    }

    @PostMapping("/{id}/calcular")
    public ResponseEntity<List<ResultadoDTO>> calcular(@PathVariable Long id) {
        // In a real scenario, we would filter by the specific network ID.
        // For the MVP and since calculationService currently calculates all,
        // we'll return all results.
        return ResponseEntity.ok(calculationService.calcularTudo());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

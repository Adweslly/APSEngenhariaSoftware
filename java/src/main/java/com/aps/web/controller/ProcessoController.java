package com.aps.web.controller;

import com.aps.service.ProcessoService;
import com.aps.web.dto.ProcessoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processos")
@Tag(name = "Processos", description = "Gerenciamento de processos do sistema")
public class ProcessoController {

    @Autowired
    private ProcessoService service;

    @GetMapping
    @Operation(summary = "Listar todos os processos")
    public List<ProcessoDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter processo por ID")
    public ResponseEntity<ProcessoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar um novo processo")
    public ResponseEntity<ProcessoDTO> create(@RequestBody ProcessoDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um processo existente")
    public ResponseEntity<ProcessoDTO> update(@PathVariable Long id, @RequestBody ProcessoDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um processo")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

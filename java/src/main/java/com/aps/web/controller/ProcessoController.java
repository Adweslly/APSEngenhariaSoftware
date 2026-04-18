package com.aps.web.controller;

import com.aps.domain.model.Processo;
import com.aps.dados.repositorio.RepositorioProcesso;
import com.aps.web.dto.ProcessoDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/processos")
public class ProcessoController {

    private final RepositorioProcesso repositorio;

    public ProcessoController() {
        this.repositorio = new RepositorioProcesso();
    }

    @GetMapping
    public List<ProcessoDTO> getAll() {
        return repositorio.findAll().stream()
            .map(p -> new ProcessoDTO(p.getId(), p.getNome(), p.getDescricao(), p.getTipo(), p.getCategoria()))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProcessoDTO getById(@PathVariable Long id) {
        return repositorio.findById(id)
            .map(p -> new ProcessoDTO(p.getId(), p.getNome(), p.getDescricao(), p.getTipo(), p.getCategoria()))
            .orElse(null);
    }

    @PostMapping
    public ProcessoDTO create(@RequestBody ProcessoDTO dto) {
        Processo processo = new Processo();
        processo.setNome(dto.getNome());
        processo.setDescricao(dto.getDescricao());
        processo.setTipo(dto.getTipo());
        processo.setCategoria(dto.getCategoria());
        repositorio.save(processo);
        return dto;
    }

    @PutMapping("/{id}")
    public ProcessoDTO update(@PathVariable Long id, @RequestBody ProcessoDTO dto) {
        return repositorio.findById(id).map(processo -> {
            processo.setNome(dto.getNome());
            processo.setDescricao(dto.getDescricao());
            processo.setTipo(dto.getTipo());
            processo.setCategoria(dto.getCategoria());
            repositorio.save(processo);
            return new ProcessoDTO(processo.getId(), processo.getNome(), processo.getDescricao(),
                processo.getTipo(), processo.getCategoria());
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repositorio.delete(id);
    }
}
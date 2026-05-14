package com.aps.service;

import com.aps.dados.repositorio.ProcessoRepository;
import com.aps.domain.model.Processo;
import com.aps.web.dto.ProcessoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository repository;

    @Transactional(readOnly = true)
    public List<ProcessoDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProcessoDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado"));
    }

    @Transactional
    public ProcessoDTO save(ProcessoDTO dto) {
        Processo entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private ProcessoDTO toDTO(Processo entity) {
        return new ProcessoDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getTipo(),
                entity.getCategoria(),
                entity.isCoproduto()
        );
    }

    private Processo toEntity(ProcessoDTO dto) {
        Processo entity = new Processo();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setTipo(dto.getTipo());
        entity.setCategoria(dto.getCategoria());
        entity.setCoproduto(dto.isCoproduto());
        return entity;
    }
}

package com.aps.service;

import com.aps.dados.repositorio.TipoRecursoRepository;
import com.aps.domain.model.TipoRecurso;
import com.aps.web.dto.TipoRecursoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoRecursoService {

    @Autowired
    private TipoRecursoRepository repository;

    @Transactional(readOnly = true)
    public List<TipoRecursoDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoRecursoDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Tipo de Recurso não encontrado"));
    }

    @Transactional
    public TipoRecursoDTO save(TipoRecursoDTO dto) {
        TipoRecurso entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private TipoRecursoDTO toDTO(TipoRecurso entity) {
        return new TipoRecursoDTO(
                entity.getId(),
                entity.getNome(),
                entity.getUnidade(),
                entity.getCategoria(),
                entity.getTipoFonte(),
                entity.getTransformidade()
        );
    }

    private TipoRecurso toEntity(TipoRecursoDTO dto) {
        TipoRecurso entity = new TipoRecurso();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setUnidade(dto.getUnidade());
        entity.setCategoria(dto.getCategoria());
        entity.setTipoFonte(dto.getTipoFonte());
        entity.setTransformidade(dto.getTransformidade());
        return entity;
    }
}

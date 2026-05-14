package com.aps.service;

import com.aps.dados.repositorio.FluxoRepository;
import com.aps.dados.repositorio.ProcessoRepository;
import com.aps.dados.repositorio.TipoRecursoRepository;
import com.aps.domain.model.Fluxo;
import com.aps.domain.model.Processo;
import com.aps.domain.model.TipoRecurso;
import com.aps.web.dto.FluxoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FluxoService {

    @Autowired
    private FluxoRepository repository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private TipoRecursoRepository tipoRecursoRepository;

    @Transactional(readOnly = true)
    public List<FluxoDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FluxoDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Fluxo não encontrado"));
    }

    @Transactional
    public FluxoDTO save(FluxoDTO dto) {
        Fluxo entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private FluxoDTO toDTO(Fluxo entity) {
        return new FluxoDTO(
                entity.getId(),
                entity.getQuantidade(),
                entity.getTipoRecurso() != null ? entity.getTipoRecurso().getId() : null,
                entity.getOrigem() != null ? entity.getOrigem().getId() : null,
                entity.getDestino() != null ? entity.getDestino().getId() : null
        );
    }

    private Fluxo toEntity(FluxoDTO dto) {
        Fluxo entity = new Fluxo();
        entity.setId(dto.getId());
        entity.setQuantidade(dto.getQuantidade());
        
        if (dto.getTipoRecursoId() != null) {
            TipoRecurso tr = tipoRecursoRepository.findById(dto.getTipoRecursoId())
                    .orElseThrow(() -> new RuntimeException("TipoRecurso não encontrado"));
            entity.setTipoRecurso(tr);
            entity.setTransformidade(tr.getTransformidade());
        }
        
        if (dto.getOrigemId() != null) {
            Processo origem = processoRepository.findById(dto.getOrigemId())
                    .orElseThrow(() -> new RuntimeException("Processo origem não encontrado"));
            entity.setOrigem(origem);
        }
        
        if (dto.getDestinoId() != null) {
            Processo destino = processoRepository.findById(dto.getDestinoId())
                    .orElseThrow(() -> new RuntimeException("Processo destino não encontrado"));
            entity.setDestino(destino);
        }
        
        entity.calcularCustoEmergia();
        return entity;
    }
}

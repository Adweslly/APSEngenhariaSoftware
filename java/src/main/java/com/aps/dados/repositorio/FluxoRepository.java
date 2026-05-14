package com.aps.dados.repositorio;

import com.aps.domain.model.Fluxo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FluxoRepository extends JpaRepository<Fluxo, Long> {
}

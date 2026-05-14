package com.aps.dados.repositorio;

import com.aps.domain.model.RedeProcessos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedeProcessosRepository extends JpaRepository<RedeProcessos, Long> {
}

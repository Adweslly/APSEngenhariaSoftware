package com.aps.dados.repositorio;

import com.aps.domain.model.ResultadoEmergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultadoEmergiaRepository extends JpaRepository<ResultadoEmergia, Long> {
}

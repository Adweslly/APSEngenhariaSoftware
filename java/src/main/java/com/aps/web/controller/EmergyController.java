package com.aps.web.controller;

import com.aps.service.EmergyCalculationService;
import com.aps.web.dto.ResultadoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/emergy")
@Tag(name = "Emergia", description = "Motor de cálculo e indicadores")
public class EmergyController {

    @Autowired
    private EmergyCalculationService calculationService;

    @GetMapping("/calculate")
    @Operation(summary = "Executar cálculo de emergia para todos os processos", description = "Aciona o motor de cálculo iterativo e retorna os resultados e indicadores.")
    public ResponseEntity<List<ResultadoDTO>> calculate() {
        return ResponseEntity.ok(calculationService.calcularTudo());
    }
}

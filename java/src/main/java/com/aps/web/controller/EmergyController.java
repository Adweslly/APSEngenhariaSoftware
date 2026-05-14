package com.aps.web.controller;

import com.aps.service.EmergyCalculationService;
import com.aps.web.dto.ResultadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/emergy")
public class EmergyController {

    @Autowired
    private EmergyCalculationService calculationService;

    @GetMapping("/calculate")
    public ResponseEntity<List<ResultadoDTO>> calculate() {
        return ResponseEntity.ok(calculationService.calcularTudo());
    }
}

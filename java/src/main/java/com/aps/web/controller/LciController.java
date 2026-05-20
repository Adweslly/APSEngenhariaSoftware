package com.aps.web.controller;

import com.aps.service.LciImportService;
import com.aps.web.dto.ImportLCIDTO;
import com.aps.web.dto.ResultadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lci")
public class LciController {

    @Autowired
    private LciImportService importService;

    @PostMapping("/import")
    public ResponseEntity<List<ResultadoDTO>> importar(@RequestBody ImportLCIDTO dto) {
        return ResponseEntity.ok(importService.importar(dto));
    }
}

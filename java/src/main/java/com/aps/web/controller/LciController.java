package com.aps.web.controller;

import com.aps.service.LciImportService;
import com.aps.web.dto.ImportLCIDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lci")
public class LciController {

    @Autowired
    private LciImportService importService;

    @PostMapping("/import")
    public ResponseEntity<String> importar(@RequestBody ImportLCIDTO dto) {
        importService.importar(dto);
        return ResponseEntity.ok("Importação concluída com sucesso");
    }
}

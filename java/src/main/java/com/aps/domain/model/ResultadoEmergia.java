package com.aps.domain.model;

import java.time.LocalDateTime;

public class ResultadoEmergia {
    private Long id;
    private Processo processo;
    private double emergiaTotal;
    private double emergiaDireta;
    private double emergiaIndireta;
    private double transformidade;
    private LocalDateTime dataCalculo;

    public ResultadoEmergia() {}

    public ResultadoEmergia(Processo processo) {
        this.processo = processo;
        this.dataCalculo = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Processo getProcesso() { return processo; }
    public void setProcesso(Processo processo) { this.processo = processo; }
    public double getEmergiaTotal() { return emergiaTotal; }
    public void setEmergiaTotal(double emergiaTotal) { this.emergiaTotal = emergiaTotal; }
    public double getEmergiaDireta() { return emergiaDireta; }
    public void setEmergiaDireta(double emergiaDireta) { this.emergiaDireta = emergiaDireta; }
    public double getEmergiaIndireta() { return emergiaIndireta; }
    public void setEmergiaIndireta(double emergiaIndireta) { this.emergiaIndireta = emergiaIndireta; }
    public double getTransformidade() { return transformidade; }
    public void setTransformidade(double transformidade) { this.transformidade = transformidade; }
    public LocalDateTime getDataCalculo() { return dataCalculo; }
    public void setDataCalculo(LocalDateTime dataCalculo) { this.dataCalculo = dataCalculo; }
}
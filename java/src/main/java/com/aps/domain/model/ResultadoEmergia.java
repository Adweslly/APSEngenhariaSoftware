package com.aps.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_emergia")
public class ResultadoEmergia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "processo_id")
    private Processo processo;
    
    private double emergiaTotal;
    private double emergiaDireta;
    private double emergiaIndireta;
    private double transformidade;
    private double elr;
    private double eyr;
    private double esi;
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
    public double getElr() { return elr; }
    public void setElr(double elr) { this.elr = elr; }
    public double getEyr() { return eyr; }
    public void setEyr(double eyr) { this.eyr = eyr; }
    public double getEsi() { return esi; }
    public void setEsi(double esi) { this.esi = esi; }
    public LocalDateTime getDataCalculo() { return dataCalculo; }
    public void setDataCalculo(LocalDateTime dataCalculo) { this.dataCalculo = dataCalculo; }
}
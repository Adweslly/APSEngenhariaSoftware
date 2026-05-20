package com.aps.web.dto;

import java.time.LocalDateTime;

public class ResultadoDTO {
    private Long id;
    private Long processoId;
    private String processoNome;
    private double emergiaTotal;
    private double emergiaDireta;
    private double emergiaIndireta;
    private double transformidade;
    private double renovavel;
    private double naoRenovavel;
    private double comprado;
    private boolean processoFinal;
    private LocalDateTime dataCalculo;
    private Double elr;
    private Double eyr;
    private Double esi;

    public ResultadoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProcessoId() { return processoId; }
    public void setProcessoId(Long processoId) { this.processoId = processoId; }
    public String getProcessoNome() { return processoNome; }
    public void setProcessoNome(String processoNome) { this.processoNome = processoNome; }
    public double getEmergiaTotal() { return emergiaTotal; }
    public void setEmergiaTotal(double emergiaTotal) { this.emergiaTotal = emergiaTotal; }
    public double getEmergiaDireta() { return emergiaDireta; }
    public void setEmergiaDireta(double emergiaDireta) { this.emergiaDireta = emergiaDireta; }
    public double getEmergiaIndireta() { return emergiaIndireta; }
    public void setEmergiaIndireta(double emergiaIndireta) { this.emergiaIndireta = emergiaIndireta; }
    public double getTransformidade() { return transformidade; }
    public void setTransformidade(double transformidade) { this.transformidade = transformidade; }
    public double getRenovavel() { return renovavel; }
    public void setRenovavel(double renovavel) { this.renovavel = renovavel; }
    public double getNaoRenovavel() { return naoRenovavel; }
    public void setNaoRenovavel(double naoRenovavel) { this.naoRenovavel = naoRenovavel; }
    public double getComprado() { return comprado; }
    public void setComprado(double comprado) { this.comprado = comprado; }
    public boolean isProcessoFinal() { return processoFinal; }
    public void setProcessoFinal(boolean processoFinal) { this.processoFinal = processoFinal; }
    public LocalDateTime getDataCalculo() { return dataCalculo; }
    public void setDataCalculo(LocalDateTime dataCalculo) { this.dataCalculo = dataCalculo; }
    public Double getElr() { return elr; }
    public void setElr(Double elr) { this.elr = elr; }
    public Double getEyr() { return eyr; }
    public void setEyr(Double eyr) { this.eyr = eyr; }
    public Double getEsi() { return esi; }
    public void setEsi(Double esi) { this.esi = esi; }
}

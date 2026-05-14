class ResultadoModel {
  final int? id;
  final int processoId;
  final String processoNome;
  final double emergiaTotal;
  final double emergiaDireta;
  final double emergiaIndireta;
  final double transformidade;
  final DateTime? dataCalculo;
  final double? elr;
  final double? eyr;
  final double? esi;

  ResultadoModel({
    this.id,
    required this.processoId,
    required this.processoNome,
    required this.emergiaTotal,
    required this.emergiaDireta,
    required this.emergiaIndireta,
    required this.transformidade,
    this.dataCalculo,
    this.elr,
    this.eyr,
    this.esi,
  });

  factory ResultadoModel.fromJson(Map<String, dynamic> json) {
    return ResultadoModel(
      id: json['id'],
      processoId: json['processoId'],
      processoNome: json['processoNome'],
      emergiaTotal: (json['emergiaTotal'] as num).toDouble(),
      emergiaDireta: (json['emergiaDireta'] as num).toDouble(),
      emergiaIndireta: (json['emergiaIndireta'] as num).toDouble(),
      transformidade: (json['transformidade'] as num).toDouble(),
      dataCalculo: json['dataCalculo'] != null 
          ? DateTime.parse(json['dataCalculo']) 
          : null,
      elr: json['elr'] != null ? (json['elr'] as num).toDouble() : null,
      eyr: json['eyr'] != null ? (json['eyr'] as num).toDouble() : null,
      esi: json['esi'] != null ? (json['esi'] as num).toDouble() : null,
    );
  }
}

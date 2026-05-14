enum TipoProcesso {
  PRODUTOR,
  CONSUMIDOR,
  ARMAZENAMENTO,
  RECURSO,
  TRANSFORMACAO,
}

class ProcessoModel {
  final int? id;
  final String nome;
  final String descricao;
  final TipoProcesso tipo;
  final String categoria;
  final bool coproduto;

  ProcessoModel({
    this.id,
    required this.nome,
    required this.descricao,
    required this.tipo,
    required this.categoria,
    required this.coproduto,
  });

  factory ProcessoModel.fromJson(Map<String, dynamic> json) {
    return ProcessoModel(
      id: json['id'],
      nome: json['nome'],
      descricao: json['descricao'],
      tipo: TipoProcesso.values.firstWhere(
        (e) => e.name == json['tipo'],
        orElse: () => TipoProcesso.CONSUMIDOR,
      ),
      categoria: json['categoria'],
      coproduto: json['coproduto'] ?? false,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome,
      'descricao': descricao,
      'tipo': tipo.name,
      'categoria': categoria,
      'coproduto': coproduto,
    };
  }
}

import 'dart:convert';
import '../../core/api/api_base.dart';

class FluxoModel {
  final int? id;
  final double quantidade;
  final int? tipoRecursoId;
  final int? origemId;
  final int? destinoId;

  FluxoModel({
    this.id,
    required this.quantidade,
    this.tipoRecursoId,
    this.origemId,
    this.destinoId,
  });

  factory FluxoModel.fromJson(Map<String, dynamic> json) {
    return FluxoModel(
      id: json['id'],
      quantidade: (json['quantidade'] as num).toDouble(),
      tipoRecursoId: json['tipoRecursoId'],
      origemId: json['origemId'],
      destinoId: json['destinoId'],
    );
  }
}

class FluxoService {
  Future<List<FluxoModel>> getAll() async {
    final response = await ApiBase.get('/fluxos');
    if (response.statusCode == 200) {
      List<dynamic> data = jsonDecode(response.body);
      return data.map((item) => FluxoModel.fromJson(item)).toList();
    } else {
      throw Exception('Falha ao carregar fluxos');
    }
  }
}

import 'dart:convert';
import '../models/resultado_model.dart';
import '../../core/api/api_base.dart';

class EmergyService {
  Future<List<ResultadoModel>> calcular() async {
    final response = await ApiBase.get('/emergy/calculate');
    if (response.statusCode == 200) {
      List<dynamic> data = jsonDecode(response.body);
      return data.map((item) => ResultadoModel.fromJson(item)).toList();
    } else {
      throw Exception('Falha ao calcular emergia');
    }
  }

  Future<void> importarLCI(String conteudo, String formato) async {
    final response = await ApiBase.post('/lci/import', {
      'conteudo': conteudo,
      'formato': formato,
    });
    if (response.statusCode != 200) {
      String errorMessage = 'Falha ao importar LCI';
      try {
        errorMessage = response.body;
      } catch (_) {}
      throw Exception(errorMessage);
    }
  }
}

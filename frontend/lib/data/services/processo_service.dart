import 'dart:convert';
import '../models/processo_model.dart';
import '../../core/api/api_base.dart';

class ProcessoService {
  Future<List<ProcessoModel>> getAll() async {
    final response = await ApiBase.get('/processos');
    if (response.statusCode == 200) {
      List<dynamic> data = jsonDecode(response.body);
      return data.map((item) => ProcessoModel.fromJson(item)).toList();
    } else {
      throw Exception('Falha ao carregar processos');
    }
  }

  Future<ProcessoModel> save(ProcessoModel processo) async {
    final response = processo.id == null
        ? await ApiBase.post('/processos', processo.toJson())
        : await ApiBase.put('/processos/${processo.id}', processo.toJson());

    if (response.statusCode == 200 || response.statusCode == 201) {
      return ProcessoModel.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Falha ao salvar processo');
    }
  }

  Future<void> delete(int id) async {
    final response = await ApiBase.delete('/processos/$id');
    if (response.statusCode != 200 && response.statusCode != 204) {
      throw Exception('Falha ao excluir processo');
    }
  }
}

import 'package:flutter/material.dart';
import '../../data/models/resultado_model.dart';
import '../../data/services/emergy_service.dart';

class EmergyProvider with ChangeNotifier {
  final EmergyService _service = EmergyService();
  List<ResultadoModel> _resultados = [];
  bool _isLoading = false;

  List<ResultadoModel> get resultados => _resultados;
  bool get isLoading => _isLoading;

  Future<void> calcular() async {
    _isLoading = true;
    notifyListeners();
    try {
      _resultados = await _service.calcular();
    } catch (e) {
      debugPrint(e.toString());
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> importarLCI(String conteudo, String formato) async {
    _isLoading = true;
    notifyListeners();
    try {
      _resultados = await _service.importarLCI(conteudo, formato);
    } catch (e) {
      debugPrint(e.toString());
      rethrow;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}

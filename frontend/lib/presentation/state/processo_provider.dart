import 'package:flutter/material.dart';
import '../../data/models/processo_model.dart';
import '../../data/services/processo_service.dart';

class ProcessoProvider with ChangeNotifier {
  final ProcessoService _service = ProcessoService();
  List<ProcessoModel> _processos = [];
  bool _isLoading = false;

  List<ProcessoModel> get processos => _processos;
  bool get isLoading => _isLoading;

  Future<void> fetchProcessos() async {
    _isLoading = true;
    notifyListeners();
    try {
      _processos = await _service.getAll();
    } catch (e) {
      debugPrint(e.toString());
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> salvarProcesso(ProcessoModel processo) async {
    try {
      await _service.save(processo);
      await fetchProcessos();
    } catch (e) {
      debugPrint(e.toString());
      rethrow;
    }
  }

  Future<void> excluirProcesso(int id) async {
    try {
      await _service.delete(id);
      await fetchProcessos();
    } catch (e) {
      debugPrint(e.toString());
      rethrow;
    }
  }
}

import 'package:flutter/material.dart';
import 'package:graphview/graphview.dart';
import 'package:provider/provider.dart';
import '../../../data/models/processo_model.dart';
import '../../../data/services/fluxo_service.dart';
import '../../state/processo_provider.dart';

class VisualizacaoScreen extends StatefulWidget {
  const VisualizacaoScreen({super.key});

  @override
  State<VisualizacaoScreen> createState() => _VisualizacaoScreenState();
}

class _VisualizacaoScreenState extends State<VisualizacaoScreen> {
  final Graph graph = Graph();
  final SugiyamaConfiguration builder = SugiyamaConfiguration();
  final FluxoService _fluxoService = FluxoService();
  final TransformationController _transformationController = TransformationController();

  bool _isLoading = false;
  String _lastProcessSignature = '';
  Map<int, ProcessoModel> _processosPorId = {};

  @override
  void initState() {
    super.initState();
    builder
      ..nodeSeparation = 80
      ..levelSeparation = 120
      ..orientation = SugiyamaConfiguration.ORIENTATION_TOP_BOTTOM
      ..bendPointShape = CurvedBendPointShape(curveLength: 20);
    _loadGraphData();
  }

  @override
  void dispose() {
    _transformationController.dispose();
    super.dispose();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final processos = context.watch<ProcessoProvider>().processos;
    final signature = processos.map((p) => p.id).join(',');
    if (signature != _lastProcessSignature) {
      _lastProcessSignature = signature;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        if (mounted) {
          _loadGraphData();
        }
      });
    }
  }

  Future<void> _loadGraphData() async {
    if (!mounted) return;
    setState(() => _isLoading = true);

    try {
      final processos = context.read<ProcessoProvider>().processos;
      final fluxos = await _fluxoService.getAll();
      final Map<int, Node> nodesPorId = {};

      graph.nodes.clear();
      graph.edges.clear();
      _processosPorId = {
        for (final processo in processos)
          if (processo.id != null) processo.id!: processo,
      };

      for (final processo in processos) {
        final id = processo.id;
        if (id == null) continue;
        final node = Node.Id(id);
        nodesPorId[id] = node;
        graph.addNode(node);
      }

      for (final fluxo in fluxos) {
        final origemId = fluxo.origemId;
        final destinoId = fluxo.destinoId;
        if (origemId == null || destinoId == null) continue;

        final origem = nodesPorId[origemId];
        final destino = nodesPorId[destinoId];
        if (origem == null || destino == null) continue;

        graph.addEdge(origem, destino);
      }

      _resetViewport();
    } catch (e) {
      debugPrint('Erro ao carregar fluxos para o grafo: $e');
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  void _resetViewport() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (!mounted) return;
      _transformationController.value = Matrix4.identity()
        ..translate(48.0, 32.0)
        ..scale(0.95);
    });
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      );
    }

    final processos = context.watch<ProcessoProvider>().processos;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Rede de Processos'),
        actions: [
          IconButton(
            icon: const Icon(Icons.center_focus_strong),
            onPressed: _resetViewport,
            tooltip: 'Centralizar',
          ),
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _loadGraphData,
            tooltip: 'Atualizar',
          ),
        ],
      ),
      body: processos.isEmpty || graph.nodes.isEmpty
          ? const Center(child: Text('Nenhum fluxo encontrado para visualizar. Importe um LCI e calcule.'))
          : InteractiveViewer(
              transformationController: _transformationController,
              constrained: false,
              boundaryMargin: const EdgeInsets.all(600),
              minScale: 0.2,
              maxScale: 3.5,
              child: Center(
                child: Padding(
                  padding: const EdgeInsets.all(64),
                  child: GraphView(
                    graph: graph,
                    algorithm: SugiyamaAlgorithm(builder),
                    paint: Paint()
                      ..color = Colors.green
                      ..strokeWidth = 1.4
                      ..style = PaintingStyle.stroke,
                    builder: (Node node) {
                      final id = node.key!.value as int;
                      return _processoNodeWidget(_processosPorId[id], id);
                    },
                  ),
                ),
              ),
            ),
    );
  }

  Widget _processoNodeWidget(ProcessoModel? processo, int id) {
    final isRecurso = processo?.tipo == TipoProcesso.RECURSO;
    final borderColor = isRecurso ? Colors.green : Colors.blue;
    final backgroundColor = isRecurso ? Colors.green.shade50 : Colors.blue.shade50;
    final label = processo?.nome ?? 'No $id';
    final subtitle = processo?.tipo.name ?? 'PROCESSO';

    return ConstrainedBox(
      constraints: const BoxConstraints(
        minWidth: 140,
        maxWidth: 220,
      ),
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(6),
          boxShadow: [
            BoxShadow(
              color: borderColor.withValues(alpha: 0.18),
              spreadRadius: 1,
              blurRadius: 4,
              offset: const Offset(0, 2),
            ),
          ],
          color: backgroundColor,
          border: Border.all(color: borderColor),
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              label,
              textAlign: TextAlign.center,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
            ),
            const SizedBox(height: 4),
            Text(
              subtitle,
              textAlign: TextAlign.center,
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
              style: TextStyle(fontSize: 10, color: Colors.grey.shade700),
            ),
          ],
        ),
      ),
    );
  }
}

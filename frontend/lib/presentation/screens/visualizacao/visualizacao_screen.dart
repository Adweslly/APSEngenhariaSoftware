import 'package:flutter/material.dart';
import 'package:graphview/graphview.dart';
import 'package:provider/provider.dart';
import '../../state/processo_provider.dart';

class VisualizacaoScreen extends StatefulWidget {
  const VisualizacaoScreen({super.key});

  @override
  State<VisualizacaoScreen> createState() => _VisualizacaoScreenState();
}

class _VisualizacaoScreenState extends State<VisualizacaoScreen> {
  final Graph graph = Graph()..isTree = true;
  final BuchheimWalkerConfiguration builder = BuchheimWalkerConfiguration();

  @override
  void initState() {
    super.initState();
    builder
      ..siblingSeparation = (100)
      ..levelSeparation = (150)
      ..subtreeSeparation = (150)
      ..orientation = (BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM);
  }

  @override
  Widget build(BuildContext context) {
    final processos = context.watch<ProcessoProvider>().processos;

    // Construindo o grafo a partir dos processos (Simplificação: conectando todos a um nó raiz fictício para demonstração)
    // Em uma versão completa, usaríamos as conexões reais de Fluxo vindas do backend.
    graph.nodes.clear();
    final root = Node.Id('Sistema');
    graph.addNode(root);

    for (var p in processos) {
      final node = Node.Id(p.nome);
      graph.addEdge(root, node);
    }

    return Scaffold(
      appBar: AppBar(title: const Text('Rede de Processos')),
      body: processos.isEmpty
          ? const Center(child: Text('Nenhum dado para visualizar.'))
          : InteractiveViewer(
              constrained: false,
              boundaryMargin: const EdgeInsets.all(100),
              minScale: 0.01,
              maxScale: 5.6,
              child: GraphView(
                graph: graph,
                algorithm: BuchheimWalkerAlgorithm(builder, TreeEdgeRenderer(builder)),
                paint: Paint()
                  ..color = Colors.green
                  ..strokeWidth = 1
                  ..style = PaintingStyle.stroke,
                builder: (Node node) {
                  var value = node.key!.value as String;
                  return _rectangleWidget(value);
                },
              ),
            ),
    );
  }

  Widget _rectangleWidget(String value) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(4),
        boxShadow: [
          BoxShadow(color: Colors.blue.withOpacity(0.2), spreadRadius: 1, blurRadius: 2),
        ],
        color: Colors.white,
        border: Border.all(color: Colors.blue),
      ),
      child: Text(value, style: const TextStyle(fontWeight: FontWeight.bold)),
    );
  }
}

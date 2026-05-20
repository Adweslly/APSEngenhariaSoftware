import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../data/models/resultado_model.dart';
import '../../state/emergy_provider.dart';

class ResultadosScreen extends StatefulWidget {
  const ResultadosScreen({super.key});

  @override
  State<ResultadosScreen> createState() => _ResultadosScreenState();
}

class _ResultadosScreenState extends State<ResultadosScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<EmergyProvider>().calcular();
    });
  }

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<EmergyProvider>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Resultados e Indicadores'),
        actions: [
          IconButton(
            icon: const Icon(Icons.play_arrow, color: Colors.green),
            onPressed: provider.calcular,
            tooltip: 'Calcular Emergia',
          ),
        ],
      ),
      body: provider.isLoading
          ? const Center(child: CircularProgressIndicator())
          : provider.resultados.isEmpty
              ? const Center(child: Text('Nenhum resultado disponivel. Execute o calculo.'))
              : SingleChildScrollView(
                  padding: const EdgeInsets.all(24),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildIndicatorsCard(provider.resultados),
                      const SizedBox(height: 32),
                      Text('Detalhamento por Processo', style: Theme.of(context).textTheme.titleLarge),
                      const SizedBox(height: 16),
                      _buildResultsTable(provider.resultados),
                    ],
                  ),
                ),
    );
  }

  Widget _buildIndicatorsCard(List<ResultadoModel> resultados) {
    final res = _resultadoPrincipal(resultados);

    return Card(
      elevation: 4,
      child: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          children: [
            Text('Indicadores de Sustentabilidade - ${res.processoNome}', style: Theme.of(context).textTheme.titleMedium),
            const SizedBox(height: 24),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _indicatorBox('EYR', _formatDecimal(res.eyr), 'Emergy Yield Ratio'),
                _indicatorBox('ELR', _formatDecimal(res.elr), 'Env. Loading Ratio'),
                _indicatorBox('ESI', _formatDecimal(res.esi), 'Sustainability Index'),
              ],
            ),
            const SizedBox(height: 24),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _indicatorBox('R', res.renovavel.toStringAsExponential(2), 'Renovavel'),
                _indicatorBox('N', res.naoRenovavel.toStringAsExponential(2), 'Nao renovavel'),
                _indicatorBox('F', res.comprado.toStringAsExponential(2), 'Comprado'),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _indicatorBox(String label, String value, String desc) {
    return Column(
      children: [
        Text(label, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
        const SizedBox(height: 8),
        Text(value, style: const TextStyle(fontSize: 28, color: Colors.green, fontWeight: FontWeight.w900)),
        const SizedBox(height: 4),
        Text(desc, style: const TextStyle(fontSize: 10, color: Colors.grey)),
      ],
    );
  }

  Widget _buildResultsTable(List<ResultadoModel> resultados) {
    return Table(
      border: TableBorder.all(color: Colors.grey.shade300),
      columnWidths: const {
        0: FlexColumnWidth(2),
        1: FlexColumnWidth(1.2),
        2: FlexColumnWidth(1.2),
        3: FlexColumnWidth(1.2),
        4: FlexColumnWidth(1.2),
        5: FlexColumnWidth(1.2),
        6: FlexColumnWidth(1.2),
      },
      children: [
        TableRow(
          decoration: BoxDecoration(color: Colors.grey.shade100),
          children: const [
            Padding(padding: EdgeInsets.all(8), child: Text('Processo', style: TextStyle(fontWeight: FontWeight.bold))),
            Padding(padding: EdgeInsets.all(8), child: Text('Total (sej)', style: TextStyle(fontWeight: FontWeight.bold))),
            Padding(padding: EdgeInsets.all(8), child: Text('Direta', style: TextStyle(fontWeight: FontWeight.bold))),
            Padding(padding: EdgeInsets.all(8), child: Text('Indireta', style: TextStyle(fontWeight: FontWeight.bold))),
            Padding(padding: EdgeInsets.all(8), child: Text('R', style: TextStyle(fontWeight: FontWeight.bold))),
            Padding(padding: EdgeInsets.all(8), child: Text('N', style: TextStyle(fontWeight: FontWeight.bold))),
            Padding(padding: EdgeInsets.all(8), child: Text('F', style: TextStyle(fontWeight: FontWeight.bold))),
          ],
        ),
        ...resultados.map((r) => TableRow(
          decoration: r.processoFinal ? BoxDecoration(color: Colors.green.withValues(alpha: 0.08)) : null,
          children: [
            Padding(padding: const EdgeInsets.all(8), child: Text(r.processoFinal ? '${r.processoNome} (final)' : r.processoNome)),
            Padding(padding: const EdgeInsets.all(8), child: Text(r.emergiaTotal.toStringAsExponential(2))),
            Padding(padding: const EdgeInsets.all(8), child: Text(r.emergiaDireta.toStringAsExponential(2))),
            Padding(padding: const EdgeInsets.all(8), child: Text(r.emergiaIndireta.toStringAsExponential(2))),
            Padding(padding: const EdgeInsets.all(8), child: Text(r.renovavel.toStringAsExponential(2))),
            Padding(padding: const EdgeInsets.all(8), child: Text(r.naoRenovavel.toStringAsExponential(2))),
            Padding(padding: const EdgeInsets.all(8), child: Text(r.comprado.toStringAsExponential(2))),
          ],
        )),
      ],
    );
  }

  ResultadoModel _resultadoPrincipal(List<ResultadoModel> resultados) {
    return resultados.firstWhere(
      (r) => r.processoFinal,
      orElse: () => resultados.reduce((a, b) => a.emergiaTotal >= b.emergiaTotal ? a : b),
    );
  }

  String _formatDecimal(double? value) {
    if (value == null) {
      return '-';
    }
    if (value != 0 && value.abs() < 0.01) {
      return value.toStringAsExponential(2);
    }
    return value.toStringAsFixed(2);
  }
}

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
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
              ? const Center(child: Text('Nenhum resultado disponível. Execute o cálculo.'))
              : SingleChildScrollView(
                  padding: const EdgeInsets.all(24),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildIndicatorsCard(provider),
                      const SizedBox(height: 32),
                      Text('Detalhamento por Processo', style: Theme.of(context).textTheme.titleLarge),
                      const SizedBox(height: 16),
                      _buildResultsTable(provider),
                    ],
                  ),
                ),
    );
  }

  Widget _buildIndicatorsCard(EmergyProvider provider) {
    // Pegando indicadores do primeiro resultado (ou média/total se aplicável)
    final res = provider.resultados.first; 
    
    return Card(
      elevation: 4,
      child: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          children: [
            Text('Indicadores de Sustentabilidade', style: Theme.of(context).textTheme.titleMedium),
            const SizedBox(height: 24),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _indicatorBox('EYR', res.eyr?.toStringAsFixed(2) ?? '-', 'Emergy Yield Ratio'),
                _indicatorBox('ELR', res.elr?.toStringAsFixed(2) ?? '-', 'Env. Loading Ratio'),
                _indicatorBox('ESI', res.esi?.toStringAsFixed(2) ?? '-', 'Sustainability Index'),
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
        Text(value, style: const TextStyle(fontSize: 32, color: Colors.green, fontWeight: FontWeight.w900)),
        const SizedBox(height: 4),
        Text(desc, style: const TextStyle(fontSize: 10, color: Colors.grey)),
      ],
    );
  }

  Widget _buildResultsTable(EmergyProvider provider) {
    return Table(
      border: TableBorder.all(color: Colors.grey.shade300),
      columnWidths: const {
        0: FlexColumnWidth(2),
        1: FlexColumnWidth(1.5),
        2: FlexColumnWidth(1.5),
        3: FlexColumnWidth(1.5),
      },
      children: [
        TableRow(
          decoration: BoxDecoration(color: Colors.grey.shade100),
          children: const [
            Padding(padding: EdgeInsets.all(8), child: Text('Processo', style: TextStyle(fontWeight: FontWeight.bold))),
            Padding(padding: EdgeInsets.all(8), child: Text('Emergia Total (sej)', style: TextStyle(fontWeight: FontWeight.bold))),
            Padding(padding: EdgeInsets.all(8), child: Text('Emergia Direta', style: TextStyle(fontWeight: FontWeight.bold))),
            Padding(padding: EdgeInsets.all(8), child: Text('Transformidade', style: TextStyle(fontWeight: FontWeight.bold))),
          ],
        ),
        ...provider.resultados.map((r) => TableRow(
          children: [
            Padding(padding: const EdgeInsets.all(8), child: Text(r.processoNome)),
            Padding(padding: const EdgeInsets.all(8), child: Text(r.emergiaTotal.toStringAsExponential(2))),
            Padding(padding: const EdgeInsets.all(8), child: Text(r.emergiaDireta.toStringAsExponential(2))),
            Padding(padding: const EdgeInsets.all(8), child: Text(r.transformidade.toStringAsExponential(2))),
          ],
        )),
      ],
    );
  }
}

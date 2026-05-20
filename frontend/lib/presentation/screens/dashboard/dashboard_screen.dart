import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:fl_chart/fl_chart.dart';
import '../../state/emergy_provider.dart';
import '../../state/processo_provider.dart';
import '../../../data/models/resultado_model.dart';
import '../../../data/models/processo_model.dart';

class DashboardScreen extends StatefulWidget {
  const DashboardScreen({super.key});

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  final List<Color> _chartColors = [
    Colors.blue,
    Colors.red,
    Colors.green,
    Colors.orange,
    Colors.purple,
    Colors.teal,
    Colors.amber,
    Colors.indigo,
  ];

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<EmergyProvider>();
    final processosProvider = context.watch<ProcessoProvider>();

    if (provider.isLoading || processosProvider.isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (provider.resultados.isEmpty) {
      return const Center(
        child: Text(
          'Nenhum dado disponível. Importe os dados e calcule a emergia.',
          style: TextStyle(fontSize: 16),
        ),
      );
    }

    // Filtrar os resultados para pegar apenas RECURSO para o gráfico de pizza
    final recursosParaPizza = provider.resultados.where((r) {
      try {
        final p = processosProvider.processos.firstWhere((p) => p.id == r.processoId);
        return p.tipo == TipoProcesso.RECURSO;
      } catch (e) {
        return false;
      }
    }).toList();

    return SingleChildScrollView(
      padding: const EdgeInsets.all(24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _buildIndicatorsCard(_resultadoPrincipal(provider.resultados)),
          const SizedBox(height: 32),
          Text('Composicao por Fontes Primarias', style: Theme.of(context).textTheme.titleLarge),
          const SizedBox(height: 16),
          _buildPieChartCard(recursosParaPizza.isEmpty ? provider.resultados : recursosParaPizza),
          const SizedBox(height: 32),
          Text('Emergia Direta vs Indireta (sej)', style: Theme.of(context).textTheme.titleLarge),
          const SizedBox(height: 16),
          _buildBarChartCard(provider.resultados),
        ],
      ),
    );
  }

  Widget _buildIndicatorsCard(ResultadoModel resultado) {
    return Card(
      elevation: 4,
      child: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          children: [
            Text('Indicadores de Sustentabilidade - ${resultado.processoNome}', style: Theme.of(context).textTheme.titleMedium),
            const SizedBox(height: 24),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _indicatorBox('EYR', _formatDecimal(resultado.eyr), 'Emergy Yield Ratio'),
                _indicatorBox('ELR', _formatDecimal(resultado.elr), 'Env. Loading Ratio'),
                _indicatorBox('ESI', _formatDecimal(resultado.esi), 'Sustainability Index'),
              ],
            ),
            const SizedBox(height: 24),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _indicatorBox('R', resultado.renovavel.toStringAsExponential(2), 'Renovavel'),
                _indicatorBox('N', resultado.naoRenovavel.toStringAsExponential(2), 'Nao renovavel'),
                _indicatorBox('F', resultado.comprado.toStringAsExponential(2), 'Comprado'),
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

  Widget _buildPieChartCard(List<ResultadoModel> resultados) {
    return Card(
      elevation: 4,
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SizedBox(
          height: 300,
          child: Row(
            children: [
              Expanded(
                flex: 2,
                child: PieChart(
                  PieChartData(
                    sectionsSpace: 2,
                    centerSpaceRadius: 40,
                    sections: resultados.asMap().entries.map((entry) {
                      final index = entry.key;
                      final res = entry.value;
                      final percentual = _getPercentual(res, resultados);
                      return PieChartSectionData(
                        color: _chartColors[index % _chartColors.length],
                        value: res.emergiaTotal,
                        title: _formatPercentual(percentual),
                        radius: 100,
                        titleStyle: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Colors.white),
                      );
                    }).toList(),
                  ),
                ),
              ),
              Expanded(
                flex: 1,
                child: ListView.builder(
                  shrinkWrap: true,
                  itemCount: resultados.length,
                  itemBuilder: (context, index) {
                    final res = resultados[index];
                    return Padding(
                      padding: const EdgeInsets.symmetric(vertical: 4.0),
                      child: Row(
                        children: [
                          Container(
                            width: 16,
                            height: 16,
                            color: _chartColors[index % _chartColors.length],
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: Text(
                              '${res.processoNome} - ${res.emergiaTotal.toStringAsExponential(2)} sej',
                              style: const TextStyle(fontSize: 12),
                              overflow: TextOverflow.ellipsis,
                            ),
                          ),
                        ],
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  double _getTotalEmergia(List<ResultadoModel> resultados) {
    return resultados.fold(0, (sum, item) => sum + item.emergiaTotal);
  }

  double _getPercentual(ResultadoModel resultado, List<ResultadoModel> resultados) {
    final total = _getTotalEmergia(resultados);
    if (total <= 0) {
      return 0;
    }
    return resultado.emergiaTotal / total * 100;
  }

  String _formatPercentual(double percentual) {
    if (percentual > 0 && percentual < 0.1) {
      return '<0.1%';
    }
    return '${percentual.toStringAsFixed(1)}%';
  }

  Widget _buildBarChartCard(List<ResultadoModel> resultados) {
    // Para simplificar a visualização, pegamos no máximo 5 ou 10 processos se houver muitos
    final displayResults = resultados.take(10).toList();
    
    double maxY = 0;
    for (var r in displayResults) {
      if (r.emergiaDireta > maxY) maxY = r.emergiaDireta;
      if (r.emergiaIndireta > maxY) maxY = r.emergiaIndireta;
    }
    // Adicionar margem de 10%
    maxY = maxY * 1.1;

    return Card(
      elevation: 4,
      child: Padding(
        padding: const EdgeInsets.all(24.0),
        child: SizedBox(
          height: 300,
          child: Column(
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  _legendItem(Colors.lightBlue, 'Emergia Direta'),
                  const SizedBox(width: 24),
                  _legendItem(Colors.indigo, 'Emergia Indireta'),
                ],
              ),
              const SizedBox(height: 24),
              Expanded(
                child: BarChart(
                  BarChartData(
                    alignment: BarChartAlignment.spaceAround,
                    maxY: maxY,
                    barTouchData: BarTouchData(
                      enabled: true,
                      touchTooltipData: BarTouchTooltipData(
                        getTooltipItem: (group, groupIndex, rod, rodIndex) {
                          return BarTooltipItem(
                            '${displayResults[group.x.toInt()].processoNome}\n',
                            const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                            children: <TextSpan>[
                              TextSpan(
                                text: rod.toY.toStringAsExponential(2),
                                style: const TextStyle(color: Colors.yellowAccent),
                              ),
                            ],
                          );
                        },
                      ),
                    ),
                    titlesData: FlTitlesData(
                      show: true,
                      bottomTitles: AxisTitles(
                        sideTitles: SideTitles(
                          showTitles: true,
                          getTitlesWidget: (double value, TitleMeta meta) {
                            if (value.toInt() >= displayResults.length) return const Text('');
                            String name = displayResults[value.toInt()].processoNome;
                            // Abreviar nomes longos para não encavalar
                            if (name.length > 10) name = '${name.substring(0, 10)}...';
                            return Padding(
                              padding: const EdgeInsets.only(top: 8.0),
                              child: Text(name, style: const TextStyle(fontSize: 10)),
                            );
                          },
                          reservedSize: 42,
                        ),
                      ),
                      leftTitles: AxisTitles(
                        sideTitles: SideTitles(
                          showTitles: true,
                          reservedSize: 60,
                          getTitlesWidget: (value, meta) {
                            if (value == 0) return const Text('0', style: TextStyle(fontSize: 10));
                            return Text(value.toStringAsExponential(1), style: const TextStyle(fontSize: 10));
                          },
                        ),
                      ),
                      topTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
                      rightTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
                    ),
                    gridData: const FlGridData(show: true, drawVerticalLine: false),
                    borderData: FlBorderData(show: false),
                    barGroups: displayResults.asMap().entries.map((entry) {
                      final index = entry.key;
                      final res = entry.value;
                      return BarChartGroupData(
                        x: index,
                        barRods: [
                          BarChartRodData(
                            toY: res.emergiaDireta,
                            color: Colors.lightBlue,
                            width: 16,
                            borderRadius: const BorderRadius.vertical(top: Radius.circular(4)),
                          ),
                          BarChartRodData(
                            toY: res.emergiaIndireta,
                            color: Colors.indigo,
                            width: 16,
                            borderRadius: const BorderRadius.vertical(top: Radius.circular(4)),
                          ),
                        ],
                      );
                    }).toList(),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _legendItem(Color color, String label) {
    return Row(
      children: [
        Container(width: 16, height: 16, color: color),
        const SizedBox(width: 8),
        Text(label, style: const TextStyle(fontSize: 12)),
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

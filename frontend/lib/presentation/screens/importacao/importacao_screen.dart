import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../state/emergy_provider.dart';

class ImportacaoScreen extends StatefulWidget {
  const ImportacaoScreen({super.key});

  @override
  State<ImportacaoScreen> createState() => _ImportacaoScreenState();
}

class _ImportacaoScreenState extends State<ImportacaoScreen> {
  final TextEditingController _controller = TextEditingController();
  String _selectedFormat = 'JSON';

  void _importar() async {
    if (_controller.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Por favor, insira o conteúdo LCI')),
      );
      return;
    }

    try {
      await context.read<EmergyProvider>().importarLCI(
        _controller.text,
        _selectedFormat,
      );
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Importação concluída com sucesso!')),
      );
      _controller.clear();
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro na importação: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final isLoading = context.watch<EmergyProvider>().isLoading;

    return Padding(
      padding: const EdgeInsets.all(24.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Importar Dados LCI',
            style: Theme.of(context).textTheme.headlineMedium,
          ),
          const SizedBox(height: 8),
          const Text('Cole aqui o conteúdo do arquivo LCI (JSON ou CSV) para processamento.'),
          const SizedBox(height: 24),
          Row(
            children: [
              const Text('Formato: '),
              const SizedBox(width: 16),
              DropdownButton<String>(
                value: _selectedFormat,
                items: ['JSON', 'CSV'].map((f) => DropdownMenuItem(
                  value: f,
                  child: Text(f),
                )).toList(),
                onChanged: (val) => setState(() => _selectedFormat = val!),
              ),
            ],
          ),
          const SizedBox(height: 16),
          Expanded(
            child: TextField(
              controller: _controller,
              maxLines: null,
              expands: true,
              textAlignVertical: TextAlignVertical.top,
              decoration: const InputDecoration(
                hintText: 'Cole o conteúdo aqui...',
                border: OutlineInputBorder(),
              ),
              style: const TextStyle(fontFamily: 'monospace', fontSize: 13),
            ),
          ),
          const SizedBox(height: 16),
          SizedBox(
            width: double.infinity,
            height: 50,
            child: ElevatedButton.icon(
              onPressed: isLoading ? null : _importar,
              icon: isLoading 
                  ? const SizedBox(width: 20, height: 20, child: CircularProgressIndicator(strokeWidth: 2))
                  : const Icon(Icons.upload_file),
              label: const Text('REALIZAR IMPORTAÇÃO'),
            ),
          ),
        ],
      ),
    );
  }
}

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:file_picker/file_picker.dart';
import 'dart:convert';
import 'dart:io';
import 'package:flutter/foundation.dart' show kIsWeb;
import '../../state/emergy_provider.dart';

class ImportacaoScreen extends StatefulWidget {
  const ImportacaoScreen({super.key});

  @override
  State<ImportacaoScreen> createState() => _ImportacaoScreenState();
}

class _ImportacaoScreenState extends State<ImportacaoScreen> {
  String _selectedFormat = 'JSON';
  PlatformFile? _selectedFile;

  void _pickFile() async {
    final result = await FilePicker.platform.pickFiles(
      type: FileType.custom,
      allowedExtensions: _selectedFormat == 'JSON' ? ['json'] : ['csv'],
    );

    if (result != null) {
      setState(() {
        _selectedFile = result.files.first;
      });
    }
  }

  void _importar() async {
    if (_selectedFile == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Por favor, selecione um arquivo')),
      );
      return;
    }

    try {
      String conteudo;
      if (kIsWeb) {
        conteudo = utf8.decode(_selectedFile!.bytes!);
      } else {
        conteudo = await File(_selectedFile!.path!).readAsString();
      }

      await context.read<EmergyProvider>().importarLCI(
        conteudo,
        _selectedFormat,
      );
      
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Importação concluída com sucesso!')),
      );
      setState(() {
        _selectedFile = null;
      });
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
          const Text('Selecione um arquivo LCI (JSON ou CSV) para processamento.'),
          const SizedBox(height: 24),
          Row(
            children: [
              const Text('Formato esperado: '),
              const SizedBox(width: 16),
              DropdownButton<String>(
                value: _selectedFormat,
                items: ['JSON', 'CSV'].map((f) => DropdownMenuItem(
                  value: f,
                  child: Text(f),
                )).toList(),
                onChanged: (val) {
                  setState(() {
                    _selectedFormat = val!;
                    _selectedFile = null;
                  });
                },
              ),
            ],
          ),
          const SizedBox(height: 32),
          Center(
            child: Column(
              children: [
                GestureDetector(
                  onTap: isLoading ? null : _pickFile,
                  child: Container(
                    width: double.infinity,
                    height: 200,
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.grey.shade400, style: BorderStyle.solid),
                      borderRadius: BorderRadius.circular(12),
                      color: Colors.grey.shade50,
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          _selectedFile != null ? Icons.insert_drive_file : Icons.cloud_upload,
                          size: 64,
                          color: _selectedFile != null ? Colors.green : Colors.blue,
                        ),
                        const SizedBox(height: 16),
                        Text(
                          _selectedFile != null 
                              ? _selectedFile!.name 
                              : 'Clique para selecionar o arquivo',
                          style: const TextStyle(fontWeight: FontWeight.bold),
                        ),
                        if (_selectedFile != null)
                          Text(
                            '${(_selectedFile!.size / 1024).toStringAsFixed(2)} KB',
                            style: TextStyle(color: Colors.grey.shade600, fontSize: 12),
                          ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 32),
                SizedBox(
                  width: double.infinity,
                  height: 50,
                  child: ElevatedButton.icon(
                    onPressed: (isLoading || _selectedFile == null) ? null : _importar,
                    icon: isLoading 
                        ? const SizedBox(width: 20, height: 20, child: CircularProgressIndicator(strokeWidth: 2))
                        : const Icon(Icons.send),
                    label: const Text('ENVIAR ARQUIVO'),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

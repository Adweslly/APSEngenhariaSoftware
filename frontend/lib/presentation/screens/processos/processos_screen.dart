import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../data/models/processo_model.dart';
import '../../state/processo_provider.dart';

class ProcessosScreen extends StatefulWidget {
  const ProcessosScreen({super.key});

  @override
  State<ProcessosScreen> createState() => _ProcessosScreenState();
}

class _ProcessosScreenState extends State<ProcessosScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<ProcessoProvider>().fetchProcessos();
    });
  }

  void _showForm([ProcessoModel? processo]) {
    showDialog(
      context: context,
      builder: (context) => ProcessoFormDialog(processo: processo),
    );
  }

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<ProcessoProvider>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Gerenciamento de Processos'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: provider.fetchProcessos,
          ),
        ],
      ),
      body: provider.isLoading
          ? const Center(child: CircularProgressIndicator())
          : provider.processos.isEmpty
              ? const Center(child: Text('Nenhum processo cadastrado.'))
              : ListView.separated(
                  padding: const EdgeInsets.all(16),
                  itemCount: provider.processos.length,
                  separatorBuilder: (_, __) => const Divider(),
                  itemBuilder: (context, index) {
                    final p = provider.processos[index];
                    return ListTile(
                      title: Text(p.nome, style: const TextStyle(fontWeight: FontWeight.bold)),
                      subtitle: Text('${p.tipo.name} | ${p.categoria}\n${p.descricao}'),
                      trailing: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          IconButton(
                            icon: const Icon(Icons.edit, color: Colors.blue),
                            onPressed: () => _showForm(p),
                          ),
                          IconButton(
                            icon: const Icon(Icons.delete, color: Colors.red),
                            onPressed: () => provider.excluirProcesso(p.id!),
                          ),
                        ],
                      ),
                      isThreeLine: true,
                    );
                  },
                ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _showForm(),
        label: const Text('NOVO PROCESSO'),
        icon: const Icon(Icons.add),
      ),
    );
  }
}

class ProcessoFormDialog extends StatefulWidget {
  final ProcessoModel? processo;
  const ProcessoFormDialog({super.key, this.processo});

  @override
  State<ProcessoFormDialog> createState() => _ProcessoFormDialogState();
}

class _ProcessoFormDialogState extends State<ProcessoFormDialog> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _nomeController;
  late TextEditingController _descController;
  late TextEditingController _catController;
  late TipoProcesso _tipo;
  late bool _coproduto;

  @override
  void initState() {
    super.initState();
    _nomeController = TextEditingController(text: widget.processo?.nome);
    _descController = TextEditingController(text: widget.processo?.descricao);
    _catController = TextEditingController(text: widget.processo?.categoria);
    _tipo = widget.processo?.tipo ?? TipoProcesso.CONSUMIDOR;
    _coproduto = widget.processo?.coproduto ?? false;
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.processo == null ? 'Novo Processo' : 'Editar Processo'),
      content: SingleChildScrollView(
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                controller: _nomeController,
                decoration: const InputDecoration(labelText: 'Nome'),
                validator: (v) => v!.isEmpty ? 'Obrigatório' : null,
              ),
              TextFormField(
                controller: _descController,
                decoration: const InputDecoration(labelText: 'Descrição'),
              ),
              TextFormField(
                controller: _catController,
                decoration: const InputDecoration(labelText: 'Categoria'),
              ),
              DropdownButtonFormField<TipoProcesso>(
                value: _tipo,
                items: TipoProcesso.values.map((t) => DropdownMenuItem(value: t, child: Text(t.name))).toList(),
                onChanged: (v) => setState(() => _tipo = v!),
                decoration: const InputDecoration(labelText: 'Tipo'),
              ),
              SwitchListTile(
                title: const Text('É Co-produto?'),
                value: _coproduto,
                onChanged: (v) => setState(() => _coproduto = v),
              ),
            ],
          ),
        ),
      ),
      actions: [
        TextButton(onPressed: () => Navigator.pop(context), child: const Text('CANCELAR')),
        ElevatedButton(
          onPressed: () async {
            if (_formKey.currentState!.validate()) {
              final model = ProcessoModel(
                id: widget.processo?.id,
                nome: _nomeController.text,
                descricao: _descController.text,
                tipo: _tipo,
                categoria: _catController.text,
                coproduto: _coproduto,
              );
              await context.read<ProcessoProvider>().salvarProcesso(model);
              if (!mounted) return;
              Navigator.pop(context);
            }
          },
          child: const Text('SALVAR'),
        ),
      ],
    );
  }
}

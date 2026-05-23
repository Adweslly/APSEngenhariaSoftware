# Emergy Analysis Frontend

Interface gráfica para o sistema de análise de emergia, desenvolvida com Flutter. Esta aplicação permite o gerenciamento de processos, importação de dados LCI e visualização detalhada dos resultados de cálculos de emergia.

## 🚀 Tecnologias Utilizadas

- **Flutter**: Framework para desenvolvimento cross-platform.
- **Dart**: Linguagem de programação.
- **Provider**: Gerenciamento de estado.
- **HTTP**: Comunicação com a API REST.
- **Google Fonts**: Tipografia personalizada (Lato/Roboto).
- **FL Chart**: Gráficos de indicadores.
- **GraphView**: Visualização da rede de processos.

## 🏗️ Arquitetura

O projeto segue uma estrutura organizada por camadas:

- **`lib/core`**: Configurações base, temas e utilitários da aplicação.
- **`lib/data`**:
  - `models/`: Definições de classes de dados (Processo, Resultado).
  - `services/`: Lógica de comunicação com o backend (API).
- **`lib/presentation`**:
  - `screens/`: Telas principais da aplicação.
  - `state/`: Gerenciamento de estado global (Providers).
  - `widgets/`: Componentes de UI reutilizáveis.

## 📋 Pré-requisitos

- Flutter SDK (^3.11.5)
- Backend em execução (Spring Boot) na porta `8080` (configurável em `lib/core/api/api_base.dart`)

## ⚙️ Instalação e Execução

1.  Clone o repositório.
2.  Navegue até a pasta do frontend:
    ```bash
    cd frontend
    ```
3.  Obtenha as dependências:
    ```bash
    flutter pub get
    ```
4.  Execute a aplicação:
    ```bash
    flutter run
    ```

## 📖 Guia de Uso

### 1. Tela Inicial
Ao abrir a aplicação, você verá o Dashboard com acesso rápido às principais funcionalidades:
- **Gerenciar Processos**: Visualizar e criar novos processos produtivos.
- **Importar LCI**: Importar dados no formato JSON/LCI.
- **Resultados**: Ver os últimos cálculos realizados.
- **Visualização**: Ver a rede de processos graficamente.

### 2. Importação de Dados
Na tela de importação, você pode colar o conteúdo JSON de um arquivo LCI. O sistema validará a estrutura e criará automaticamente a rede de processos e fluxos no backend.

### 3. Gerenciamento de Processos
- Visualize a lista de todos os processos cadastrados.
- Cada processo exibe seu nome, tipo (Transformação, Recurso, etc.) e categoria.
- (Em breve) Edição e exclusão direta pela interface.

### 4. Cálculo de Emergia
Na tela de resultados, você pode disparar o cálculo para uma rede específica. O backend processará os dados e o frontend exibirá:
- **Tabela de Fluxos**: Detalhes de cada recurso (Uev/j, Emergia Solar, etc.).
- **Indicadores**: Gráficos visuais (EYR, ELR, ESI) para análise de sustentabilidade.

### 5. Visualização de Rede
Exibe graficamente como os processos estão conectados. Útil para identificar dependências e fluxos circulares na análise.

## 🛠️ Configuração da API

A URL base da API pode ser alterada em `lib/core/api/api_base.dart`:

```dart
static const String baseUrl = 'http://localhost:8080/api';
```

## 🧪 Testes

Para executar os testes de widget:
```bash
flutter test
```

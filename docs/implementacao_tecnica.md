# Implementacao tecnica

Este documento concentra detalhes tecnicos do estado atual do projeto. O README da raiz deve permanecer como guia rapido de uso e overview.

## Estrutura geral

```text
APSEngenhariaSoftware/
|-- README.md
|-- docs/
|-- frontend/
|-- java/
|-- exemplo_lci_soja.json
`-- analise_complexa_biodiesel.json
```

## Backend

O backend fica em `java/` e usa:

- Java 17
- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- springdoc-openapi para Swagger UI
- Maven

Ponto de entrada:

```text
java/src/main/java/com/aps/ApsEngenhariaSoftwareApplication.java
```

Configuracao principal:

```text
java/src/main/resources/application.properties
```

Valores atuais:

```properties
spring.application.name=aps-engenharia-software
server.port=8080
spring.datasource.url=jdbc:h2:mem:apsdb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Camadas

- `domain/model`: entidades de dominio como `Processo`, `Fluxo`, `TipoRecurso`, `RedeProcessos` e `ResultadoEmergia`.
- `domain/enums`: enumeradores de categoria, fonte e tipo de processo.
- `dados/importador`: importacao e parsing de dados LCI/JSON.
- `dados/repositorio`: repositories JPA.
- `dados/transformador`: conversao de dados importados para o modelo interno.
- `processamento/algoritmo`: motor principal de calculo de emergia.
- `processamento/indicadores`: calculo de indicadores como EYR, ELR e ESI.
- `processamento/validador`: validacoes da algebra emergetica.
- `service`: regras de aplicacao usadas pelos controllers.
- `web/controller`: API REST.
- `web/dto`: objetos de transferencia da API.
- `config`: configuracao do Swagger/OpenAPI.

### Controllers e rotas

- `EmergyController`: `GET /api/emergy/calculate`
- `ProcessoController`: CRUD em `/api/processos`
- `FluxoController`: CRUD em `/api/fluxos`
- `TipoRecursoController`: CRUD em `/api/recursos`
- `RedeProcessosController`: CRUD basico em `/api/redes` e calculo em `/api/redes/{id}/calcular`
- `LciController`: importacao em `POST /api/lci/import`

### Observacoes de implementacao

- O calculo de `/api/redes/{id}/calcular` atualmente chama `calculationService.calcularTudo()` e retorna resultados gerais, sem filtrar efetivamente pela rede informada.
- O banco H2 usa `create-drop`, entao os dados nao persistem entre execucoes.
- O projeto possui teste automatizado para o calculador de emergia em `java/src/test/java/com/aps/processamento/algoritmo/CalculadorEmergiaTest.java`.

## Frontend

O frontend fica em `frontend/` e usa:

- Flutter
- Dart SDK `^3.11.5`
- Provider
- HTTP
- Google Fonts
- FL Chart
- GraphView
- File Picker

Ponto de entrada:

```text
frontend/lib/main.dart
```

Base da API:

```text
frontend/lib/core/api/api_base.dart
```

Valor atual:

```dart
static const String baseUrl = 'http://localhost:8080/api';
```

### Organizacao

- `lib/core`: configuracao de API e tema.
- `lib/data/models`: modelos usados pelo frontend.
- `lib/data/services`: servicos HTTP para backend.
- `lib/presentation/screens`: telas da aplicacao.
- `lib/presentation/state`: providers de estado.

### Telas

A navegacao principal fica em `HomeScreen` com `NavigationRail` e inclui:

- Home/Dashboard
- Importar LCI
- Processos
- Visualizar
- Resultados

### Comunicacao com a API

- `EmergyService` consome `/emergy/calculate` e `/lci/import`.
- `ProcessoService` consome `/processos`.
- `FluxoService` consome `/fluxos`.

## Fluxo de uso esperado

1. Rodar o backend.
2. Rodar o frontend.
3. Importar um JSON LCI ou cadastrar processos manualmente.
4. Executar o calculo de emergia.
5. Consultar resultados, indicadores e visualizacao da rede.

## Arquivos de exemplo

- `exemplo_lci_soja.json`: exemplo simples de inventario LCI.
- `analise_complexa_biodiesel.json`: exemplo mais completo para analise de biodiesel.

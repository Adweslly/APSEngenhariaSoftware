# APS Engenharia de Software

Sistema para analise de emergia em sistemas produtivos, com backend em Spring Boot e frontend em Flutter. O projeto recebe dados de Inventario de Ciclo de Vida (LCI), organiza processos e fluxos, executa calculos emergeticos e apresenta resultados e indicadores de sustentabilidade.

## Estado atual

O projeto possui dois modulos principais:

- `java/`: API REST em Java 17 com Spring Boot 3.2, Maven, JPA, banco H2 em memoria e documentacao Swagger/OpenAPI.
- `frontend/`: aplicacao Flutter que consome a API, permite importar LCI, gerenciar processos, visualizar rede de processos e consultar resultados.

Tambem existem arquivos JSON de exemplo na raiz:

- `exemplo_lci_soja.json`
- `analise_complexa_biodiesel.json`

## Como funciona

1. O frontend envia requisicoes para a API em `http://localhost:8080/api`.
2. A API cadastra processos, recursos e fluxos no banco H2 em memoria.
3. A importacao LCI recebe conteudo JSON e transforma esses dados em estruturas do dominio.
4. O motor de calculo processa os fluxos de entrada e saida, calcula emergia direta, indireta e total.
5. Os resultados retornam para o frontend com indicadores como EYR, ELR e ESI.

Como o banco H2 esta configurado em memoria, os dados sao recriados a cada nova execucao do backend.

## Requisitos

- Java 17 ou superior
- Maven
- Flutter SDK com Dart compativel com `^3.11.5`
- Backend rodando na porta `8080` antes de usar as telas do frontend que consomem a API

## Rodar o backend

```bash
cd java
mvn spring-boot:run
```

Servicos principais:

- API: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- H2 Console: `http://localhost:8080/h2-console`

Credenciais do H2:

- JDBC URL: `jdbc:h2:mem:apsdb`
- User: `sa`
- Password: vazio

## Rodar o frontend

```bash
cd frontend
flutter pub get
flutter run
```

A URL da API usada pelo frontend esta em:

```text
frontend/lib/core/api/api_base.dart
```

Valor atual:

```text
http://localhost:8080/api
```

## Funcionalidades disponiveis

- Dashboard inicial da aplicacao.
- Importacao de dados LCI via JSON.
- Listagem, criacao, atualizacao e exclusao de processos.
- Consulta de fluxos no frontend.
- Calculo de emergia para os dados cadastrados/importados.
- Exibicao de resultados e indicadores.
- Visualizacao grafica da rede de processos.
- API REST documentada com Swagger.

## Endpoints principais

- `GET /api/processos`
- `POST /api/processos`
- `PUT /api/processos/{id}`
- `DELETE /api/processos/{id}`
- `GET /api/fluxos`
- `POST /api/fluxos`
- `GET /api/recursos`
- `POST /api/recursos`
- `GET /api/redes`
- `POST /api/redes`
- `POST /api/redes/{id}/calcular`
- `GET /api/emergy/calculate`
- `POST /api/lci/import`

## Testes

Backend:

```bash
cd java
mvn test
```

Frontend:

```bash
cd frontend
flutter test
```

## Documentacao

O README da raiz serve como guia rapido de funcionamento e execucao. Documentos tecnicos, especificacoes, relatorios e materiais de apoio ficam em `docs/`.

Arquivos principais:

- `docs/implementacao_tecnica.md`: detalhes tecnicos e arquitetura atual.
- `docs/SPEC.md`: especificacao original do projeto.
- `docs/plano_implementacao.md`: plano de implementacao.
- `docs/relatorio_emergia.md`: relatorio sobre emergia.
- `docs/backend_java.md`: README anterior do backend.
- `docs/frontend_flutter.md`: README anterior do frontend.
- `docs/sprint1_backend_mvp.md`: documentacao da sprint/MVP do backend.
- `docs/ES_Orientacoes_Aluno.md`: orientacoes academicas.

## Licenca

Projeto academico desenvolvido para APS de Engenharia de Software.

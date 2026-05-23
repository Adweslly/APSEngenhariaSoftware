# Plano da Sprint 1 - Backend API (MVP Ponta a Ponta)

## Objetivo
Entregar um Minimum Viable Product (MVP) funcional do backend Spring Boot, contemplando todo o ciclo de vida dos dados: desde o CRUD básico e importação síncrona de arquivos LCI, até o motor de cálculo completo cobrindo as regras da álgebra emergética e exportação dos indicadores.

## Key Files & Context
- `src/main/java/com/aps/domain/model/`: Entidades de domínio (`Processo`, `Fluxo`, `Transformidade` - a ser criada).
- `src/main/java/com/aps/dados/repositorio/`: Repositórios Spring Data JPA.
- `src/main/java/com/aps/web/controller/`: Endpoints REST.
- `src/main/java/com/aps/dados/importador/`: Serviços de parsing LCI.
- `src/main/java/com/aps/processamento/algoritmo/`: Onde o `CalculadorEmergia` e o Grafo serão implementados.

## Escopo das Tarefas

### Task 1: Modelagem de Domínio e Persistência
- Mapear as entidades JPA necessárias: `Processo`, `Fluxo`, `Transformidade` (para armazenar os UEVs base), e `ResultadoEmergia`.
- Configurar relacionamentos corretamente (ex: `Processo` 1-N `Fluxo`).
- Criar os repositórios correspondentes estendendo `JpaRepository`.

### Task 2: Desenvolvimento da API REST (CRUDs)
- Implementar Services e Controllers para permitir operações básicas de Criação, Leitura, Atualização e Deleção (CRUD) de Processos, Fluxos e Transformidades.
- Garantir o mapeamento com DTOs adequados (`ProcessoDTO`, `FluxoDTO`, etc.).

### Task 3: Serviço de Importação LCI Síncrono
- Criar o endpoint de importação (ex: `POST /api/lci/import`).
- Implementar o parser para receber o arquivo JSON/CSV na requisição, ler os dados e persistir como Processos e Fluxos no banco.
- O processamento deve ser síncrono, retornando status imediato de sucesso ou erro na validação.

### Task 4: Motor de Cálculo e Grafo (Álgebra Emergética)
- Desenvolver a estrutura de dados (Grafo) em memória para representar a rede de processos, utilizando os dados salvos no banco.
- Implementar o algoritmo de cálculo emergético suportando todas as regras fundamentais:
  - Soma de fluxos normais.
  - Divisões de fluxos (Splits - divisão da emergia acumulada).
  - Co-produtos (atribuição integral da emergia acumulada a todos os braços da ramificação).
  - Tratamento de loops/feedback para evitar contagem dupla (identificação de ciclos no grafo).

### Task 5: Indicadores e Resultados
- Implementar o serviço para calcular os índices finais: Transformidade de saída, EYR (Emergy Yield Ratio), ELR (Environmental Loading Ratio) e ESI (Emergy Sustainability Index).
- Criar endpoint (ex: `GET /api/emergy/calculate`) que executa o fluxo completo do motor de cálculo e retorna um relatório JSON estruturado com os resultados e indicadores.

## Verificação & Testing
- Cada camada (Controller, Service, Repository) deverá ter testes unitários básicos.
- O motor de álgebra emergética deverá ter uma suíte de testes unitários específica com cenários isolados para splits, co-produtos e loops.
- Teste de integração end-to-end simulando a importação de um arquivo de teste e verificando o resultado do endpoint de cálculo.

## Migration & Rollback
- O desenvolvimento ocorrerá utilizando o banco H2 em memória ou configurações locais descartáveis para a Fase MVP, permitindo recriação fácil do schema sem necessidade de scripts de migração (Flyway/Liquibase) neste momento.

# Plano de Implementação - Sistema de Cálculo de Emergia

## 1. Visão Geral do Projeto

Sistema computacional para cálculo de emergia em sistemas complexos a partir de dados de Inventário de Ciclo de Vida (LCI), inspirado no software SCALE (Marvuglia et al., 2013).

## 2. Arquitetura do Sistema

```
┌─────────────────────────────────────────────────────────┐
│                    Flutter Desktop                      │
│                      (Frontend)                         │
│    ┌──────────┐  ┌──────────┐  ┌──────────────────┐    │
│    │ Import  │  │ Modelagem│  │ Resultados/      │    │
│    │ LCI     │  │ Processos│  │ Relatórios       │    │
│    └──────────┘  └──────────┘  └──────────────────┘    │
└─────────────────────────────────────────────────────────┘
                         │ REST API
                         ▼
┌─────────────────────────────────────────────────────────┐
│                 Spring Boot Backend                     │
│    ┌──────────┐  ┌──────────┐  ┌──────────────────┐    │
│    │ Módulo   │  │ Simulador│  │ Controller       │    │
│    │ Dados    │  │ Cálculo  │  │ REST             │    │
│    └──────────┘  └──────────┘  └──────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

## 3. Requisitos Funcionais

### 3.1 Módulo de Gerenciamento de Dados LCI
- [ ] Importar arquivos LCI (CSV, JSON, Excel)
- [ ] Validar schema dos dados de entrada
- [ ] Representar sistemas como matrizes de processos
- [ ] Gerenciar banco de dados de transformidades
- [ ] Armazenar históricos de análises

### 3.2 Simulador de Cálculo de Emergia
- [ ] Implementar álgebra emergética
- [ ] Representar sistema como rede de processos interconectados
- [ ] Calcular emergia direta e indireta
- [ ] Evitar contagem dupla de fluxos
- [ ] Calcular indicadores: EYR, ELR, ESI

### 3.3 Interface Gráfica (GUI)
- [ ] Tela de importação de dados LCI
- [ ] Editor visual de processos e fluxos
- [ ] Visualização de grafo de processos
- [ ] Exibição de resultados e métricas
- [ ] Geração de relatórios

## 4. Requisitos Não Funcionais

- **Desempenho**: Processamento eficiente de grandes volumes de dados
- **Escalabilidade**: Suporte a sistemas complexos com múltiplos processos
- **Manutenibilidade**: Código bem estruturado e documentado
- **Usabilidade**: Interface intuitiva e amigável
- **Confiabilidade**: Precisão nos cálculos matemáticos

## 5. Fases de Desenvolvimento

### Fase 1: Pesquisa e Planejamento (Semanas 1-2)
- [ ] Estudo do artigo SCALE (Marvuglia et al., 2013)
- [ ] Estudo das regras da álgebra emergética
- [ ] Estudo do Emergy Simulator (EmSim)
- [ ] Definição de Escopo
- [ ] Mapeamento de Requisitos Funcionais
- [ ] Mapeamento de Requisitos Não Funcionais
- [ ] Levantamento de dados de transformidades

**Entregáveis:**
- Documento de Escopo
- Lista de Requisitos Funcionais
- Lista de Requisitos Não Funcionais
- Referências bibliográficas estudadas

### Fase 2: Modelagem (Semanas 3-4)
- [ ] Diagrama de Casos de Uso
- [ ] Diagrama de Classes
- [ ] Diagramas de Sequência
- [ ] Diagrama de Arquitetura
- [ ] Modelo de Dados

**Entregáveis:**
- Diagramas UML (casos de uso, classes, sequência)
- Documento de Arquitetura
- Modelo de Entidades

### Fase 3: Desenvolvimento Backend (Semanas 5-8)
- [ ] Setup projeto Spring Boot
- [ ] Estrutura de pacotes e camadas
- [ ] Implementar entidades (Process, Flow, Transformity)
- [ ] Implementar repositórios
- [ ] Implementar serviço de importação LCI
- [ ] Implementar grafo de processos
- [ ] Implementar álgebra emergética
- [ ] Implementar indicadores (EYR, ELR, ESI)
- [ ] Implementar controllers REST
- [ ] Configuração do Git e padrões de código

**Entregáveis:**
- Código fonte do backend
- Testes unitários (JUnit)
- Testes de integração
- Documentação de API

### Fase 4: Desenvolvimento Frontend (Semanas 9-12)
- [ ] Setup projeto Flutter Desktop
- [ ] Estrutura de diretórios
- [ ] Implementar tema e constantes
- [ ] Implementar modelos de dados
- [ ] Implementar serviços API
- [ ] Implementar tela de importação
- [ ] Implementar tela de modelagem
- [ ] Implementar tela de resultados
- [ ] Implementar visualização de grafo

**Entregáveis:**
- Código fonte do frontend
- Interface gráfica funcional
- Integração com backend

### Fase 5: Testes e Validação (Semanas 13-14)
- [ ] Execução de testes unitários
- [ ] Execução de testes de integração
- [ ] Testes de desempenho
- [ ] Testes de usabilidade
- [ ] Validação de resultados
- [ ] Correção de bugs

**Entregáveis:**
- Relatório de testes
- Resultados de validação

### Fase 6: Documentação e Apresentação (Semanas 15-16)
- [ ] Documentação técnica completa
- [ ] Manual do usuário
- [ ] Preparação da apresentação
- [ ] Banner de apresentação

**Entregáveis:**
- Documentação técnica (ABNT)
- Código fonte organizado
- Apresentação do sistema

## 6. Estrutura Backend (Java + Spring Boot)

```
src/main/java/com/emergy/
├── config/
│   └── AppConfig.java
├── controller/
│   ├── LciController.java
│   ├── ProcessController.java
│   └── EmergyController.java
├── service/
│   ├── LciImportService.java
│   ├── ProcessService.java
│   └── EmergyCalculationService.java
├── model/
│   ├── entity/
│   │   ├── Process.java
│   │   ├── Flow.java
│   │   ├── Transformity.java
│   │   └── EmergyResult.java
│   └── dto/
│       ├── LciDataDTO.java
│       ├── ProcessDTO.java
│       └── EmergyResultDTO.java
├── repository/
│   ├── ProcessRepository.java
│   └── TransformityRepository.java
├── engine/
│   ├── GraphBuilder.java
│   ├── EmergyCalculator.java
│   ├── AlgebraProcessor.java
│   └── IndicatorCalculator.java
└── exception/
    └── EmergyException.java
```

## 7. Estrutura Frontend (Flutter Desktop)

```
lib/
├── main.dart
├── app/
│   └── app.dart
├── core/
│   ├── constants/
│   ├── theme/
│   └── utils/
├── data/
│   ├── models/
│   ├── repositories/
│   └── services/
│       └── api_service.dart
└── presentation/
    ├── pages/
    │   ├── home_page.dart
    │   ├── import_page.dart
    │   ├── modelagem_page.dart
    │   └── resultados_page.dart
    └── widgets/
        ├── process_graph.dart
        └── result_charts.dart
```

## 8. Metodologia de Testes

Adotar **Test-Driven Development (TDD)**:

### Testes Unitários
- Entidades e domínios
- Services de cálculo
- Utilitários

### Testes de Integração
- API REST
- Repositórios
- Integração com banco de dados em memória

### Testes de Desempenho
- Processamento de grandes matrizes LCI
- Cálculos em sistemas complexos

## 9. Controle de Versão

- Repositório Git com branches:
  - `main` - Código estável
  - `develop` - Desenvolvimento
  - `feature/*` - Novas funcionalidades
  - `bugfix/*` - Correções
- Commits semanticamente descritos
- Code review antes de merge

## 10. Critérios de Avaliação

| Critério | Peso |
|----------|------|
| Qualidade do planejamento e modelagem | 30% |
| Funcionalidade do sistema e implementação | 30% |
| Boas práticas de desenvolvimento | 20% |
| Documentação e apresentação | 20% |

## 11. Referências

- MARVUGLIA, Antonino et al. SCALE: Software for CALculating Emergy based on Life Cycle Inventories. Ecological Modelling, v. 248, p. 80–91, 2013.
- ARBAULT, Damien et al. Emergy evaluation using the calculation software SCALE. Science of the Total Environment, v. 472, p. 608–619, 2014.
- VALYI, Raphaël; ORTEGA, Enrique. Emergy Simulator: An Open Source Simulation Platform. 2004.
- ODUM, H. T. Environmental Accounting: Emergy and Environmental Decision Making. 1996.
- BROWN, M. T.; ULGIATI, S. Emergy analysis and environmental accounting. 2002.

## 12. Próximos Passos

1. [ ] Revisar requisitos com base no documento de orientações
2. [ ] Iniciar estudo do artigo SCALE
3. [ ] Definir formato do arquivo LCI de entrada
4. [ ] Criar repositório Git
5. [ ] Iniciar desenvolvimento do backend

---

**Tecnologias:**
- Backend: Java 17+, Spring Boot 3.x, Maven
- Frontend: Flutter 3.x, Dart 3.x
- Comunicação: REST API (HTTP)
- Testes: JUnit, Mockito (backend), Flutter Test
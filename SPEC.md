# Especificação do Projeto - EmergyCalc

## 1. Escopo do Projeto

### 1.1 Objetivo
Desenvolvimento de um sistema computacional para cálculo de emergia em sistemas complexos, baseado em dados de Inventários de Ciclo de Vida (LCI), inspirado no software SCALE (Marvuglia et al., 2013).

### 1.2 Problea Abordado
Métodos tradicionais de avaliação energética analisam sistemas de forma fragmentada, desconsiderando interdependências e fluxos indiretos. A emergia, conceito desenvolvido por H. T. Odum, permite quantificar em uma unidade comum (sej - solar emjoule) todas as formas de energia utilizadas em um processo produtivo.

### 1.3 Solução Proposta
Sistema dividido em três módulos principais:
- **Módulo de Dados**: Gerenciamento de dados LCI
- **Módulo de Processamento**: Cálculo de emergia com base na álgebra emergética
- **Módulo de Apresentação**: Interface gráfica para visualização e interação

### 1.4 Limitações do Escopo
- Versão desktop (não web/mobile)
- Processamento de dados LCI em formato JSON
- Cálculo estático (previsão para simulação dinâmica em versões futuras)
- Sem integração com bases de dados externas

---

## 2. Estrutura do Sistema

### 2.1 Módulos Principais

```
┌─────────────────────────────────────────────────────────────┐
│                    APRESENTAÇÃO (GUI)                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │
│  │  Visualiz.  │  │  Relatórios │  │  Config    │           │
│  └─────────────┘  └─────────────┘  └─────────────┘           │
├─────────────────────────────────────────────────────────────┤
│                 PROCESSAMENTO (Core)                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │
│  │  Algoritmo  │  │  Validação  │  │  Indicad.  │           │
│  │  Emergia   │  │  Algebra   │  │  Sustainability│         │
│  └─────────────┘  └─────────────┘  └─────────────┘           │
├─────────────────────────────────────────────────────────────┤
│                      DADOS                                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │
│  │  Importador │  │  Reposit.  │  │  Transform. │           │
│  │  LCI       │  │  Dados    │  │  Dados      │           │
│  └─────────────┘  └─────────────┘  └─────────────┘           │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Descrição dos Módulos

| Módulo | Responsabilidade |
|--------|------------------|
| **Importador LCI** | Leitura e parsing de arquivos LCI (CSV, JSON, XML) |
| **Repositório de Dados** | Armazenamento e gerenciamento de dados em memória |
| **Transformador de Dados** | Conversão de dados LCI para estruturas do sistema |
| **Algoritmo de Emergia** | Implementação da álgebra emergética e cálculo |
| **Validador Algebra** | Verificação de consistência (evitar contagem dupla) |
| **Indicadores de Sustentabilidade** | Cálculo de EYR, ELR, ESI |
| **Visualizador** | Display de resultados e diagramas |
| **Gerador de Relatórios** | Exportação de resultados |
| **Configuração** | Parametrização do sistema |

---

## 3. Arquitetura

### 3.1 Padrão Arquitetural
**Backend API + Frontend Flutter** com três camadas:

```
┌────────────────────────────────────────┐
│          FRONTEND (Flutter)           │
│    (Widgets, Pages, Providers)        │
├────────────────────────────────────────┤
│          BACKEND API (Java)           │
│    (REST Controllers, Services)        │
├────────────────────────────────────────┤
│          CAMADA DE DADOS              │
│    (JPA/Hibernate + PostgreSQL)         │
└────────────────────────────────────────┘
```

### 3.2 Modelo de Domínio

```
┌─────────────────────────────────────────────────────────────┐
│                       ENTIDADES                             │
├─────────────────────────────────────────────────────────────┤
│  Processo                                                   │
│  - id: Long                                                 │
│  - nome: String                                             │
│  - tipo: TipoProcesso (PRODUTOR/CONSUMIDOR/ARMAZENAMENTO)   │
│  - inputs: List<Fluxo>                                      │
│  - outputs: List<Fluxo>                                     │
├─────────────────────────────────────────────────────────────┤
│  Fluxo                                                      │
│  - id: Long                                                 │
│  - quantidade: double                                      │
│  - tipoRecurso: TipoRecurso                                 │
│  - transformidade: double (sej/unit)                       │
│  - origem: Processo                                         │
│  - destino: Processo                                        │
├─────────────────────────────────────────────────────────────┤
│  TipoRecurso                                                │
│  - id: Long                                                 │
│  - nome: String                                             │
│  - unidade: String                                          │
│  - categoria: CategoriaRecurso (MATERIA/ENERGIA/SERVIÇO)   │
├─────────────────────────────────────────────────────────────┤
│  RedeProcessos                                              │
│  - processos: List<Processo>                                │
│  - fluxos: List<Fluxo>                                       │
│  - calcularEmergia(): Map<Processo, Double>                  │
├─────────────────────────────────────────────────────────────┤
│  RedeProcessos                                              │
│  - id: Long                                                 │
│  - nome: String                                             │
│  - processos: List<Processo>                                │
│  - fluxos: List<Fluxo>                                      │
│  - calcularEmergia(): Map<Processo, Double>                 │
├─────────────────────────────────────────────────────────────┤
│  ResultadoEmergia                                            │
│  - id: Long                                                 │
│  - processo: Processo                                      │
│  - emergiaTotal: double (sej)                               │
│  - emergiaDireta: double                                    │
│  - emergiaIndireta: double                                   │
│  - transformidade: double                                │
└─────────────────────────────────────────────────────────────┘
```

### 3.3 Padrões de Projeto (Design Patterns)
- **Factory**: Criação de entidades e importadores
- **Strategy**: Algoritmos de cálculo alternativ os
- **Repository**: Persistência de dados
- **MVC**: Separação GUI/Lógica
- **Builder**: Construção de objetos complexos

---

## 4. Stack Tecnológico

### 4.1 Tecnologias Principal

**Backend:**
| Componente | Tecnologia | Versão |
|------------|-----------|--------|
| **Linguagem** | Java | 17+ |
| **Framework** | Spring Boot | 3.x |
| **Build Tool** | Maven/Gradle | Latest |
| **ORM** | JPA/Hibernate | 6.x |
| **Banco de Dados** | PostgreSQL | 15+ |
| **Container** | Docker | Latest |
| **Anotações** | Lombok | Latest |
| **Testes** | JUnit 5 | 5.x |
| **BDD** | Cucumber-JVM | Latest |
| **Mock** | Mockito | 5.x |
| **Logging** | SLF4J/Logback | Latest |

**Frontend:**
| Componente | Tecnologia | Versão |
|------------|-----------|--------|
| **Framework** | Flutter | 3.x |
| **Linguagem** | Dart | 3.x |
| **Estado** | Provider/Riverpod | Latest |
| **HTTP** | Dio | Latest |
| **Charts** | fl_chart | Latest |

### 4.2 Dependências Externas

**Backend:**
| Biblioteca | Propósito |
|------------|-----------|
| Jackson | Parsing JSON |
| JFreeChart | Gráficos |
| Apache Commons | Utilitários |

**Frontend:**
| Biblioteca | Propósito |
|------------|-----------|
| dio | Requisições HTTP |
| provider | Gerenciamento de estado |
| fl_chart | Gráficos e visualização |
| json_annotation | Serialização JSON |

### 4.3 Ambiente de Desenvolvimento

- **IDE Backend**: IntelliJ IDEA / Eclipse
- **IDE Frontend**: Android Studio / VS Code
- **Versionamento**: Git
- **Repo Remoto**: GitHub/GitLab
- **Container**: Docker + Docker Compose

### 4.4 Persistência de Dados

**Configuração Docker/PostgreSQL:**
| Recurso | Configuração |
|--------|--------------|
| **Imagem** | postgres:15 |
| **Porta** | 5432 |
| **Banco** | emergycalc |
| **Usuário** | emergyuser |
| **Senha** | (variável de ambiente) |
| **Volume** | emergy_data |

**Docker Compose:**
```yaml
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: emergycalc
      POSTGRES_USER: emergyuser
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    ports:
      - "5432:5432"
    volumes:
      - emergy_data:/var/lib/postgresql/data
```

---

## 5. Requisitos Funcionais

### 5.1 Casos de Uso

#### RF01 - Importar Dados LCI
| Campo | Detalhe |
|-------|---------|
| **ID** | RF01 |
| **Nome** | Importar Dados de Inventário do Ciclo de Vida |
| **Ator** | Analista/Usuário |
| **Pré-condições** | Arquivo LCI existir em formato suportado |
| **Pós-condições** | Dados carregados no sistema |
| **Fluxo Principal** | 1. Usuário seleciona arquivo LCI |
| | 2. Sistema valida formato |
| | 3. Sistema parseia dados |
| | 4. Dados armazenados em memória |
| | 5. Retorno de confirmação |
| **Fluxo Alternativo** | 3a. Formato inválido → Erro detalhado |
| **Critério de Aceitação** | Arquivo JSON com dados válidos carregado com sucesso |

#### RF02 - Criar/Editar Rede de Processos
| Campo | Detalhe |
|-------|---------|
| **ID** | RF02 |
| **Nome** | Criar ou Editar Rede de Processos |
| **Ator** | Analista/Usuário |
| **Pré-condições** | Dados LCI importados |
| **Pós-condições** | Rede de processos definida |
| **Fluxo Principal** | 1. Usuário inicia criação de rede |
| | 2. Sistema apresenta editor |
| | 3. Usuário define processos e conexões |
| | 4. Sistema valida estrutura |
| | 5. Rede salva |
| **Critério de Aceitação** | Rede com pelo menos 1 processo salva no sistema |

#### RF03 - Calcular Emergia
| Campo | Detalhe |
|-------|---------|
| **ID** | RF03 |
| **Nome** | Calcular Emergia da Rede |
| **Ator** | Analista/Usuário |
| **Pré-condições** | Rede de processos definida |
| **Pós-condições** | Resultados calculados |
| **Fluxo Principal** | 1. Usuário solicita cálculo |
| | 2. Sistema valida algebra |
| | 3. Algoritmo calcula emergia |
| | 4. Resultados armazenados |
| | 5. Resultados exibidos |
| **Fluxo Alternativo** | 2a. Inconsistência matemática → Erro específico |
| **Critério de Aceitação** | Emergia calculada para cada processo |

#### RF04 - Visualizar Fluxos
| Campo | Detalhe |
|-------|---------|
| **ID** | RF04 |
| **Nome** | Visualizar Fluxos de Emergia |
| **Ator** | Analista/Usuário |
| **Pré-condições** | Emergia calculada |
| **Pós-condições** | Resultados visualizados |
| **Fluxo Principal** | 1. Usuário solicita visualização |
| | 2. Sistema gera representação gráfica |
| | 3. Display apresentar resultados |
| **Critério de Aceitação** | Gráfico/Diagrama exibido |

#### RF05 - Gerar Relatório
| Campo | Detalhe |
|-------|---------|
| **ID** | RF05 |
| **Nome** | Gerar Relatório de Resultados |
| **Ator** | Analista/Usuário |
| **Pré-condições** | Resultados calculados |
| **Pós-condições** | Relatório gerado |
| **Fluxo Principal** | 1. Usuário solicita relatório |
| | 2. Sistema compila dados |
| | 3. Relatório exportado (PDF/CSV/JSON) |
| **Critério de Aceitação** | Arquivo gerado em formato selecionado |

#### RF06 - Calcular Indicador ELR
| Campo | Detalhe |
|-------|---------|
| **ID** | RF06 |
| **Nome** | Calcular Indicador ELR (Environmental Loading Ratio) |
| **Ator** | Analista/Usuário |
| **Pré-condições** | Resultados de emergia disponíveis |
| **Pós-condições** | ELR calculado |
| **Fluxo Principal** | 1. Usuário solicita indicador |
| | 2. Sistema calcula ELR |
| | 3. Resultado exibido |
| **Critério de Aceitação** | ELR calculado com sucesso |

---

## 5.7 RF07 - Reservado (Simulação Dinâmica Futuro)
| Campo | Detalhe |
|-------|---------|
| **ID** | RF08 |
| **Nome** | Calcular Simulação Dinâmica (Reservado) |
| **Ator** | Analista/Usuário |
| **Pré-condições** | Resultados de emergia estática disponíveis |
| **Pós-condições** | Simulação executada |
| **Fluxo Principal** | 1. Usuário configura parâmetros de simulação |
| | 2. Sistema executa simulação temporal |
| | 3. Resultados dinâmicos exibidos |
| **Critério de Aceitação** | Simulação executada com sucesso (reservado para versão futura) |

---

**OBS**: RF08 mantido como reserva para implementação futura de simulação dinâmica. Arquitetura preparada para suportar esta funcionalidade.

#### RF07 - Configurar Parâmetros
| Campo | Detalhe |
|-------|---------|
| **ID** | RF07 |
| **Nome** | Configurar Parâmetros do Sistema |
| **Ator** | Administrador/Usuário |
| **Pré-condições** | Sistema em execução |
| **Pós-condições** | Parâmetros atualizados |
| **Fluxo Principal** | 1. Usuário acessa configurações |
| | 2. Altera parâmetros |
| | 3. Sistema salva configurações |
| **Critério de Aceitação** | Novas configurações aplicadas |

---

## 6. Requisitos Não-Funcionais

### 6.1 Desempenho

| ID | Requisito | Critério |
|----|-----------|----------|
| RNF01 | Tempo de processamento | Rede com 100 processos calculada em < 5s |
| RNF02 | Memória | Uso máximo de 512MB para redes típicas |
| RNF03 | Carregamento | Arquivo LCI de 10MB carregado em < 3s |

### 6.2 Escalabilidade

| ID | Requisito | Critério |
|----|-----------|----------|
| RNF04 | Processos | Suportar mínimo 1000 processos |
| RNF05 | Fluxos | Suportar mínimo 10000 conexões |

### 6.3 Usabilidade

| ID | Requisito | Critério |
|----|-----------|----------|
| RNF06 | Interface | Interface gráfica intuitiva |
| RNF07 | Feedback | Resposta visual para todas as operações |
| RNF08 | Ajuda | Documentação de ajuda integrada |

### 6.4 Confiabilidade

| ID | Requisito | Critério |
|----|-----------|----------|
| RNF09 | Validação | Detecção de contagem dupla de energia |
| RNF10 | Consistência | Resultados consistentes em múltiplas execuções |
| RNF11 | Robustez | Tratamento adequado de erros |

### 6.5 Manutenibilidade

| ID | Requisito | Critério |
|----|-----------|----------|
| RNF12 | Modularidade | Módulos independentes intercambiáveis |
| RNF13 | Documentação | Código documentado |
| RNF14 | Cobertura | Mínimo 70% de cobertura de testes |

### 6.6 Portabilidade

| ID | Requisito | Critério |
|----|-----------|----------|
| RNF15 | SO | Windows e Linux |

---

## 7. Glossário

| Termo | Definição |
|-------|-----------|
| **Emergia** | Energia disponível de um tipo usada direta e indiretamente para fazer um produto ou serviço (Odum, 1996) |
| **Transformidade** | Razão entre emergia e energia de saída (sej/J) |
| **LCI** | Life Cycle Inventory - Inventário do Ciclo de Vida |
| **ELR** | Environmental Loading Ratio - Razão de Carga Ambiental |
| **Solar Emjoule (sej)** | Unidade padrão de emergia |
| **Rede de Processos** | Estrutura que representa processos interconectados |

---

## 10. Diagramas UML

### 10.1 Diagrama de Casos de Uso

```
+---------------------+       +------------------+       +-------------------+
|      ATOR           |       |    SISTEMA       |       |    SISTEMA        |
|    Analista        |       |   EmergyCalc     |       |   (Backend)      |
+---------------------+       +------------------+       +-------------------+
         |                            |                            |
         |  1. Seleciona arquivo    |                            |
         |-----------------------> |                            |
         |                         2. Valida JSON            |
         |<--------------------------|                            |
         |                         3. Parsing dados           |
         |<------------------------->|                           |
         |                         4. Salva no banco         |
         |<------------------------->|                           |
         |                         5. Confirmação            |
         |<--------------------------|                            |
         |                            |                            |
         |  6. Criar rede            |                            |
         |-----------------------> |                            |
         |                         7. Criar Processo         |
         |<-------------------------|                            |
         |                         8. Criar Fluxo           |
         |<-------------------------|                            |
         |                         9. Validar estrutura    |
         |<--------------------------|                            |
         |                         10. Confirmação         |
         |<--------------------------|                            |
         |                            |                            |
         |  11. Calcular emergia     |                            |
         |-----------------------> |                            |
         |                         12. Valida algebra       |
         |<-------------------------|                            |
         |                         13. Calcula emergia     |
         |<--------------------------|                            |
         |                         14. Calcula ELR          |
         |<--------------------------|                            |
         |                         15. Resultados           |
         |<--------------------------|                            |
         |                            |                            |
         |  16. Visualizar           |                            |
         |-----------------------> |                            |
         |                         17. Exibe grafico         |
         |<-------------------------|                            |
         |                            |                            |
         |  18. Gerar relatorio     |                            |
         |-----------------------> |                            |
         |                         19. Exporta PDF/JSON    |
         |<--------------------------|                            |
         |                            |                            |
         |  20. Configurar           |                            |
         |-----------------------> |                            |
         |                         21. Atualiza config      |
         |<--------------------------|                            |
+--------+                            +---------------------------+
```

**Associaciones:**
| Ator -> Sistema | Funcionalidade |
|-----------------|---------------|
| Analista | RF01, RF02, RF03, RF04, RF05, RF06, RF07 |
| Administrador | RF07 |

---

### 10.2 Diagrama de Classes (Modelo de Domínio)

```
+-----------------------+       +-----------------------+
|     TipoRecurso       |       |     TipoProcesso       |
+-----------------------+       +-----------------------+
| - id: Long           |       | <<enumeration>>       |
| - nome: String       |       | PRODUTOR              |
| - unidade: String    |       | CONSUMIDOR             |
| - categoria: String|       | ARMAZENAMENTO         |
| - transformidade: Dbl|       +-----------------------+
+-----------------------+                ^
         ^                               |
         | 1..*                      / \
        / \                           /   \
       /   \                         /     \
      /     \                       /       \
     /       \                     /         \
+---+       +--------+         +---+       +--------+
|Fluxo              |         |Processo           |
+------------------+         +-------------------+
| - id: Long       |         | - id: Long        |
| - quantidade:Dbl|<--->| - nome: String   |
| - transformidade:Dbl|         | - tipo: TipoProcesso|
| - custoEmergia: Dbl|+         | - descricao: Str  |
| - origem: Processo|         | - categoria: Str  |
| - destino: Processo         | - inputs: List<Fluxo>|
+------------------+         | - outputs: List<Fluxo>|
         ^                 +-------------------+
         | 1..*                    ^
        / \                         |
       /   \                       |
      /     \                     1..*
     /       \                   / \
+---+       +------------+      /   \
|ResultadoEmergia       |     /     \
+---------------------+     /       \
| - id: Long          |<---+         |
| - emergiaTotal: Dbl |         +---------+
| - emergiaDireta: Dbl|         |RedeProcessos    |
| - emergiaIndireta:Dbl|         +--------------+
| - transformidade:Dbl|         | - id: Long     |
| - processo: Processo         | - nome: String |
| - dataCalculo: Date          | - descricao:Str |
+---------------------+         | - processos:*  |
                              | - fluxos: List |
                              | - dataCriacao: |
                              +--------------+
```
**Relacionamentos:**
| Classe A | Relação | Classe B | Multiplicidade |
|---------|--------|---------|-------------|
| RedeProcessos | contem | Processo | 1..* |
| RedeProcessos | contem | Fluxo | 0..* |
| Processo | tem | Fluxo | 0..* (input) |
| Processo | tem | Fluxo | 0..* (output) |
| Fluxo | tem | Processo | 1 (origem) |
| Fluxo | tem | Processo | 1 (destino) |
| ResultadoEmergia | pertence | Processo | 1 |

---

### 10.3 Diagramento Classes (Camadas Backend)

```
+----------------------+     +----------------------+
|    CONTROLLER         |     |     REPOSITORY       |
+----------------------+     +----------------------+
| ProcessController    |     | ProcessRepository   |
+----------------------+     +----------------------+
| + createProcess()    |     | + save()            |
| + updateProcess()    |     | + findById()        |
| + deleteProcess()    |     | + findAll()         |
| + getAll()         |     | + delete()          |
| + getById()        |     +----------------------+
+----------------------+              ^
         ^                           |
         |                         implements
         |                          / \
         |                    +-----+ +-----+
         |                    |    JPA    |
         |                    +----------+
         |
+----------------------+     +----------------------+
|      SERVICE         |     |      ENTITY         |
+----------------------+     +----------------------+
| ProcessService      |     |  ProcessEntity     |
+----------------------+     +----------------------+
| + create()        |     | - id: Long       |
| + update()        |     | - nome: String   |
| + delete()        |     | - tipo: String  |
| + findAll()       |     | - descricao:Str |
| + calculateEmergy |     | - categoria:Str |
| + calculateELR    |     | - createdAt: Date |
| + validateAlgebra |     | - updatedAt:Date|
+----------------------+     | - version: Long |
         ^                 +---------------------+
         |                  @Entity, @Id
         |                  @GeneratedValue
         |                  @Data, @Column
         |                  @Version
         |
+----------------------+     +----------------------+
|      DTO             |     |    MAPPER         |
+----------------------+     +----------------------+
| ProcessDTO          |     |  ProcessMapper    |
+----------------------+     +----------------------+
| - id: Long          |     | + toEntity()      |
| - nome: String      |     | + toDTO()        |
| - tipo: String     |     | + toListDTO()    |
| - descricao: String|     +----------------------+
| - categoria: Str |
| - inputs: List    |
| - outputs: List  |
+----------------------+
```

---

### 10.4 Diagrama de Sequência - Calcular Emergia

```
+--------+     +--------+     +--------+     +--------+     +--------+
|Client UI|     |Controller|    |Service |     |Repository|   |Database|
+--------+     +--------+     +--------+     +--------+     +--------+
    |             |             |             |             |
    | 1.POST/calculateEmergia  |             |             |
    |------------>|             |             |             |
    |             | 2. valida dados         |             |
    |             |------------>|           |             |
    |             |             |             |             |
    |             | 3. getProcessosRede     |             |
    |             |------------>|          |             |
    |             |             | 4. SELECT *        |
    |             |             |------------->|   |
    |             |             |             | 5.OK      |
    |             |             |<------------|         |
    |             |<------------|             |             |
    |             |             |             |             |
    |             | 6. validaAlgebra       |             |
    |             |------------>|           |             |
    |             |             |             |             |
    |             | 7. calculaEmergia     |              |
    |             |<------------|           |             |
    |             |             |             |             |
    |             | 8. para cada processo |
    |             | 8.1 busca inputs     |
    |             | 8.2 soma fluxos      |
    |             | 8.3 aplica transf.  |
    |             | 8.4 repete para deps. |
    |             |             |             |             |
    |             | 9. calculaELR        |
    |             |<------------|          |
    |             |             |             |             |
    |             | 10. salvarResultado   |
    |             |------------>|        |             |
    |             |             | 11. INSERT     |
    |             |             |------------->|          |
    |             |             |             |12.OK     |
    |             |             |<------------|         |
    |             |<------------|           |             |
    |             |             |             |             |
    | 13.200 OK  |             |             |             |
    |<-----------|             |             |             |
    |             |             |             |             |
+--------+     +--------+     +--------+     +--------+     +--------+
```

---

### 10.5 Diagrama de Sequência - Importar LCI

```
+--------+     +--------+     +--------+     +--------+     +--------+
|Client UI|     |Controller|    |Service |     |Repository|   |Database|
+--------+     +--------+     +--------+     +--------+     +--------+
    |             |             |             |             |
    | 1.POST/importLCI (JSON) |             |             |
    |------------>|             |             |             |
    |             | 2. validaJSON          |
    |             |------------>|         |             |
    |             |             |             |             |
    |             | 3. parseProcessos     |
    |             |<------------|         |             |
    |             |             |             |             |
    |             | 4. parseFluxos        |
    |             |<------------|         |             |
    |             |             |             |             |
    |             | 5. criaEntidades      |
    |             |<------------|         |             |
    |             |             |             |             |
    |             | 6. validaEstrutura    |
    |             |<------------|         |
    |             |             |             |             |
    |             | 7. ERRO: estrutura   | invalid
    |             |<------------|         |             |
    |             |             |             |             |
    | 8.400 Error |<------------|         |
    |<-----------|             |             |             |
    |             |             |             |             |
    |             |             |             |             |
    |             | 9. salvarProcessos   |
    |             |------------>|        |             |
    |             |             | 10. BATCH INSERT   |
    |             |             |------------->|          |
    |             |             |             |11.OK     |
    |             |             |<------------|         |
    |             |             |             |             |
    |             | 12. salvarFluxos     |
    |             |------------>|        |             |
    |             |             | 13. BATCH INSERT   |
    |             |             |------------->|          |
    |             |             |             |14.OK      |
    |             |             |<------------|         |
    |             |<------------|           |             |
    |             |             |             |             |
    | 15.201 Created           |             |
    |<-----------|             |             |             |
    |             |             |             |             |
+--------+     +--------+     +--------+     +--------+     +--------+
```

---

### 10.6 Diagrama de Componentes

```
+------------------------------------------------------------------+
|                        APLICATIVO EMERGYCALC                        |
+------------------------------------------------------------------+
|                                                                   |
|  +-------------------+    +-------------------+                     |
|  |   FRONTEND        |    |   BACKEND         |    DATABASE       |
|  |   (Flutter)      |    |   (Spring Boot)   |    (PostgreSQL)  |
|  +-------------------+    +-------------------+    +----------+  |
|  |                   |    |                   |    |          |
|  | +-------------+  |    | +-----------+     |    | +----+  |
|  | |  Providers |  |<===>| | REST API  |     |    | | DB |  |
|  | +-------------+  |    | +-----------+     |    | +----+  |
|  |                   |    |                   |    |          |
|  | +-------------+  |    | +-----------+     |    | +----+  |
|  | |   Pages    |  |<===>| |Controllers|     |    | | PG  |  |
|  | +-------------+  |    | +-----------+     |    | +----+  |
|  |                   |    |                   |    |          |
|  | +-------------+  |    | +-----------+     |    | +----+  |
|  | |   Widgets |  |    | | Services  |     |    | +----+  |
|  | +-------------+  |    | +-----------+     |    |          |
|  |                   |    |                   |    |          |
|  | +-------------+  |    | +-----------+     |    |          |
|  | |   Models   |  |    | |Repositories|     |    |          |
|  | +-------------+  |    | +-----------+     |    |          |
|  |                   |    |                   |    |          |
|  | +-------------+  |    | +-----------+     |    |          |
|  | |   Repository|  |<===>| |  Entities |     |    |          |
|  | +-------------+  |    | +-----------+     |    |          |
|  +-------------------+    +-------------------+    +----------+  |
|                                                                   |
|  Protocolo: HTTP/REST (JSON)  |  Protocolo: JDBC       |              |
|  Porto: 8080                 |  Porto: 5432         |              |
+------------------------------------------------------------------+

+------------------------------------------------------------------+
|                         DOCKER COMPOSE                             |
+------------------------------------------------------------------+
|                                                                   |
|  +------------------+     +------------------+                      |
|  |   emergycalc     |     |   postgres      |                      |
|  |   (backend)     |     |   (database)    |                      |
|  +------------------+     +------------------+                      |
|  | porta: 8080     |     | porta: 5432     |                      |
|  | depends_on:     |     |                |                      |
|  |   postgres     |     |                |                      |
|  +------------------+     +------------------+                      |
|                                                                   |
|  +------------------+                                              |
|  |   flutter-web  | (futuro)                                    |
|  +------------------+                                              |
+------------------------------------------------------------------+
```

---

### 10.7 Diagrama de Arquitetura (Pacotes Backend)

```
br.com.emergycalc
|
+--- config
|    +--- DatabaseConfig (PostgreSQL, Hibernate)
|    +--- SecurityConfig (JWT)
|    +--- CorsConfig
|
+--- controller
|    +--- ProcessController (@RestController)
|    +--- FluxoController
|    +--- RedeProcessosController
|    +--- ResultadoController
|    +--- ImportacaoController
|    +--- RelatorioController
|
+--- service
|    +--- ProcessService (@Service)
|    +--- FluxoService
|    +--- RedeProcessosService
|    +--- EmergiaCalculationService
|    +--- ELRCalculationService
|    +--- ImportacaoLCIService
|    +--- RelatorioService
|
+--- repository
|    +--- ProcessRepository (JPA)
|    +--- FluxoRepository
|    +--- RedeProcessosRepository
|    +--- ResultadoEmergiaRepository
|    +--- TipoRecursoRepository
|
+--- entity
|    +--- ProcessEntity (@Entity)
|    +--- FluxoEntity
|    +--- RedeProcessosEntity
|    +--- ResultadoEmergiaEntity
|    +--- TipoRecursoEntity
|
+--- dto
|    +--- ProcessDTO
|    +--- FluxoDTO
|    +--- RedeProcessosDTO
|    +--- ResultadoEmergiaDTO
|    +--- ImportLCIDTO
|    +--- RelatorioDTO
|
+--- mapper
|    +--- ProcessMapper
|    +--- FluxoMapper
|    +--- RedeProcessosMapper
|
+--- exception
|    +--- GlobalExceptionHandler
|    +--- ResourceNotFoundException
|    +--- BusinessException
|    +--- ValidationException
|
+--- util
|    +--- Constants
|    +--- AlgebraEmergeticaUtils
|    +--- TransformidadeUtils
|
+--- enums
|    +--- TipoProcesso
|    +--- CategoriaRecurso
|    +--- TipoFluxo
```

---

### 10.8 Diagrama de Arquitetura (Pacotes Frontend - Flutter)

```
lib/
|
+--- main.dart
|
+--- core/
|    +--- constants/
|    |    +--- app_constants.dart
|    |    +--- api_constants.dart
|    |
|    +--- theme/
|    |    +--- app_theme.dart
|    |
|    +--- utils/
|    |    +--- formatters.dart
|    |    +--- validators.dart
|
+--- data/
|    +--- models/
|    |    +--- process_model.dart
|    |    +--- fluxo_model.dart
|    |    +--- rede_processos_model.dart
|    |    +--- resultado_model.dart
|    |
|    +--- repositories/
|    |    +--- process_repository.dart
|    |    +--- fluxo_repository.dart
|    |    +--- resultado_repository.dart
|    |
|    +--- services/
|         +--- api_service.dart
|         +--- http_client.dart
|
+--- presentation/
|    +--- providers/
|    |    +--- process_provider.dart
|    |    +--- fluxo_provider.dart
|    |    +--- resultado_provider.dart
|    |    +--- importacao_provider.dart
|    |
|    +--- pages/
|    |    +--- home_page.dart
|    |    +--- process_page.dart
|    |    +--- rede_page.dart
|    |    +--- calculo_page.dart
|    |    +--- visualizacao_page.dart
|    |    +--- relatorio_page.dart
|    |    +--- configuracao_page.dart
|    |
|    +--- widgets/
|         +--- process_card.dart
|         +--- fluxo_diagrama.dart
|         +--- grafico_barras.dart
|         +--- grafico_pizza.dart
|         +--- arquivo_upload.dart
|         +--- loading_widget.dart
|         +--- error_widget.dart
```

---

## 11. Referências

- MARVUGLIA, Antonino et al. SCALE: Software for CALculating Emergy based on Life Cycle Inventories. Ecological Modelling, v. 248, p. 80-91, 2013.
- ARBAULT, Damien et al. Emergy evaluation using the calculation software SCALE. Science of the Total Environment, v. 472, p. 608-619, 2014.
- ODUM, H. T. Environmental Accounting: Emergy and Environmental Decision Making. New York: John Wiley & Sons, 1996.
- BROWN, M. T.; ULGIATI, S. Emergy-based indices and indicators to evaluate sustainability. Ecological Modelling, v. 208, p. 329-338, 2002.
- VALYI, Raphaël; ORTEGA, Enrique. Emergy Simulator: An Open Source Simulation Platform. 2004.
- ZHAO, Yu et al. Emergy evaluation of a swamp dike-pond complex. Ecological Indicators, v. 158, p. 111481, 2024.

---

## 12. Histórico de Revisões

| Data | Versão | Descrição | Autor |
|------|--------|-----------|-------|
| 18/04/2026 | 0.1 | Versão inicial do documento | [Autor] |
| 18/04/2026 | 0.2 | Adição diagramas UML | [Autor] |
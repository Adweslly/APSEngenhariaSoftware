# APS - Engenharia de Software

Sistema computacional para cálculo de emergia em sistemas complexos, baseado em dados de Inventários de Ciclo de Vida (LCI).

## Sobre o Projeto

Este projeto foi desenvolvido como trabalho de APS (Atividade Prática Supervisionada) para cálculo de emergia, um conceito desenvolvido por H. T. Odum que permite quantificar em uma unidade comum (sej - solar emjoule) todas as formas de energia utilizadas em um processo produtivo.

## Módulos do Sistema

### 1. Dados
- **Importador LCI**: Leitura e parsing de arquivos LCI (JSON)
- **Repositório**: Armazenamento e gerenciamento de dados
- **Transformador**: Conversão de dados LCI para estruturas do sistema

### 2. Processamento
- **Algoritmo de Emergia**: Implementação da álgebra emergética e cálculo
- **Validador Algebra**: Verificação de consistência (evitar contagem dupla)
- **Indicadores**: Cálculo de EYR, ELR, ESI

### 3. Apresentação
- **Visualizador**: Display de resultados e diagramas
- **Gerador de Relatórios**: Exportação de resultados
- **Configuração**: Parametrização do sistema

## Tecnologias

| Componente | Tecnologia |
|------------|------------|
| Linguagem | Java 17+ |
| Framework | Spring Boot 3.2 |
| Build | Maven |
| Database | H2 (dev) |

## Estrutura

```
java/
├── src/main/java/com/aps/
│   ├── domain/           # Modelo de domínio
│   ├── dados/          # Camada de dados
│   ├── processamento/   # Processamento
│   ├── apresentacao/   # Apresentação
│   └── web/            # API REST
└── pom.xml
```

## Como Executar

```bash
cd java
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## API Endpoints

### Processos
- `GET /api/processos` - Listar processos
- `POST /api/processos` - Criar processo
- `GET /api/processos/{id}` - Buscar processo

### Redes
- `GET /api/redes` - Listar redes
- `POST /api/redes/{id}/calcular` - Calcular emergia
- `POST /api/redes/{id}/calcular-elr` - Calcular ELR

## Documentação

- [SPEC.md](./SPEC.md) - Especificação completa do projeto
- [plano_implementacao.md](./plano_implementacao.md) - Plano de implementação
- [relatorio_emergia.md](./relatorio_emergia.md) - Relatório de emergia

## Licência

Este projeto é para fins acadêmicos.
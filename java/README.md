# APS Engenharia de Software

Sistema computacional para cálculo de emergia em sistemas complexos, baseado em dados de Inventários de Ciclo de Vida (LCI).

## Descrição

Projeto desenvolvido como trabalho de APS (Atividade Prática Supervisionada) para cálculo de emergia, um conceito desenvolvido por H. T. Odum que permite quantificar em uma unidade comum (sej - solar emjoule) todas as formas de energia utilizadas em um processo produtivo.

## Tecnologias

- **Java** 17+
- **Spring Boot** 3.2
- **Maven**
- **H2 Database** (desenvolvimento)

## Estrutura do Projeto

```
java/src/main/java/com/aps/
├── domain/              # Modelo de domínio
│   ├── model/         # Entidades
│   └── enums/        # Enumeradores
├── dados/             # Camada de dados
│   ├── importador/   # Importação LCI
│   ├── repositorio/  # Repositório
│   └── transformador/ # Transformação de dados
├── processamento/      # Processamento
│   ├── algoritmo/   # Cálculo de emergia
│   ├── validador/   # Validação algébrica
│   └── indicadores/# Indicadores de sustentabilidade
├── apresentacao/    # Apresentação
│   ├── visualizador/# Visualização de resultados
│   ├── relatorio/  # Geração de relatórios
│   └── config/     # Configurações
└── web/             # API REST
    ├── controller/ # Controladores
    └── dto/       # Data Transfer Objects
```

## Como Executar

```bash
cd java
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## API Endpoints

### Processos
- `GET /api/processos` - Listar todos os processos
- `GET /api/processos/{id}` - Buscar processo por ID
- `POST /api/processos` - Criar novo processo
- `PUT /api/processos/{id}` - Atualizar processo
- `DELETE /api/processos/{id}` - Deletar processo

### Redes de Processos
- `GET /api/redes` - Listar todas as redes
- `GET /api/redes/{id}` - Buscar rede por ID
- `POST /api/redes` - Criar nova rede
- `POST /api/redes/{id}/calcular` - Calcular emergia da rede
- `POST /api/redes/{id}/calcular-elr` - Calcular ELR da rede
- `DELETE /api/redes/{id}` - Deletar rede

## Conceitos

### Emergia
Energia disponível de um tipo usada direta e indiretamente para fazer um produto ou serviço.

### Transformidade
Razão entre emergia e energia de saída (sej/J).

### Indicadores de Sustentabilidade
- **EYR** (Emergy Yield Ratio) - Razão de Emergia Produzida
- **ELR** (Environmental Loading Ratio) - Razão de Carga Ambiental
- **ESI** (Environmental Sustainability Index) - Índice de Sustentabilidade Ambiental

## Licença

Este projeto é para fins acadêmicos.
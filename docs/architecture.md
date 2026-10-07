# Arquitetura — API de Cotação de Seguro

## Visão geral

API REST em Java 21 + Spring Boot 3 + MongoDB que recebe dados cadastrais, produto selecionado e item segurado e retorna o preço do seguro calculado. Os produtos são pré-cadastrados (seed) no MongoDB.

## Componentes

```
┌─────────────────────────────────────────────────┐
│                Spring Boot App                  │
│                                                 │
│  Controller          Service          Repository│
│  ┌──────────┐  ┌──────────────┐  ┌─────────────┐│
│  │QuoteCtrl │→ │QuoteService  │→ │ProductRepo  ││
│  └──────────┘  │              │  └─────────────┘│
│                │PricingService│                  │
│                └──────────────┘                  │
│                                                 │
│  Config / Seed                                  │
│  ┌──────────────┐                               │
│  │DataSeeder    │ (ApplicationRunner)            │
│  └──────────────┘                               │
│                                                 │
│  Exception Handling                             │
│  ┌──────────────────────┐                       │
│  │GlobalExceptionHandler│ (@ControllerAdvice)   │
│  └──────────────────────┘                       │
└─────────────────────────────────────────────────┘
          │
          ▼
   ┌────────────┐
   │  MongoDB    │
   │ (products)  │
   └────────────┘
```

### Camadas

| Camada | Pacote | Responsabilidade |
|--------|--------|------------------|
| Controller | `com.insurance.quote.controller` | Recebe requisições HTTP, valida entrada (Bean Validation), delega ao serviço |
| Service | `com.insurance.quote.service` | Lógica de negócio: busca o produto, calcula o preço via `PricingService` |
| Repository | `com.insurance.quote.repository` | Acesso ao MongoDB via Spring Data MongoDB |
| Model | `com.insurance.quote.model` | Documentos MongoDB (`Product`) |
| DTO | `com.insurance.quote.dto` | Objetos de request/response (`QuoteRequest`, `QuoteResponse`) |
| Config | `com.insurance.quote.config` | Seed de dados (`DataSeeder`) |
| Exception | `com.insurance.quote.exception` | Handler global de exceções, exceções customizadas |

### Fluxo principal — Cotação

1. `POST /api/quotes` com JSON contendo `customer`, `productId` e `insuredItem`.
2. `QuoteController` valida o payload via Bean Validation (`@Valid`).
3. `QuoteService.quote()` busca o `Product` pelo `id` no `ProductRepository`.
4. Se não encontrado, lança `ProductNotFoundException` → HTTP 404.
5. `PricingService.calculatePrice(product)` retorna o preço com base no tipo do produto.
6. Controller retorna HTTP 200 com `QuoteResponse`.

### Cálculo de preço (PricingService)

Por ora o cálculo é direto: retorna o `basePrice` do produto. A separação em `PricingService` permite evoluir para fórmulas distintas por tipo (`COBERTURA` vs `ASSISTENCIA`) sem alterar o serviço de cotação.

### Seed de dados (DataSeeder)

Um `ApplicationRunner` que, ao iniciar, verifica se a collection `products` está vazia. Se estiver, insere os três produtos pré-cadastrados. Isso garante idempotência: reiniciar a aplicação não duplica dados.

## Mapeamento de critérios de aceitação

| AC | Componente(s) |
|----|---------------|
| AC-1 | `QuoteController`, `QuoteRequest` (DTO com validação) |
| AC-2 | `QuoteController`, `QuoteService`, `QuoteResponse` |
| AC-3 | `PricingService`, `QuoteService` |
| AC-4 | `ProductNotFoundException`, `GlobalExceptionHandler` |
| AC-5 | Bean Validation no `QuoteRequest`, `GlobalExceptionHandler` |
| AC-6 | `DataSeeder` |
| AC-7 | `Product` (documento MongoDB) |
| AC-8 | Testes JUnit 5 com `@SpringBootTest` e Embedded MongoDB ou Testcontainers |
| AC-9 | `application.properties` com `spring.data.mongodb.uri=${SPRING_DATA_MONGODB_URI}` |

## Front-end

**None: no front-end changes.** Os requisitos pedem apenas uma API REST.

## Plano de implementação

### Backend (`back-end/`)

A estrutura Maven será criada na pasta `back-end/` com o seguinte layout:

```
back-end/
├── pom.xml
├── Dockerfile
├── .env.example
└── src/
    ├── main/
    │   ├── java/com/insurance/quote/
    │   │   ├── QuoteApplication.java
    │   │   ├── controller/
    │   │   │   └── QuoteController.java
    │   │   ├── dto/
    │   │   │   ├── QuoteRequest.java
    │   │   │   ├── QuoteResponse.java
    │   │   │   ├── CustomerData.java
    │   │   │   ├── InsuredItem.java
    │   │   │   └── ErrorResponse.java
    │   │   ├── model/
    │   │   │   ├── Product.java
    │   │   │   └── ProductType.java
    │   │   ├── repository/
    │   │   │   └── ProductRepository.java
    │   │   ├── service/
    │   │   │   ├── QuoteService.java
    │   │   │   └── PricingService.java
    │   │   ├── config/
    │   │   │   └── DataSeeder.java
    │   │   └── exception/
    │   │       ├── ProductNotFoundException.java
    │   │       └── GlobalExceptionHandler.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/insurance/quote/
            ├── controller/
            │   └── QuoteControllerTest.java
            └── service/
                ├── QuoteServiceTest.java
                └── PricingServiceTest.java
```

#### Tarefas backend (em ordem)

1. **Criar `pom.xml`** com dependências: `spring-boot-starter-web`, `spring-boot-starter-data-mongodb`, `spring-boot-starter-validation`, `spring-boot-starter-test`, `de.flapdoodle.embed.mongo.spring3x` (teste).
2. **Criar `QuoteApplication.java`** — classe principal com `@SpringBootApplication`.
3. **Criar modelo `Product`** — documento MongoDB com campos `id`, `name`, `type` (enum `ProductType`), `basePrice`.
4. **Criar enum `ProductType`** — valores `COBERTURA`, `ASSISTENCIA`.
5. **Criar `ProductRepository`** — interface Spring Data MongoDB.
6. **Criar DTOs** — `CustomerData`, `InsuredItem`, `QuoteRequest`, `QuoteResponse`, `ErrorResponse` com anotações de validação.
7. **Criar `PricingService`** — cálculo de preço baseado no tipo do produto.
8. **Criar `QuoteService`** — orquestra busca do produto e cálculo de preço.
9. **Criar `QuoteController`** — endpoint `POST /api/quotes`.
10. **Criar `GlobalExceptionHandler`** — tratamento de `ProductNotFoundException` e `MethodArgumentNotValidException`.
11. **Criar `DataSeeder`** — seed dos três produtos.
12. **Criar `application.properties`** — configuração do MongoDB URI e porta.
13. **Criar `.env.example`** — variáveis de ambiente documentadas.
14. **Criar `Dockerfile`** — multi-stage build com Maven + JDK 21, `HEALTHCHECK`.
15. **Escrever testes** — `QuoteControllerTest` (integração), `QuoteServiceTest`, `PricingServiceTest` (unitários).

### Frontend (`front-end/`)

Nenhuma tarefa. Não há front-end neste projeto.

## Como executar

### Variáveis de ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `SERVER_PORT` | Porta HTTP do servidor | `8080` |
| `SPRING_DATA_MONGODB_URI` | URI de conexão ao MongoDB | `mongodb://localhost:27017/insurance` |

### Testes

```bash
mvn test
```

Os testes usam Embedded MongoDB (flapdoodle) para não depender de um MongoDB externo.

### Build

```bash
mvn -DskipTests package
```

Gera o JAR em `back-end/target/`.

### Executar

```bash
# Com MongoDB local
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/insurance java -jar back-end/target/quote-*.jar

# Com Docker Compose (gerado pela Squad)
docker compose up -d
```

### Lint

O projeto usa as regras padrão do compilador Java (sem linter adicional). Os testes validam o comportamento.

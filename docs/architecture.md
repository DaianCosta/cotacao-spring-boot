# Arquitetura — task-007: Responder 404/405 e Swagger

## Visão geral

Tarefa de correção no tratamento de erros HTTP e adição de documentação Swagger/OpenAPI à API de cotação de seguro.
Não há alteração no modelo de dados, na lógica de negócio nem criação de front-end.

## Front-end

**None: no front-end changes.** O projeto não possui front-end; a tarefa é exclusivamente back-end.

## Componentes afetados

### 1. GlobalExceptionHandler (correção)

**Arquivo**: `back-end/src/main/java/com/insurance/quote/exception/GlobalExceptionHandler.java`

O handler atual captura `Exception.class` e retorna 500 para tudo que não seja `ProductNotFoundException` ou `MethodArgumentNotValidException`. Isso inclui exceções do Spring MVC que deveriam resultar em 404 ou 405.

**Mudança**: adicionar dois novos métodos `@ExceptionHandler`:

- `handleNoHandlerFound(NoHandlerFoundException ex)` → retorna 404 com `ErrorResponse(404, "Not Found", mensagem descritiva)`.
- `handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex)` → retorna 405 com `ErrorResponse(405, "Method Not Allowed", mensagem descritiva)`.

O handler genérico de `Exception.class` permanece inalterado para erros realmente inesperados.

**Satisfaz**: AC-1, AC-2, AC-3, AC-4, AC-5, AC-6.

### 2. Configuração do Spring MVC (nova propriedade)

**Arquivo**: `back-end/src/main/resources/application.properties`

Adicionar:
```properties
spring.mvc.throw-exception-if-no-handler-found=true
spring.web.resources.add-mappings=false
```

A primeira propriedade faz o Spring lançar `NoHandlerFoundException` em vez de retornar a página de erro padrão.
A segunda desabilita o mapeamento automático de recursos estáticos (que capturaria a requisição antes do handler).

**Nota**: `spring.web.resources.add-mappings=false` é seguro porque esta API não serve recursos estáticos. O Swagger UI
do springdoc usa seu próprio servlet e não depende do mapeamento de recursos estáticos do Spring.

**Satisfaz**: AC-1, AC-4.

### 3. Dependência springdoc-openapi (nova)

**Arquivo**: `back-end/pom.xml`

Adicionar a dependência `springdoc-openapi-starter-webmvc-ui` (versão 2.6.0), que é compatível com Spring Boot 3.3.x
e Java 21. Ela provê:

- Swagger UI em `/swagger-ui.html` (redireciona para `/swagger-ui/index.html`)
- Especificação OpenAPI em `/v3/api-docs`

Nenhuma configuração adicional é necessária; o springdoc faz scan automático dos controllers.

**Satisfaz**: AC-7.

### 4. Testes (novos e existentes)

**Arquivo**: `back-end/src/test/java/com/insurance/quote/controller/QuoteControllerTest.java`

Adicionar testes ao arquivo existente:

- `returns404_whenRouteNotFound` — `GET /` → 404 com corpo JSON padronizado.
- `returns404_whenArbitraryRouteNotFound` — `GET /caminho/qualquer/inexistente` → 404.
- `returns405_whenMethodNotAllowed_GET` — `GET /api/quotes` → 405 com corpo JSON padronizado.
- `returns405_whenMethodNotAllowed_DELETE` — `DELETE /api/quotes` → 405.

**Satisfaz**: AC-8.

## Mapeamento AC → Componentes

| AC   | Componente(s)                                       |
|------|-----------------------------------------------------|
| AC-1 | GlobalExceptionHandler + application.properties     |
| AC-2 | GlobalExceptionHandler                              |
| AC-3 | GlobalExceptionHandler                              |
| AC-4 | GlobalExceptionHandler + application.properties     |
| AC-5 | GlobalExceptionHandler (usa `ErrorResponse` existente) |
| AC-6 | GlobalExceptionHandler (handler genérico inalterado)|
| AC-7 | pom.xml (springdoc-openapi)                         |
| AC-8 | QuoteControllerTest                                 |

## Plano de implementação

### Backend

Todos os arquivos estão na pasta `back-end/`. A implementação é disjunta e pode ser feita em qualquer ordem.

| Passo | Arquivo | Ação |
|-------|---------|------|
| 1 | `pom.xml` | Adicionar dependência `springdoc-openapi-starter-webmvc-ui:2.6.0` |
| 2 | `src/main/resources/application.properties` | Adicionar `spring.mvc.throw-exception-if-no-handler-found=true` e `spring.web.resources.add-mappings=false` |
| 3 | `src/main/java/.../exception/GlobalExceptionHandler.java` | Adicionar handlers para `NoHandlerFoundException` e `HttpRequestMethodNotSupportedException` |
| 4 | `src/test/java/.../controller/QuoteControllerTest.java` | Adicionar 4 testes para 404 e 405 |

### Frontend

Não há trabalho de front-end nesta tarefa.

## Como executar

### Testes
```bash
cd back-end && mvn test
```

### Build
```bash
cd back-end && mvn -DskipTests package
```

### Variáveis de ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `SERVER_PORT` | Porta do servidor | `8080` |
| `SPRING_DATA_MONGODB_URI` | URI de conexão MongoDB | `mongodb://localhost:27017/insurance` |

### Iniciar o sistema

```bash
cd back-end && java -jar target/quote-*.jar
```

Ou via Docker:
```bash
cd back-end && docker compose up -d
```

### Swagger UI

Após iniciar, acessar: `http://localhost:8080/swagger-ui.html`

# Contrato de API — task-007

## Base URL

`http://localhost:8080`

## Formato de erro padrão

Todas as respostas de erro usam o record `ErrorResponse`:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Nenhum endpoint encontrado para GET /caminho",
  "details": null
}
```

O campo `details` é omitido do JSON quando `null` (via `@JsonInclude(NON_NULL)`).

---

## Endpoints existentes (sem alteração)

### POST /api/quotes — Criar cotação de seguro

**Request**:
```json
{
  "customer": {
    "name": "João da Silva",
    "document": "123.456.789-00"
  },
  "productId": "abc123",
  "insuredItem": {
    "description": "Apartamento 3 quartos"
  }
}
```

**Respostas**:

| Status | Descrição | Corpo |
|--------|-----------|-------|
| 200 | Cotação criada | `QuoteResponse` |
| 400 | Validação falhou | `ErrorResponse` com `details: [...]` |
| 404 | Produto não encontrado | `ErrorResponse` |
| 500 | Erro interno inesperado | `ErrorResponse` |

**Response 200**:
```json
{
  "productId": "abc123",
  "productName": "RESIDENCIAL",
  "productType": "COBERTURA",
  "price": 300.00,
  "customerName": "João da Silva"
}
```

---

## Comportamentos alterados por esta tarefa

### Rota inexistente → 404 Not Found

Qualquer requisição a um caminho não mapeado retorna:

```
GET / HTTP/1.1
→ 404 Not Found
```

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Nenhum endpoint encontrado para GET /"
}
```

**Exemplos de rotas que retornam 404**:
- `GET /`
- `GET /foo`
- `GET /caminho/qualquer/inexistente`
- `POST /api/nonexistent`

### Método não permitido → 405 Method Not Allowed

Requisição com método HTTP não suportado em rota existente:

```
GET /api/quotes HTTP/1.1
→ 405 Method Not Allowed
```

```json
{
  "status": 405,
  "error": "Method Not Allowed",
  "message": "Método GET não é permitido para este endpoint. Métodos suportados: [POST]"
}
```

```
DELETE /api/quotes HTTP/1.1
→ 405 Method Not Allowed
```

```json
{
  "status": 405,
  "error": "Method Not Allowed",
  "message": "Método DELETE não é permitido para este endpoint. Métodos suportados: [POST]"
}
```

---

## Novos endpoints (Swagger/OpenAPI)

### GET /swagger-ui.html — Swagger UI (redirecionamento)

Redireciona para `/swagger-ui/index.html`. Abre a interface interativa da documentação.

| Status | Descrição |
|--------|-----------|
| 302 | Redirecionamento para `/swagger-ui/index.html` |

### GET /swagger-ui/index.html — Swagger UI

Página HTML interativa de documentação da API.

| Status | Descrição |
|--------|-----------|
| 200 | Página HTML do Swagger UI |

### GET /v3/api-docs — Especificação OpenAPI (JSON)

Retorna a especificação OpenAPI 3.0 da API em formato JSON.

| Status | Descrição |
|--------|-----------|
| 200 | Especificação OpenAPI em JSON |

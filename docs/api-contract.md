# Contrato de API — API de Cotação de Seguro

Base URL: `/api`

## Endpoints

### POST /api/quotes

Calcula o preço de uma cotação de seguro com base no produto selecionado.

#### Request

**Content-Type:** `application/json`

```json
{
  "customer": {
    "name": "João da Silva",
    "document": "123.456.789-00"
  },
  "productId": "6715a1b2c3d4e5f6a7b8c9d0",
  "insuredItem": {
    "description": "Apartamento 3 quartos, Bairro Centro"
  }
}
```

**Campos:**

| Campo | Tipo | Obrigatório | Validação | Descrição |
|-------|------|-------------|-----------|-----------|
| `customer` | `object` | Sim | `@NotNull` | Dados cadastrais do cliente |
| `customer.name` | `string` | Sim | `@NotBlank` | Nome completo do cliente |
| `customer.document` | `string` | Sim | `@NotBlank` | Documento do cliente (CPF ou CNPJ) |
| `productId` | `string` | Sim | `@NotBlank` | ID do produto na collection `products` (ObjectId como string hex) |
| `insuredItem` | `object` | Sim | `@NotNull` | Dados do item segurado |
| `insuredItem.description` | `string` | Sim | `@NotBlank` | Descrição do item segurado |

#### Responses

##### 200 OK — Cotação calculada com sucesso

```json
{
  "productId": "6715a1b2c3d4e5f6a7b8c9d0",
  "productName": "RESIDENCIAL",
  "productType": "COBERTURA",
  "price": 300.00,
  "customerName": "João da Silva"
}
```

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `productId` | `string` | ID do produto utilizado |
| `productName` | `string` | Nome do produto |
| `productType` | `string` | Tipo do produto (`COBERTURA` ou `ASSISTENCIA`) |
| `price` | `number` | Preço calculado do seguro (em reais, duas casas decimais) |
| `customerName` | `string` | Nome do cliente (eco para confirmação) |

##### 400 Bad Request — Payload inválido

Retornado quando campos obrigatórios estão ausentes ou em formato incorreto.

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação",
  "details": [
    "customer.name: must not be blank",
    "productId: must not be blank"
  ]
}
```

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `status` | `integer` | Código HTTP |
| `error` | `string` | Nome do erro HTTP |
| `message` | `string` | Mensagem geral |
| `details` | `string[]` | Lista de erros de validação por campo |

##### 404 Not Found — Produto não encontrado

Retornado quando o `productId` informado não corresponde a nenhum produto na collection.

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Produto não encontrado com o ID: 6715a1b2c3d4e5f6a7b8c9d0"
}
```

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `status` | `integer` | Código HTTP |
| `error` | `string` | Nome do erro HTTP |
| `message` | `string` | Mensagem descritiva do erro |

##### 500 Internal Server Error — Erro inesperado

```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Erro interno do servidor"
}
```

## Formato de erro padrão

Todas as respostas de erro seguem o mesmo formato:

```json
{
  "status": <int>,
  "error": "<string>",
  "message": "<string>",
  "details": ["<string>"]  // opcional, presente apenas em erros de validação
}
```

## Notas

- Não há paginação, filtro ou ordenação — há apenas um endpoint de cotação.
- Não há autenticação — fora de escopo.
- O `productId` é o `_id` do MongoDB representado como string hexadecimal de 24 caracteres.
- O preço é retornado como `number` (ponto flutuante com até duas casas decimais).
- O `Content-Type` de todas as respostas é `application/json`.

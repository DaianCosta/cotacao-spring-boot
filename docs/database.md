# Modelo de Dados — MongoDB

## Collection: `products`

Armazena os produtos de seguro pré-cadastrados. Populada pelo seed ao iniciar a aplicação (se estiver vazia).

### Campos

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| `_id` | `ObjectId` | Sim (auto) | Identificador único gerado pelo MongoDB |
| `name` | `String` | Sim | Nome do produto (ex.: "VIDA") |
| `type` | `String` | Sim | Tipo do produto: `"COBERTURA"` ou `"ASSISTENCIA"` |
| `basePrice` | `Double` | Sim | Preço base em reais (ex.: 150.00) |

### Índices

| Nome | Campos | Tipo | Justificativa |
|------|--------|------|---------------|
| `_id_` | `_id` | Único (padrão) | Chave primária, usado nas buscas por produto |
| `idx_name` | `name` | Único | Garante que não haja produtos duplicados por nome |

### Documentos de exemplo (seed)

```json
[
  {
    "_id": ObjectId("..."),
    "name": "VIDA",
    "type": "COBERTURA",
    "basePrice": 150.00
  },
  {
    "_id": ObjectId("..."),
    "name": "RESIDENCIAL",
    "type": "COBERTURA",
    "basePrice": 300.00
  },
  {
    "_id": ObjectId("..."),
    "name": "ASSISTÊNCIA MORADIA",
    "type": "ASSISTENCIA",
    "basePrice": 30.00
  }
]
```

## Observações

- O campo `type` é armazenado como `String` (não como subdocumento ou referência). Os valores válidos são controlados pelo enum `ProductType` no Java.
- O `_id` é `ObjectId` gerado automaticamente pelo MongoDB. A API recebe e retorna o `id` como `String` (representação hexadecimal do `ObjectId`).
- O seed é idempotente: só insere quando a collection está vazia. Não há migration destrutiva.
- Não há collection de cotações — as cotações não são persistidas (fora de escopo).

## Plano de migração

Não há migração. O banco é criado automaticamente pelo MongoDB na primeira conexão. Os dados iniciais são inseridos pelo `DataSeeder` (ApplicationRunner) na inicialização da aplicação.

Fluxo:
1. Aplicação inicia e conecta ao MongoDB via `SPRING_DATA_MONGODB_URI`.
2. `DataSeeder` verifica se `products` está vazia (`count == 0`).
3. Se vazia, insere os três documentos de seed.
4. Cria o índice único em `name` via anotação `@Indexed(unique = true)` no modelo `Product`.

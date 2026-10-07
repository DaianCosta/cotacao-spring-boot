# Modelo de Dados — task-007

## Sem alterações

Esta tarefa não altera o modelo de dados. As collections existentes no MongoDB permanecem inalteradas.

### Collection existente: `products`

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `_id` | `String` | Identificador único do produto |
| `name` | `String` | Nome do produto (ex.: "RESIDENCIAL") |
| `type` | `String` | Tipo do produto (enum: `COBERTURA`, `ASSISTENCIA`) |
| `basePrice` | `Double` | Preço base do produto |

**Índices**: `_id` (padrão do MongoDB).

### Documento exemplo

```json
{
  "_id": "abc123",
  "name": "RESIDENCIAL",
  "type": "COBERTURA",
  "basePrice": 250.00
}
```

Nenhuma migration é necessária.

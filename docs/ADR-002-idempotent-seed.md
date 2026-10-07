# ADR-002 — Seed idempotente via ApplicationRunner

## Contexto

AC-6 exige que a collection `products` contenha exatamente os três produtos ao iniciar. Precisamos de um mecanismo de carga inicial que não duplique dados em reinicializações.

## Decisão

Usar um `ApplicationRunner` (`DataSeeder`) que verifica se a collection está vazia (`count == 0`). Se estiver, insere os três produtos. Caso contrário, não faz nada.

## Alternativas consideradas

1. **Mongock / Liquibase for MongoDB**: framework de migrations para MongoDB. Excessivo para três documentos estáticos; adiciona dependência e complexidade de configuração desnecessárias.
2. **Script externo de seed**: requer execução manual ou integração com CI; menos conveniente para desenvolvimento local.
3. **Upsert por nome**: mais robusto para atualizações parciais, mas adiciona complexidade sem benefício claro — os produtos são fixos e não mudam.

## Consequências

- **Positivo**: simples, automático, sem dependências extras. Idempotente para o caso de uso (reinicializações).
- **Negativo**: se os produtos forem alterados manualmente no banco, a aplicação não os recriará (a collection não estará vazia). Aceitável dado o escopo.

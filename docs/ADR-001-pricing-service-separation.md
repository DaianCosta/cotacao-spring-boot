# ADR-001 — Separação do cálculo de preço em PricingService

## Contexto

O requisito AC-3 define que o preço é calculado com base no tipo do produto. Atualmente o cálculo é simples (retorna o preço base), mas o pedido menciona "por enquanto", indicando que regras mais complexas podem surgir (ex.: fórmulas diferentes para COBERTURA vs ASSISTENCIA, fatores de risco baseados no item segurado).

## Decisão

Isolar o cálculo de preço em um `PricingService` separado do `QuoteService`. O `QuoteService` orquestra (busca produto, chama pricing, monta resposta); o `PricingService` contém apenas a lógica de cálculo.

## Alternativas consideradas

1. **Cálculo inline no `QuoteService`**: mais simples, mas mistura orquestração com regra de negócio. Quando novas fórmulas surgirem, o `QuoteService` cresceria com responsabilidades distintas.
2. **Strategy pattern com uma classe por tipo**: excessivo para dois tipos e uma fórmula trivial. Pode ser adotado futuramente se o número de tipos crescer.

## Consequências

- **Positivo**: facilita a evolução do cálculo sem alterar o fluxo de cotação; facilita testes unitários isolados do pricing.
- **Negativo**: uma classe a mais no projeto, marginalmente mais complexo para o cenário atual.

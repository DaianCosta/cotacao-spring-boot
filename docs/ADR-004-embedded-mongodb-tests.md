# ADR-004 — Embedded MongoDB para testes

## Contexto

AC-8 exige testes automatizados. Os testes de integração precisam de um MongoDB. No ambiente de CI/container da Squad, não há garantia de um MongoDB externo disponível durante `mvn test`.

## Decisão

Usar a biblioteca `de.flapdoodle.embed.mongo` (via `de.flapdoodle.embed.mongo.spring3x`) para testes. Ela baixa e executa um MongoDB embutido durante a execução dos testes, sem necessidade de infraestrutura externa.

## Alternativas consideradas

1. **Testcontainers**: mais fiel ao ambiente real (Docker), mas exige Docker disponível no ambiente de teste. O container de build da Squad já é Docker — Docker-in-Docker adiciona complexidade.
2. **Mock do Repository**: rápido, mas não valida a integração real com o MongoDB (queries, índices, seed).

## Consequências

- **Positivo**: testes autossuficientes; rodam em qualquer ambiente com JDK; validam a integração real com o MongoDB.
- **Negativo**: primeira execução baixa o binário do MongoDB (~100 MB, cacheado depois); testes são mais lentos que mocks puros. Aceitável para o escopo.

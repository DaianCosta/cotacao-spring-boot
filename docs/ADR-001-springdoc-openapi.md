# ADR-001: Usar springdoc-openapi para Swagger UI

## Contexto

A tarefa pede documentação interativa da API via Swagger. No ecossistema Spring Boot 3.x existem duas opções principais:

1. **springdoc-openapi** (`springdoc-openapi-starter-webmvc-ui`): mantido ativamente, compatível com Spring Boot 3.x, baseado em OpenAPI 3.0.
2. **springfox**: abandonado desde 2020, incompatível com Spring Boot 3.x.

## Decisão

Usar `springdoc-openapi-starter-webmvc-ui` versão 2.6.0.

## Alternativas consideradas

- **springfox**: descartado por incompatibilidade com Spring Boot 3.x e falta de manutenção.
- **Geração manual de OpenAPI YAML**: descartado por adicionar trabalho manual desnecessário; o springdoc gera automaticamente a partir dos controllers.

## Consequências

- Swagger UI disponível em `/swagger-ui.html` sem configuração adicional.
- A especificação OpenAPI é gerada automaticamente a partir das anotações dos controllers.
- Uma dependência nova é adicionada ao projeto (`~3 MB`).
- O springdoc serve seus recursos estáticos por servlet próprio, independente do mapeamento de recursos do Spring MVC.

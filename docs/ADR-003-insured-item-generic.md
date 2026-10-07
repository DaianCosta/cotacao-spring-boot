# ADR-003 — Item segurado como objeto genérico com descrição

## Contexto

O pedido menciona "item segurado" sem especificar quais atributos o descrevem. Cada tipo de produto poderia exigir dados diferentes (ex.: VIDA → idade, RESIDENCIAL → endereço). A ambiguidade 2 do documento de requisitos reconhece isso e aceita um objeto genérico.

## Decisão

O `insuredItem` terá apenas um campo obrigatório `description` (string livre). Isso satisfaz AC-1 sem inventar campos de domínio que os requisitos não pedem.

## Alternativas consideradas

1. **Campos tipados por produto** (ex.: `age`, `address`): mais rico semanticamente, mas fora do escopo. Exigiria validação condicional por tipo de produto.
2. **Map<String, Object> completamente livre**: flexível, mas impossibilita validação mínima e documentação do contrato.

## Consequências

- **Positivo**: contrato simples e estável; evita decisões de domínio que deveriam vir do negócio.
- **Negativo**: o campo `description` não captura dados estruturados do item. Aceitável — o requisito diz explicitamente que isso está fora do escopo atual.

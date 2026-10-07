# ADR-002: Tratar 404 e 405 via @ExceptionHandler em vez de ErrorController

## Contexto

O Spring MVC oferece duas abordagens para tratar erros de rota inexistente e método não permitido:

1. **@ExceptionHandler no @RestControllerAdvice**: capturar `NoHandlerFoundException` e `HttpRequestMethodNotSupportedException` diretamente no `GlobalExceptionHandler`.
2. **Implementar `ErrorController`**: substituir o `/error` padrão do Spring Boot com um controller customizado que inspeciona os atributos de erro.

## Decisão

Usar `@ExceptionHandler` no `GlobalExceptionHandler` existente, combinado com `spring.mvc.throw-exception-if-no-handler-found=true` e `spring.web.resources.add-mappings=false`.

## Alternativas consideradas

- **ErrorController**: mais complexo, requer parsing de atributos do request, e dificulta testes unitários com `MockMvc.standaloneSetup`. Além disso, não segue o padrão já estabelecido no projeto.
- **Estender `ResponseEntityExceptionHandler`**: o `GlobalExceptionHandler` poderia estender essa classe base do Spring. Embora funcione, mudaria a hierarquia da classe existente e poderia afetar o tratamento de outros erros de forma inesperada. Mantemos o handler explícito, que é mais transparente.

## Consequências

- Consistência com o padrão existente: todos os erros são tratados no mesmo `@RestControllerAdvice`.
- A propriedade `spring.web.resources.add-mappings=false` desabilita o mapeamento de recursos estáticos do Spring MVC. Isso é seguro porque a API não serve recursos estáticos e o springdoc usa servlet próprio.
- Os testes existentes com `MockMvc.standaloneSetup` continuam funcionando sem mudança na configuração do MockMvc.

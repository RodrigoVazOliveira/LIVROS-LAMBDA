## Biblioteca

### Objetivo:

    Criar uma POC para fazer um microserviço com ApiGateway usando
    com dados não relacional. Usei como base de contexto uma biblioteca
    para armazenar livros com tipo de livro e genero e o livro propiamente dito.
    A POC também foi desenvolvido usando conceito de TDD.

    Abaixo maiores detalhoes

---
### Tecnologia utilizadas:
    - Java 8
    - Serverless Framework
    - AWS Lambda
    - AWS DynamoDB
    - GraphQL
    - AWS ApiGateway

---

### Funcinalidades

    1. Geneero de livro
        - Gravar um novo genero
        - Atualizar um genero existente
        - Listar todos generos cadastrados
        - Deleta um genero pelo id
    2. Tipo de livro
        - Gravar um novo tipo de livro
        - Atualizar um tipo de livro existente
        - Listar todos tipo de livro cadastrados
        - Deleta um tipo de livro pelo id
    3. Livro
        - Gravar um novo livro
        - Atualizar um livro existente
        - Listar todos livro cadastrados
        - Deleta um livro pelo id
    
### TABELA DE ENDPOINT

 HTTP METHOD | ENDPOINT | BODY | FUNÇÃO 
 --------|-----|---|---|
POST | /generos | SIM | Gravar um novo genero
PUT | /generos | SIM | Atualizar um genero existente
GET | /generos | NÃO | Listar todos generos cadastrados
DELETE | /generos | SIM | Deleta um genero pelo id
POST | /tipodelivros | SIM | Gravar um novo tipo de livro
PUT | /tipodelivros | SIM | Atualizar um tipo de livro existente
GET | /tipodelivros | NÃO | Listar todos tipo de livro cadastrados
DELETE | /tipodelivros | SIM | Deleta um tipo de livro pelo id
POST | /tipodelivros | SIM | Gravar um novo livro
PUT | /tipodelivros | SIM | Atualizar um livro existente
GET | /tipodelivros | NÃO | Listar todos livro cadastrados
DELETE | /tipodelivros | SIM | Deleta um livro pelo id

---

## Criar um projeto com aws-lambda 

Com o comando abaixo vai ser criado um projeto padrão para criar a lambda
com framework Serverless Framework (https://www.serverless.com/)

 ~~~bash
  $ serverless create --template aws-java-maven --name nomedoseuservico
 ~~~

Após a implementação, configuração das credencias e região da AWS para fazer o 
deploy basta:

~~~bash
$ serverless deploy
~~~

Para testar sua lambda localmente:

~~~bash
$ serverless invoke local -f nomedasuafuncao 
~~~
# posto-api

Este projeto tem como foco me aprofundar no desenvolvimento de APIs, consiste em uma api básica para um posto de gasolina ter controle de transações efetuadas no dia a dia mostrando dados como data da transação, valor e quanto de gasolina foi abastecida.

## tecnologias:

Para este projeto usei as seguintes tecnologias:

- Kotlin
- Ktor - Para trabalhar com requisições HTTP.


Para salvar os dados foi utilizado:

- MySQL 
- Exposed - ORM para lidar com queries SQL de maneira mais dinâmica.
- Flyway - Para criar as migrações do banco de dados caso seja necessário alguma alteração, ajudando no versionamento do mesmo.

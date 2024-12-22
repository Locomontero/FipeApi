# FipeApi - Projeto Java Backend com Quarkus, Maven, Docker, PostgreSQL, Kafka, H2, Hibernate, JUnit, Panache, Lombok, Jackson e Mappers

Este projeto é um backend desenvolvido em **Java** utilizando o framework **Quarkus**, com integração com **Kafka** para comunicação assíncrona, **PostgreSQL** para persistência de dados, **H2** para testes em memória, e **FIPE API** para consulta de informações sobre veículos. Utiliza ainda **Lombok** para redução de código boilerplate e **Jackson** para serialização/deserialização de JSON. **Mappers** são usados para converter dados entre DTOs e Entidades.

### Estrutura do Projeto

- **API1**: Responsável por enviar requisições para a fila Kafka com dados de veículos.
- **API2**: Consome as mensagens da fila Kafka, realiza chamadas para o serviço FIPE e processa as respostas.
- **Kafka**: Filas de mensagens que realizam a comunicação assíncrona entre a API1 e API2.
- **PostgreSQL**: Banco de dados utilizado para armazenar informações dos veículos e resultados da consulta FIPE.
- **H2**: Banco de dados em memória utilizado para testes.
- **Quarkus**: Framework para criar APIs RESTful com foco em performance e desenvolvimento ágil.
- **Hibernate (Panache)**: ORM (Object-Relational Mapping) para facilitar o mapeamento entre objetos Java e tabelas do banco de dados.
- **Lombok**: Biblioteca que reduz a quantidade de código boilerplate (como getters, setters e construtores).
- **Jackson**: Biblioteca para manipulação de JSON, sendo utilizada para serialização e deserialização de dados.
- **JUnit**: Framework para testes unitários.
- **Mappers**: Conversão de dados entre DTOs e entidades, implementado manualmente ou com o auxílio de bibliotecas como MapStruct.

### Tecnologias Utilizadas

- **Quarkus**: Framework Java moderno, otimizado para desenvolvimento rápido de microserviços e APIs RESTful.
- **Maven**: Gerenciador de dependências e ferramenta de build.
- **Docker**: Usado para containerizar o projeto, facilitando a execução em diferentes ambientes.
- **PostgreSQL**: Banco de dados relacional utilizado para armazenar dados de veículos e informações sobre consultas à API FIPE.
- **Kafka**: Sistema de mensagens utilizado para a comunicação assíncrona entre as APIs.
- **FIPE API**: Serviço externo que fornece informações sobre veículos no Brasil, como marca, modelo e preço.
- **H2**: Banco de dados em memória utilizado para realizar testes unitários e de integração de forma isolada e eficiente.
- **Hibernate (Panache)**: ORM utilizado para interação com o banco de dados, facilitando a persistência e recuperação de dados de forma simples e eficaz.
- **Lombok**: Biblioteca que gera automaticamente código como getters, setters, construtores, entre outros, reduzindo a quantidade de código repetitivo.
- **Jackson**: Biblioteca para serialização e desserialização de objetos Java para JSON e vice-versa.
- **JUnit**: Framework utilizado para realizar testes unitários e de integração.
- **MapStruct**: Biblioteca (opcional) para mapeamento entre objetos DTO e entidades, proporcionando mapeamentos eficientes e sem necessidade de código manual.

### Como Rodar o Projeto

#### 1. Pré-requisitos

- **Docker**: Certifique-se de ter o [Docker](https://www.docker.com/get-started) instalado em sua máquina.
- **Java 17+**: O Quarkus é otimizado para Java 17 ou superior. Caso não tenha o JDK 17 ou superior, faça o download [aqui](https://adoptopenjdk.net/).
- **Maven**: Utilizado para gerenciamento de dependências e build. Caso não tenha, siga as instruções para instalação [aqui](https://maven.apache.org/install.html).

#### 2. Clonar o Repositório

```bash
git clone https://github.com/locomontero/fipeapi
cd fipeapi

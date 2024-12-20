# FipeApi
# Projeto Java Backend com Quarkus, Maven, Docker, PostgreSQL e Kafka

Este projeto é um backend desenvolvido em **Java** utilizando **Quarkus**, **Maven**, **Docker**, **PostgreSQL** e **Kafka** para comunicação assíncrona entre duas APIs (API1 e API2). O serviço é integrado com o sistema FIPE, fornecendo dados sobre veículos através de uma API externa.

## Estrutura do Projeto

- **API1**: Responsável por enviar requisições para a fila Kafka com dados de veículos.
- **API2**: Consome as mensagens da fila Kafka, realiza chamadas para o serviço FIPE e processa as respostas.
- **Kafka**: Filas de mensagens que realizam a comunicação assíncrona entre a API1 e API2.
- **PostgreSQL**: Banco de dados utilizado para armazenar informações dos veículos e resultados da consulta FIPE.
- **Quarkus**: Framework para criar APIs RESTful com foco em performance e desenvolvimento ágil.
- **Docker**: Utilizado para containerizar o projeto e facilitar o ambiente de desenvolvimento e produção.

## Tecnologias Utilizadas

- **Quarkus** - Framework Java moderno para aplicações nativas.
- **Maven** - Gerenciador de dependências e build tool.
- **Docker** - Containerização para facilitar a execução do projeto em diferentes ambientes.
- **PostgreSQL** - Banco de dados relacional para persistência de dados.
- **Kafka** - Sistema de mensagens para comunicação assíncrona entre APIs.
- **FIPE API** - Utiliza um serviço de api externo  (https://deividfortuna.github.io/fipe/) para consumir dados dos veículos.

## Como Rodar o Projeto

### 1. Pré-requisitos

- **Docker**: Certifique-se de ter o Docker instalado em sua máquina.
- **Java 17+**: O Quarkus é otimizado para Java 17 ou superior.
- **Maven**: Utilizado para gerenciamento de dependências e build.

### 2. Clonar o Repositório

```bash
git clone https://github.com/locomontero/fipeapi
Ou clicar em clone se deseja utilizar ssh

# FipeApi
Api backend Java de Busca/Informações de Veículos
Implementação de filas para processamento
assíncrono e gerenciar dados em banco SQL com o  objetivo de criar soluções eficientes e bem
estruturadas para um cenário de integração com o serviço FIPE.
1.1 Serviço REST na API-1 para acionar a “carga inicial” dos dados de veículos.
1.2 Implementação da lógica na API-1 para buscar as “marcas” no serviço da FIPE
(https://deividfortuna.github.io/fipe/).
1.3 Configuração de uma “fila” para receber as “marcas” da API-1 e enviar uma por vez para a API-2
para processamento assíncrono.
1.4 Implementação a lógica na API-2 para buscar os “códigos” e “modelos” dos veículos no serviço
da FIPE com base nas “marcas” recebidas da fila.
1.5 Implementação da lógica na API-2 para salvar no banco de dados “SQL” as informações
de "código", "marca" e "modelo" dos veículos encontrados no serviço da FIPE.
1.6 Criação um serviço REST na API-1 para buscar as "marcas" armazenadas no banco de dados.
1.7 Criação de um serviço REST na API-1 para buscar os "códigos", "modelos" e “observações” dos
veículos por "marca" no banco de dados.
1.8 Criação um serviço REST na API-1 para salvar os dados alterados do veículo, como: "modelo" e
“observações” no banco de dados.
Tecnologia Utilizada: Java, Quarkus dentre outras...

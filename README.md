# Mini E-commerce API

API REST em **Java e Spring Boot** para gerenciar produtos, clientes e pedidos de uma loja. Ao criar um pedido, a API valida o estoque, desconta as quantidades compradas e calcula o total, tudo dentro de uma transação. O projeto inclui uma página web para usar o sistema pelo navegador.

## Funcionalidades

- CRUD de **produtos** (nome, preço e estoque)
- CRUD de **clientes**, com validação de e-mail e bloqueio de e-mail repetido
- Criação de **pedidos** com vários itens:
  - confere se o cliente e os produtos existem
  - recusa o pedido se algum produto não tiver estoque suficiente
  - desconta o estoque e calcula o total
  - guarda o preço do produto no momento da compra
- Operação transacional: se um item do pedido falhar, nada é salvo, nem a baixa de estoque dos itens anteriores
- Validação dos dados de entrada com Bean Validation
- Painel web para cadastrar produtos e clientes e registrar pedidos

## Tecnologias

- Java 17
- Spring Boot 4.1.1 (Spring Web MVC)
- Spring Data JPA e Hibernate
- Bean Validation
- Banco de dados H2 (em memória)
- Maven
- HTML, CSS e JavaScript (painel web)

## Modelo de dados

```
Cliente 1 ──── N Pedido 1 ──── N ItemPedido N ──── 1 Produto
```

- Um **cliente** pode ter vários **pedidos**.
- Um **pedido** tem vários **itens**.
- Cada **item** aponta para um **produto**, com quantidade e preço unitário.

## Endpoints

### Produtos

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/produtos` | Lista os produtos |
| `GET` | `/produtos/{id}` | Busca um produto |
| `POST` | `/produtos` | Cadastra um produto |
| `PUT` | `/produtos/{id}` | Atualiza um produto |
| `DELETE` | `/produtos/{id}` | Exclui um produto |

### Clientes

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/clientes` | Lista os clientes |
| `GET` | `/clientes/{id}` | Busca um cliente |
| `POST` | `/clientes` | Cadastra um cliente |
| `PUT` | `/clientes/{id}` | Atualiza um cliente |
| `DELETE` | `/clientes/{id}` | Exclui um cliente |

### Pedidos

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/pedidos` | Cria um pedido e baixa o estoque |
| `GET` | `/pedidos` | Lista os pedidos |
| `GET` | `/pedidos/{id}` | Busca um pedido |

### Exemplo: criar um pedido

`POST /pedidos`

```json
{
  "clienteId": 1,
  "itens": [
    { "produtoId": 1, "quantidade": 2 }
  ]
}
```

Resposta (`201 Created`):

```json
{
  "id": 1,
  "cliente": { "id": 1, "nome": "Maria Souza", "email": "maria@email.com" },
  "data": "2026-10-04T20:30:00",
  "status": "CRIADO",
  "itens": [
    {
      "id": 1,
      "produto": { "id": 1, "nome": "Notebook", "preco": 3500.00, "estoque": 8 },
      "quantidade": 2,
      "precoUnitario": 3500.00,
      "subtotal": 7000.00
    }
  ],
  "total": 7000.00
}
```

### Respostas de erro

| Situação | Status |
|---|---|
| Dados inválidos (campo vazio, e-mail inválido, estoque negativo, quantidade menor que 1) | `400 Bad Request` |
| Cliente, produto ou pedido não encontrado | `404 Not Found` |
| Estoque insuficiente ou e-mail já cadastrado | `409 Conflict` |

## Como rodar

### Pré-requisitos

- JDK 17 instalado
- Variável de ambiente `JAVA_HOME` apontando para o JDK

### Passos

1. Clone o repositório:

   ```bash
   git clone https://github.com/lucassilvasantos2207-svg/mini-ecommerce-api.git
   cd mini-ecommerce-api
   ```

2. Suba a API:

   ```bash
   # Windows
   mvnw spring-boot:run

   # Linux / macOS
   ./mvnw spring-boot:run
   ```

3. Quando aparecer `Started EcommerceApplication` no terminal, a API está no ar em `http://localhost:8080`.

4. Abra `http://localhost:8080` no navegador para usar o painel web.

> O banco H2 fica em memória: os dados são apagados sempre que a aplicação é reiniciada.

## Estrutura do projeto

```
src/main/java/com/lucas/ecommerce/
├── EcommerceApplication.java
├── Produto.java / ProdutoRepository.java / ProdutoController.java
├── Cliente.java / ClienteRepository.java / ClienteController.java
├── Pedido.java / ItemPedido.java / StatusPedido.java
├── PedidoRequest.java        # formato do JSON recebido ao criar um pedido
├── PedidoRepository.java
├── PedidoService.java        # regras de negócio e transação do pedido
└── PedidoController.java

src/main/resources/static/
└── index.html                # painel web
```

## O que aprendi

- Modelar relacionamentos com JPA (`@ManyToOne` e `@OneToMany`)
- Aplicar regras de negócio em uma camada de service
- Usar transações para manter os dados consistentes
- Validar dados de entrada e devolver os status HTTP corretos
- Integrar uma página web simples com uma API REST

## Próximos passos

- Testes automatizados com JUnit e Mockito
- Trocar o H2 por MySQL, para os dados ficarem gravados
- Padronizar as respostas de erro em JSON
- Cancelamento de pedido com devolução ao estoque

## Autor

**Lucas Silva Santos**

- GitHub: [lucassilvasantos2207-svg](https://github.com/lucassilvasantos2207-svg)
- LinkedIn: [lucas-silva-santos](https://linkedin.com/in/lucas-silva-santos)

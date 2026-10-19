# 📚 Projeto Editora API (Spring Boot) — Trabalho 01

Aplicação backend desenvolvida para a disciplina de **Desenvolvimento Web** da **Universidade Estadual de Ponta Grossa (UEPG)**.
Trata-se de uma API RESTful completa e modular construída com **Spring Boot** utilizando arquitetura em camadas (Controller, Service, Repository, Entity), focada no gerenciamento de **Autores** e **Artigos** com mapeamento objeto-relacional (JPA/Hibernate) e banco de dados **PostgreSQL**.

---

## 🚀 Tecnologias Utilizadas

*   **Java 17+** (Compatível e validado com JDK 17 até 26)
*   **Spring Boot 4.1.1**
    *   Spring Web MVC
    *   Spring Data JPA (Hibernate 7.x)
    *   Spring Boot Starter Validation (Hibernate Validator / Jakarta Validation)
*   **PostgreSQL 18.x** (Driver JDBC 42.7.x)
*   **Lombok** (Geração de Getters, Setters, Construtores, Equals/HashCode)
*   **Maven Wrapper** (Gerenciamento de dependências e execução sem instalação prévia do Maven)

---

## 🏗️ Arquitetura e Estrutura de Pacotes (`dw.editora`)

O projeto implementa separação estrita de responsabilidades:

*   **`entity`**: Classes de domínio JPA que mapeiam o modelo relacional:
    *   [`Autor.java`](src/main/java/dw/editora/entity/Autor.java): Contém `id` (PK IDENTITY), `nome` (obrigatório, `@NotBlank`), `email` (obrigatório, único, `@Email`), `biografia` (opcional, `TEXT`) e relacionamento `@OneToMany(mappedBy = "autor")` com `@JsonIgnore` para prevenção de recursão cíclica.
    *   [`Artigo.java`](src/main/java/dw/editora/entity/Artigo.java): Contém `id` (PK IDENTITY), `titulo` (`@NotBlank`), `resumo` (`@NotBlank`), `publicado` (boolean) e relacionamento `@ManyToOne` com `@JoinColumn(name = "autor_id", nullable = false)`.
*   **`repository`**: Interfaces que estendem `JpaRepository`:
    *   [`AutorRepository.java`](src/main/java/dw/editora/repository/AutorRepository.java): CRUD de autores, busca por nome (`findByNomeContainingIgnoreCase`) e verificação de e-mail (`existsByEmail`, `findByEmail`).
    *   [`ArtigoRepository.java`](src/main/java/dw/editora/repository/ArtigoRepository.java): CRUD de artigos, filtros por status de publicação (`findByPublicado`), busca por título (`findByTituloContainingIgnoreCase`) e busca por autor (`findByAutorId`).
*   **`service`**: Camada que encapsula as regras de negócio:
    *   [`AutorService.java`](src/main/java/dw/editora/service/AutorService.java): Validação de regras de negócio, garantia de unicidade de e-mail, consulta de artigos associados.
    *   [`ArtigoService.java`](src/main/java/dw/editora/service/ArtigoService.java): Associação de autor existente ao artigo via ID, validações de consistência e integridade.
*   **`control`**: Controladores REST expondo os endpoints HTTP:
    *   [`AutorController.java`](src/main/java/dw/editora/control/AutorController.java): Endpoints sob `/api/autores`.
    *   [`ArtigoController.java`](src/main/java/dw/editora/control/ArtigoController.java): Endpoints sob `/api/artigos`.
*   **`exception`**: Tratamento centralizado de erros:
    *   [`GlobalExceptionHandler.java`](src/main/java/dw/editora/exception/GlobalExceptionHandler.java): `@RestControllerAdvice` padronizando respostas para validações (400), JSON malformado (400), recurso não encontrado (404), conflito de integridade/duplicidade (409) e erro interno (500).
    *   [`ApiError.java`](src/main/java/dw/editora/exception/ApiError.java): Formato padronizado de resposta de erro.

---

## ⚙️ Configuração do Banco de Dados PostgreSQL

A aplicação está configurada para rodar na porta **8000** e conectar ao PostgreSQL.

O arquivo [`src/main/resources/application.properties`](src/main/resources/application.properties) utiliza variáveis de ambiente com valores padrão:

```properties
server.port=8000

spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:editora}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:SENHA}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
```

### Configuração de Credenciais
Por padrão, o projeto conecta com usuário `postgres` e senha `SENHA` no banco `editora`. Se o seu PostgreSQL utilizar outra senha ou porta, você pode configurar via variáveis de ambiente sem alterar o código:

*   **Linux / macOS:**
    ```bash
    export DB_PASSWORD=sua_senha
    export DB_USER=postgres
    export DB_NAME=editora
    ```
*   **Windows (PowerShell):**
    ```powershell
    $env:DB_PASSWORD="sua_senha"
    $env:DB_NAME="editora"
    ```

---

## 📍 Endpoints da API

A URL base da aplicação é `http://localhost:8000`.

### 1. Endpoints de Autores (`/api/autores`)

| Método | Endpoint | Status Sucesso | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/autores` | `201 Created` | Cadastra um novo autor (retorna header `Location`). |
| `GET` | `/api/autores` | `200 OK` | Lista todos os autores (aceita `?nome=` opcional). |
| `GET` | `/api/autores/{id}` | `200 OK` | Retorna o autor pelo ID (`404` se não existir). |
| `GET` | `/api/autores/nome?filtro={nome}` | `200 OK` | Busca autores por filtro no nome. |
| `PUT` | `/api/autores/{id}` | `200 OK` | Atualiza os dados de um autor. |
| `DELETE` | `/api/autores/{id}` | `204 No Content` | Remove o autor (`404` se não existir, `409` se tiver artigos). |
| `DELETE` | `/api/autores` | `204 No Content` | Remove todos os autores. |
| `GET` | `/api/autores/{id}/artigos` | `200 OK` | Retorna todos os artigos publicados pelo autor (`404` se autor não existir; `[]` se não tiver artigos). |

### 2. Endpoints de Artigos (`/api/artigos`)

| Método | Endpoint | Status Sucesso | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/artigos` | `201 Created` | Cria um novo artigo associado a um autor (retorna header `Location`). |
| `GET` | `/api/artigos` | `200 OK` | Lista todos os artigos (aceita `?titulo=` opcional). |
| `GET` | `/api/artigos/{id}` | `200 OK` | Retorna o artigo pelo ID com dados do seu autor (`404` se não existir). |
| `GET` | `/api/artigos/publicados` | `200 OK` | Lista artigos publicados (`publicado = true`). |
| `PUT` | `/api/artigos/{id}` | `200 OK` | Atualiza completamente um artigo. |
| `DELETE` | `/api/artigos/{id}` | `204 No Content` | Remove o artigo pelo ID. |
| `DELETE` | `/api/artigos` | `204 No Content` | Remove todos os artigos. |

---

## 📝 Exemplos de Requisição e Resposta

### Cadastrar Autor (`POST /api/autores`)
*   **Request Body:**
    ```json
    {
      "nome": "Clarice Lispector",
      "email": "clarice@lispector.com",
      "biografia": "Uma das escritoras brasileiras mais importantes do século XX."
    }
    ```
*   **Response (`201 Created` com Header `Location: /api/autores/1`):**
    ```json
    {
      "id": 1,
      "nome": "Clarice Lispector",
      "email": "clarice@lispector.com",
      "biografia": "Uma das escritoras brasileiras mais importantes do século XX."
    }
    ```

### Cadastrar Artigo (`POST /api/artigos`)
*   **Request Body:**
    ```json
    {
      "titulo": "A Hora da Estrela",
      "resumo": "História de Macabéa, uma jovem alagoana vivendo no Rio de Janeiro.",
      "publicado": true,
      "autor": {
        "id": 1
      }
    }
    ```
*   **Response (`201 Created` com Header `Location: /api/artigos/1`):**
    ```json
    {
      "id": 1,
      "titulo": "A Hora da Estrela",
      "resumo": "História de Macabéa, uma jovem alagoana vivendo no Rio de Janeiro.",
      "publicado": true,
      "autor": {
        "id": 1,
        "nome": "Clarice Lispector",
        "email": "clarice@lispector.com",
        "biografia": "Uma das escritoras brasileiras mais importantes do século XX."
      }
    }
    ```

### Consultar Artigos de um Autor (`GET /api/autores/1/artigos`)
*   **Response (`200 OK`):**
    ```json
    [
      {
        "id": 1,
        "titulo": "A Hora da Estrela",
        "resumo": "História de Macabéa, uma jovem alagoana vivendo no Rio de Janeiro.",
        "publicado": true,
        "autor": {
          "id": 1,
          "nome": "Clarice Lispector",
          "email": "clarice@lispector.com",
          "biografia": "Uma das escritoras brasileiras mais importantes do século XX."
        }
      }
    ]
    ```

### Resposta de Erro Padronizada (Exemplo: 409 Conflict em e-mail duplicado)
```json
{
  "timestamp": "2026-10-19T15:43:54.382",
  "status": 409,
  "error": "Conflito de dados",
  "message": "Já existe um autor cadastrado com o e-mail: clarice@lispector.com",
  "path": "/api/autores"
}
```

---

## 💻 Como Executar a Aplicação e os Testes

### Pré-requisitos
*   Java JDK 17 ou superior.
*   Serviço do PostgreSQL rodando localmente (porta padrão 5432).

### Executar os Testes Automatizados
Os testes de integração utilizam um banco isolado (`editora_test`) configurado em `src/test/resources/application.properties`, preservando integralmente os dados da base de desenvolvimento.

*   **Linux / macOS:**
    ```bash
    ./mvnw clean test
    ```
*   **Windows:**
    ```cmd
    mvnw.cmd clean test
    ```

### Executar a Aplicação Localmente
*   **Linux / macOS:**
    ```bash
    ./mvnw spring-boot:run
    ```
*   **Windows:**
    ```cmd
    mvnw.cmd spring-boot:run
    ```
A aplicação iniciará na porta `8000` pronta para receber requisições em `http://localhost:8000/api`.

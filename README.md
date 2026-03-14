# 📦 Produtos API

API RESTful para gerenciamento de produtos com integração tributária via código NCM (Nomenclatura Comum do Mercosul), desenvolvida como parte do processo seletivo **Mix Fiscal**.

---

## 🚀 Tecnologias

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.5.x | Framework base |
| Spring Data JPA | — | Camada de persistência |
| Spring Validation | — | Validação de entrada |
| PostgreSQL | 16 | Banco de dados (perfil `postgres`) |
| H2 Database | — | Banco em memória (perfil padrão) |
| Lombok | — | Redução de boilerplate |
| SpringDoc OpenAPI | 2.x | Documentação Swagger |
| JUnit 5 + Mockito | — | Testes unitários |
| Docker / Docker Compose | — | Infraestrutura local |
| Maven | 3.9+ | Gerenciador de dependências |

---

## 📋 Pré-requisitos

- [Java 17+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/get-started) *(necessário apenas para rodar com PostgreSQL)*

```bash
java -version
mvn -version
docker -version
```

---

## ▶️ Como rodar o projeto

O projeto possui dois perfis de execução. Em ambos, a API sobe em `http://localhost:8080`.

---

### Perfil padrão — H2 em memória *(sem Docker)*

Ideal para rodar rapidamente sem nenhuma dependência de infraestrutura.

```bash
git clone https://github.com/pricardo91/produtos-api.git
cd produtos-api

./mvnw spring-boot:run
```

> **Console H2:** `http://localhost:8080/h2-console`
> - JDBC URL: `jdbc:h2:mem:produtosdb`
> - User: `sa`
> - Password: *(vazio)*

⚠️ Os dados são perdidos ao reiniciar a aplicação (`create-drop`).

---

### Perfil `postgres` — PostgreSQL via Docker

```bash
# 1. Suba o banco de dados
docker compose up -d

# 2. Aguarde o container ficar saudável
docker compose ps

# 3. Suba a aplicação com o perfil postgres
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

Credenciais configuradas no `docker-compose.yml`:

| Parâmetro | Valor |
|---|---|
| Host | `localhost:5432` |
| Database | `produtosdb` |
| Usuário | `postgres` |
| Senha | `postgres` |

As credenciais podem ser sobrescritas via variáveis de ambiente:

```bash
DB_USERNAME=outro_user DB_PASSWORD=outra_senha ./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

Para encerrar o banco:

```bash
docker compose down          # para os containers
docker compose down -v       # para os containers e apaga os dados
```

---

## 🧪 Como executar os testes

Os testes utilizam H2 em memória e **não dependem do Docker**.

```bash
./mvnw test
```

---

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, acesse:

**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## 🗂️ Endpoints

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| `POST` | `/v1/produtos` | Cadastrar novo produto | `201 Created` |
| `GET` | `/v1/produtos` | Listar produtos (paginado) | `200 OK` |
| `GET` | `/v1/produtos/{id}` | Buscar produto por ID + dados NCM | `200 OK` |
| `PATCH` | `/v1/produtos/{id}` | Atualizar produto | `200 OK` |
| `DELETE` | `/v1/produtos/{id}` | Deletar produto | `204 No Content` |

### Exemplo — Criar produto

```bash
curl -X POST http://localhost:8080/v1/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Notebook Gamer",
    "sku": "NOTE-GAM-001",
    "precoBase": 4599.90,
    "codigoNcm": "8471.30.12"
  }'
```

### Paginação

```
GET /v1/produtos?page=0&size=10&sort=nome,asc
```

---

## 🏗️ Estrutura do projeto

```
src/main/java/com/mixfiscal/produtos_api/
├── client/       # Integração com API externa de impostos (TaxIntegrationClient)
├── config/       # Configurações (OpenAPI, TaxIntegrationProperties)
├── controller/   # Controllers REST + interfaces Swagger (ProdutoAPI)
├── dto/
│   ├── in/       # Objetos de entrada (request)
│   └── out/      # Objetos de saída (response)
├── entity/       # Entidades JPA
├── exception/    # Exceções de negócio
├── handler/      # Tratamento global de erros (@ControllerAdvice)
├── mapper/       # Conversão entre entidades e DTOs
├── repository/   # Interfaces Spring Data JPA
└── service/      # Regras de negócio

src/main/resources/
├── application.yaml           # Configuração base + perfil H2 (padrão)
└── application-postgres.yaml  # Configuração do perfil postgres
```

---

## 🔌 Integração Tributária (NCM)

O `TaxIntegrationClient` simula a consulta de alíquotas de impostos (II, IPI, PIS, COFINS) com base no código NCM do produto. Os dados retornados são **mockados** — a arquitetura já está preparada para substituir pela chamada HTTP real sem alterar nenhuma outra camada.

A URL da API é configurada em `application.yaml`:

```yaml
tax:
  api:
    url: http://api.mixfiscal.com.br/impostos
```

---

## 🛠️ Tratamento de erros

Todos os erros retornam um corpo padronizado:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Produto com ID '...' não encontrado.",
  "description": "...",
  "path": "GET: /v1/produtos/..."
}
```

| Status | Situação |
|---|---|
| `400 Bad Request` | Campos inválidos ou ausentes |
| `404 Not Found` | Produto não encontrado |
| `409 Conflict` | SKU já cadastrado |
| `500 Internal Server Error` | Erro inesperado |

---

## 👨‍💻 Autor

Desenvolvido por **[Ricardo Silva](https://github.com/pricardo91)** como parte do processo seletivo da Mix Fiscal
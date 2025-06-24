# API de Tarefas com Javalin

### Como Rodar

**1. Método pela IDE (IntelliJ):**
* **Servidor:** Abra `src/main/java/org/example/Main.java` e clique no play para iniciar.
* **Testes:** Abra `src/test/java/ApiTest.java` e clique no play para rodar os testes.
* **Cliente:** Com o servidor rodando, abra `src/main/java/org/example/client/ApiClient.java` e clique no play.

**2. Método pelo Terminal:**
* Compilar: `./gradlew build`
* Rodar API: `./gradlew run`
* Rodar Testes: `./gradlew test`

### Endpoints
* `GET /tarefas`
* `GET /tarefas/{id}`
* `POST /tarefas`
* `GET /status`
* `GET /hello`
* `GET /saudacao/{nome}`
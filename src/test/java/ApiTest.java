import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.example.controller.AlunoController;
import org.example.dao.AlunoDAO;
import org.example.model.Aluno;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiTest {
    Javalin app = setupApp();

    private Javalin setupApp() {
        Javalin app = Javalin.create(config -> config.jsonMapper(new io.javalin.json.JavalinJackson()));
        AlunoDAO dao = new AlunoDAO();
        AlunoController controller = new AlunoController(dao);

        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));
        app.post("/alunos", controller::criar);
        app.get("/alunos", controller::buscarTodos);
        app.get("/alunos/{id}", controller::buscarPorId);
        return app;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testHelloEndpoint() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/hello");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).isEqualTo("Hello, Javalin!");
        });
    }

    @Test
    void testCreateAluno() {
        JavalinTest.test(app, (server, client) -> {
            String newAlunoJson = "{\"nome\": \"Marie Curie\", \"email\": \"marie@example.com\"}";
            Response response = client.post("/alunos", newAlunoJson);

            assertThat(response.code()).isEqualTo(201);
            Aluno createdAluno = objectMapper.readValue(response.body().string(), Aluno.class);
            assertThat(createdAluno.getNome()).isEqualTo("Marie Curie");
            assertThat(createdAluno.getId()).isPositive();
        });
    }

    @Test
    void testGetAlunoById() {
        JavalinTest.test(app, (server, client) -> {
            String newAlunoJson = "{\"nome\": \"Nikola Tesla\", \"email\": \"tesla@example.com\"}";
            Response createResponse = client.post("/alunos", newAlunoJson);
            Aluno createdAluno = objectMapper.readValue(createResponse.body().string(), Aluno.class);
            long newId = createdAluno.getId();

            Response getResponse = client.get("/alunos/" + newId);
            assertThat(getResponse.code()).isEqualTo(200);
            Aluno fetchedAluno = objectMapper.readValue(getResponse.body().string(), Aluno.class);
            assertThat(fetchedAluno.getNome()).isEqualTo("Nikola Tesla");
        });
    }

    @Test
    void testGetAlunosList() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/alunos");
            assertThat(response.code()).isEqualTo(200);

            List<Aluno> alunos = objectMapper.readValue(response.body().string(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Aluno.class));
            assertThat(alunos).isNotEmpty();
        });
    }

    @Test
    void testGetAlunoNotFound() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/alunos/9999");
            assertThat(response.code()).isEqualTo(404);
        });
    }
}

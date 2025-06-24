import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.example.controller.TarefaController;
import org.example.dao.TarefaDAO;
import org.example.model.Tarefa;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiTest {
    Javalin app = setupApp();

    private Javalin setupApp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Javalin app = Javalin.create(config -> config.jsonMapper(new JavalinJackson()));
        TarefaDAO dao = new TarefaDAO();
        TarefaController controller = new TarefaController(dao);

        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));
        app.post("/tarefas", controller::criar);
        app.get("/tarefas", controller::buscarTodas);
        app.get("/tarefas/{id}", controller::buscarPorId);
        return app;
    }

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void testHelloEndpoint() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/hello");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).isEqualTo("Hello, Javalin!");
        });
    }

    @Test
    void testCreateTarefa() {
        JavalinTest.test(app, (server, client) -> {
            String newTarefaJson = "{\"titulo\": \"Testar API\", \"descricao\": \"Usar o JavalinTest para testar.\"}";
            Response response = client.post("/tarefas", newTarefaJson);

            assertThat(response.code()).isEqualTo(201);
            Tarefa createdTarefa = objectMapper.readValue(response.body().string(), Tarefa.class);
            assertThat(createdTarefa.getTitulo()).isEqualTo("Testar API");
            assertThat(createdTarefa.getId()).isPositive();
            assertThat(createdTarefa.isConcluida()).isFalse();
            assertThat(createdTarefa.getDataCriacao()).isNotNull();
        });
    }

    @Test
    void testGetTarefaById() {
        JavalinTest.test(app, (server, client) -> {
            String newTarefaJson = "{\"titulo\": \"Buscar por ID\", \"descricao\": \"Uma tarefa específica.\"}";
            Response createResponse = client.post("/tarefas", newTarefaJson);
            Tarefa createdTarefa = objectMapper.readValue(createResponse.body().string(), Tarefa.class);
            long newId = createdTarefa.getId();

            Response getResponse = client.get("/tarefas/" + newId);
            assertThat(getResponse.code()).isEqualTo(200);
            Tarefa fetchedTarefa = objectMapper.readValue(getResponse.body().string(), Tarefa.class);
            assertThat(fetchedTarefa.getTitulo()).isEqualTo("Buscar por ID");
        });
    }

    @Test
    void testGetTarefasList() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/tarefas");
            assertThat(response.code()).isEqualTo(200);

            List<Tarefa> tarefas = objectMapper.readValue(response.body().string(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Tarefa.class));
            assertThat(tarefas).isNotEmpty();
        });
    }

    @Test
    void testGetTarefaNotFound() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/tarefas/9999");
            assertThat(response.code()).isEqualTo(404);
        });
    }
}
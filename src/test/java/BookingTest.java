// Importa as classes necessárias para os testes
import io.restassured.RestAssured; // Biblioteca para automação de testes REST
import org.junit.jupiter.api.Test; // Framework de testes JUnit 5
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// Classe que contém os testes de API
public class BookingTest {

    public String lerJson(String caminhoArquivo) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
    }

    // Define um método de teste
    @Test
    public void testGetBooking() {
        // Configura a URL base para as requisições da API
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        // Configura e executa a requisição GET para o endpoint "/booking/"
        given() // Define as configurações da requisição (headers, parâmetros, etc.)
            .header("Accept", "*/*") //adiciona o header accept
        .when() // Indica o início da execução da requisição
            .get("/booking/") // Especifica o endpoint a ser chamado
        .then() // Define as validações da resposta
            .statusCode(200) // Verifica se o status code da resposta é 200 (OK)
            .log().all();    // Loga no console todos os detalhes da resposta (body, headers, etc.)
    }

    @Test
    public void testWithId() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/595")
                .then()
                .statusCode(200)
                .body("firstname", equalTo("Josh"))
                .body("lastname", equalTo("Allen"))
                .body("totalprice", equalTo(111))
                .body("depositpaid", is(true))
                .body("bookingdates.checkin", equalTo("2018-01-01"))
                .body("bookingdates.checkout", equalTo("2019-01-01"))
                .body("additionalneeds", equalTo("super bowls"));
    }

    @Test
    public void cadastrarReserva() throws IOException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String jsonBody = lerJson("src/test/resources/payloads/reserva.json");

        given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo("herbertao"))
                .body("booking.lastname", equalTo("qazando"))
                .body("booking.totalprice", equalTo(222))
                .body("booking.depositpaid", is(true))
                .body("booking.bookingdates.checkin", equalTo("2018-01-01"))
                .body("booking.bookingdates.checkout", equalTo("2019-01-01"))
                .body("booking.additionalneeds", equalTo("Breakfast"));
    }
}
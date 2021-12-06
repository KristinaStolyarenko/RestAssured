import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestApiTests {

    @Test
    void createUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"andrey\",\"job\": \"qa\"}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("andrey"), "job", is("qa"));
    }

    @Test
    void getSingleUser() {
        given()
                .when()
                .get("https://reqres.in/api/users/1")
                .then()
                .statusCode(200)
                .body("data.email", is("george.bluth@reqres.in"), "data.first_name",
                        is("George"),"data.last_name", is("Bluth"));
    }

    @Test
    void putUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Gosha\"}")
                .when()
                .put("https://reqres.in/api/users/1")
                .then()
                .statusCode(200)
                .body( "name", is("Gosha"),"updatedAt", notNullValue());
    }

    @Test
    void patchUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Bob\",\"job\": \"hunter\"}")
                .when()
                .patch("https://reqres.in/api/users/1")
                .then()
                .statusCode(200)
                .body( "name", is("Bob"), "job", is("hunter"),
                        "updatedAt", notNullValue());
    }

    @Test
    void DeleteUser() {
        given()
                .when()
                .delete("https://reqres.in/api/users/1")
                .then()
                .statusCode(204);
    }

}

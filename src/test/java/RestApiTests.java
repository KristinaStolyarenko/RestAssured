import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static filters.CustomLogFilter.customLogFilter;
import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestApiTests {

    @Test
    void createUser() {
        given()
                .log().all()
                .filter(customLogFilter().withCustomTemplates())
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
                .log().all()
                .filter(customLogFilter().withCustomTemplates())
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
                .log().all()
                .filter(customLogFilter().withCustomTemplates())
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
                .log().all()
                .filter(customLogFilter().withCustomTemplates())
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
    void deleteUser() {
        given()
                .log().all()
                .filter(customLogFilter().withCustomTemplates())
                .when()
                .delete("https://reqres.in/api/users/1")
                .then()
                .statusCode(204);
    }

    @Test
    void getWithNotExistUser() {
        given()
                .log().all()
                .filter(customLogFilter().withCustomTemplates())
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

}

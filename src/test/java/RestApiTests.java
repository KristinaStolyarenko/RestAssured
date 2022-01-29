import models.NewUser;
import lombok.UserData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.requestSpec;
import static specs.Specs.responseSpec;

public class RestApiTests {

    @Test
    void createUser() {
        NewUser testUser = new NewUser();
        testUser.setName("Andrey");
        testUser.setJob("QA");

        NewUser newUser = given()
                .spec(requestSpec)
                .body(testUser)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().as(NewUser.class);
        assertEquals(testUser.getName(), newUser.getName());
        assertEquals(testUser.getJob(), newUser.getJob());
    }

    @Test
    void getSingleUser() {
        UserData userData = given()
                .spec(requestSpec)
                .when()
                .get("/users/1")
                .then()
                .spec(responseSpec)
                .extract().as(UserData.class);
        assertEquals(1, userData.getUser().getId());
        assertEquals("george.bluth@reqres.in", userData.getUser().getEmail());
        assertEquals("George", userData.getUser().getFirstName());
        assertEquals("Bluth", userData.getUser().getLastName());
    }

    @Test
    void putUser() {
        given()
                .spec(requestSpec)
                .body("{\"name\": \"Gosha\"}")
                .when()
                .put("/users/1")
                .then()
                .spec(responseSpec)
                .body("name", is("Gosha"))
                .body("updatedAt", notNullValue());
    }

    @Test
    void patchUser() {
        given()
                .spec(requestSpec)
                .body("{\"name\": \"Bob\",\"job\": \"hunter\"}")
                .when()
                .patch("/users/1")
                .then()
                .spec(responseSpec)
                .body("name", is("Bob"))
                .body("job", is("hunter"))
                .body("updatedAt", notNullValue());
    }

    @Test
    void deleteUser() {
        given()
                .spec(requestSpec)
                .when()
                .delete("/users/1")
                .then()
                .statusCode(204);
    }

    @Test
    void getWithNotExistUser() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void getListUsers() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users")
                .then()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("george.bluth@reqres.in"));
    }
}
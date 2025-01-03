package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ReqresApiTests {

    static {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    public void testGetUsers() {
        Response response = given()
                .when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertTrue(response.jsonPath().getList("data").size() > 0, "User list should not be empty");
    }

    @Test
    public void testGetUserById() {
        int userId = 2;

        Response response = given()
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertEquals(userId, response.jsonPath().getInt("data.id"), "User ID does not match");
    }

    @Test
    public void testCreateUser() {
        String requestBody = """
                {
                    "name": "morpheus",
                    "job": "leader"
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Assertions.assertEquals("morpheus", response.jsonPath().getString("name"), "Name does not match");
        Assertions.assertNotNull(response.jsonPath().getString("id"), "ID should be generated");
    }

    @Test
    public void testUpdateUser() {
        int userId = 2;
        String requestBody = """
                {
                    "name": "morpheus",
                    "job": "zion resident"
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/users/" + userId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertEquals("zion resident", response.jsonPath().getString("job"), "Job was not updated");
    }

    @Test
    public void testDeleteUser() {
        int userId = 2;

        given()
                .when()
                .delete("/users/" + userId)
                .then()
                .statusCode(204);
    }

    @Test
    public void testLoginSuccessful() {
        String requestBody = """
                {
                    "email": "eve.holt@reqres.in",
                    "password": "cityslicka"
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertNotNull(response.jsonPath().getString("token"), "Token should be generated");
    }

    @Test
    public void testLoginUnsuccessful() {
        String requestBody = """
                {
                    "email": "peter@klaven"
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .extract()
                .response();

        Assertions.assertEquals("Missing password", response.jsonPath().getString("error"), "Error message does not match");
    }
}
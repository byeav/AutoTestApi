package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ApiTests {

    static {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void testGetPosts() {
        Response response = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertFalse(response.jsonPath().getList("").isEmpty(), "Response should not be empty");
    }

    @Test
    public void testGetPostById() {
        int postId = 1;

        Response response = given()
                .when()
                .get("/posts/" + postId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertEquals(postId, response.jsonPath().getInt("id"), "Post ID does not match");
    }

    @Test
    public void testCreatePost() {
        String requestBody = """
                {
                    "title": "foo",
                    "body": "bar",
                    "userId": 1
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Assertions.assertEquals("foo", response.jsonPath().getString("title"), "Title does not match");
    }

    @Test
    public void testUpdatePost() {
        int postId = 1;
        String updatedBody = """
                {
                    "id": 1,
                    "title": "updated title",
                    "body": "updated body",
                    "userId": 1
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(updatedBody)
                .when()
                .put("/posts/" + postId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assertions.assertEquals("updated title", response.jsonPath().getString("title"), "Title was not updated");
    }

    @Test
    public void testDeletePost() {
        int postId = 1;

        given()
                .when()
                .delete("/posts/" + postId)
                .then()
                .statusCode(200);
    }
}
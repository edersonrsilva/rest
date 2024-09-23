package br.com.usermanager.controller;

import br.com.usermanager.domain.entity.User;
import br.com.usermanager.service.UserService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void testFindAll() {
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testFindById() {
        User createdUser = userService.save(new User(null, "John Doe"));

        given()
                .pathParam("id", createdUser.getId())
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo("John Doe"));

    }

    @Test
    public void testSave() {
        long userId = given()
                .contentType("application/json")
                .body(new User(null, "Joao"))
                .when()
                .post("/users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath().getLong("id");

        User newUser = userService.findById(userId);
        assertEquals("Joao", newUser.getUsername());
    }

    @Test
    public void testUpdate() {
        String newName = "John Doe Doe";
        User createdUser = userService.save(new User(null, "John Doe"));
        createdUser.setUsername(newName);

        Response response = given()
                .contentType("application/json")
                .pathParam("id", createdUser.getId())
                .body(createdUser)
                .when()
                .put("/users/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        User updatedUser = response.as(User.class);

        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals(newName, updatedUser.getUsername());
    }

    @Test
    public void testDelete(){
        User createdUser = userService.save(new User(null, "John Doe"));

        given()
                .pathParam("id", createdUser.getId())
                .when()
                .delete("/users/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
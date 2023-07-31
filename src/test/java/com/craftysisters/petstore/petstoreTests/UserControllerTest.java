package com.craftysisters.petstore.petstoreTests;

import com.craftysisters.petstore.base.BaseTest;
import com.craftysisters.petstore.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends BaseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private int userStatus;


    @BeforeEach
    void setUp() {
        userId = 10001;
        username = "myCoolUsername";
        firstName = "Steven";
        lastName = "Seagal";
        email = "email@email.com";
        password = "password123";
        phone = "123456";
        userStatus = 0;
    }

    @Test
    @Tag("postUser")
    @DisplayName("Should be able to create user")
    void postUser() {

        UserDto userRequest = new UserDto();
        userRequest.setId(userId);
        userRequest.setUsername(username);
        userRequest.setFirstName(firstName);
        userRequest.setLastName(lastName);
        userRequest.setEmail(email);
        userRequest.setPassword(password);
        userRequest.setPhone(phone);
        userRequest.setUserStatus(userStatus);

        Response firstResponse = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .body(userRequest)
                .when()
                .post("/user");

        firstResponse.then().statusCode(HttpStatus.SC_OK);

        assertThat(firstResponse.jsonPath().getInt("code")).isEqualTo(200);

        Response secondResponse = when()
                .get("/user/{username}", username);

        UserDto userActual = secondResponse.as(UserDto.class);

        assertThat(userActual).isEqualTo(userRequest);

    }


    @Test
    @Tag("postUser")
    @DisplayName("Should be able to find user with username")
    void getUser() {

        Response response = when()
                .get("/user/{username}", username);

        response.then().statusCode(HttpStatus.SC_OK);

        assertThat(response.jsonPath().getString("username")).isEqualTo(username);

    }




}

/*
 * MIT License
 *
 * Copyright (c) 2020 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.craftysisters.petstore.petstoreTests;

import com.craftysisters.petstore.base.BaseTest;
import com.craftysisters.petstore.enums.ErrorCodes;
import com.craftysisters.petstore.dto.ErrorDto;
import com.craftysisters.petstore.dto.PetDto;
import com.craftysisters.petstore.dto.TagDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

class PetControllerTest extends BaseTest {

    private ObjectMapper objectMapper;
    private Long petId;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        petId = Long.parseLong("444414444");
    }


    @Test
    @Tag("getPet")
    @DisplayName("Should be able to hit the pet endpoint")
    void getPet() throws JsonProcessingException {
        Response response = when()
                .get("/pet/{id}", petId);

        String responseBody = response.getBody().asString();
        PetDto petResponse = objectMapper.readValue(responseBody, PetDto.class);

        response.then().statusCode(HttpStatus.SC_OK);
        Assertions.assertEquals(petId, petResponse.getId());
    }

    @Test
    @Tag("getPet")
    @DisplayName("Should be able to hit the pet endpoint")
    void getPetNotFound() throws JsonProcessingException {
        Response response = when()
                .get("/pet/{id}", petId);


        if(response.statusCode() != 200){
            ErrorDto error = response.as(ErrorDto.class);
            Assertions.assertEquals(ErrorCodes.PET_NOT_FOUND, error.getCode());
        } else {
            String responseBody = response.getBody().asString();
            PetDto petResponse = objectMapper.readValue(responseBody, PetDto.class);
            Assertions.assertEquals(petId, petResponse.getId());
        }
    }

    @Test
    @Tag("postPet")
    @DisplayName("Should be able to create pet")
    void postPet() {

        PetDto petRequest = new PetDto();
        petRequest.setId(petId);
        petRequest.setName("Pipi");
        petRequest.getTags().add(new TagDto(0, "amazing"));

        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .body(petRequest)
                .when()
                .post("/pet");

        response.then()
                .statusCode(HttpStatus.SC_OK);

        PetDto petResponse = response.as(PetDto.class);
        Assertions.assertEquals(petRequest.getName(), petResponse.getName());
        Assertions.assertNotNull(petResponse.getId());

        assertThat(petResponse).usingRecursiveComparison()
                .ignoringFields("id", "tags")
                .isEqualTo(petRequest);

    }

    @Test
    @Tag("getPetByStatus")
    @DisplayName("Should be able to find pet by status")
    void getPetByStatus() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus");

        //Example of ObjectMapper
        //List<PetDto> responseFromObjectMapper = objectMapper.readValue(response.asString(), new TypeReference<List<PetDto>>(){});
        List<PetDto> petResponseList = response.as(new TypeRef<List<PetDto>>(){});

        for (PetDto pet : petResponseList) {
            Assertions.assertEquals(pet.getStatus(), "available");
        }

    }


    @Test
    @Tag("getPetById")
    @DisplayName("Should find pet by id")
    void getpetById(){
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .when()
                .get("/pet/{petId}", petId);


        PetDto petExpected = new PetDto();
        petExpected.setId(petId);

        PetDto petResponse = response.as(PetDto.class);
        Assertions.assertEquals(petResponse.getId(), petExpected.getId());

        assertThat(petResponse).usingRecursiveComparison()
                .ignoringFields("tags", "category", "name", "photoUrls", "status")
                .isEqualTo(petExpected);
    }

    @Test
    @Tag("updatePetById")
    @DisplayName("Should be able to update pet using id")
    void updatePetById(){

        PetDto petExpected = new PetDto();
        petExpected.setId(petId);
        petExpected.setName("Maja");
        petExpected.setStatus("unavailable");

        Response response1 = RestAssured.given()
                .accept("application/json")
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", "Maja")
                .formParam("status", "unavailable")
                .when()
                .post("/pet/{petId}", petId);

        response1.then()
                .statusCode(HttpStatus.SC_OK);

        Response response2 = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .when()
                .get("/pet/{petId}", petId);

        PetDto petResponse = response2.as(PetDto.class);
        Assertions.assertEquals(petExpected.getName(), petResponse.getName());

    }


    @Test
    @Tag("updatePet")
    @DisplayName("Should be able to update pet")
    void updatePet(){
        PetDto petRequest = new PetDto();
        petRequest.setId(petId);
        petRequest.setName("Pipi");
        petRequest.setStatus("available");

        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .body(petRequest)
                .when()
                .put("/pet");

        response.then()
                .statusCode(HttpStatus.SC_OK);

        PetDto petResponse = response.as(PetDto.class);
        Assertions.assertEquals(petRequest.getName(), petResponse.getName());
        Assertions.assertEquals(petRequest.getId(), petResponse.getId());
        Assertions.assertEquals(petRequest.getStatus(), petResponse.getStatus());
    }

    @Test
    @Tag("noIdUpdatePet")
    @DisplayName("Shouldn't be able to update pet without id in body")
    void updatePetWithoutId(){
        PetDto petRequest = new PetDto();
        petRequest.setName("Pipi");
        petRequest.setStatus("available");

        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .body(petRequest)
                .when()
                .put("/pet");

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @Tag("uploadPetImage")
    @DisplayName("Should be able to upload pet image")
    void uploadImage(){
        String filePath = "C:/Users/HP/Desktop/mouse.png";

        File imageFile = new File(filePath);
        Response response = RestAssured.given()
                .multiPart("image", imageFile, ContentType.IMAGE_PNG.getMimeType())
                .accept("application/json")
                .contentType("multipart/form-data")
                .when()
                .post("/pet/{petId}/uploadImage", petId);

        response.then().statusCode(HttpStatus.SC_OK);

    }

    @Test
    @Tag("deletePet")
    @DisplayName("Should be able to delete pet")
    void deletePet(){
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .when()
                .delete("/pet/{petId}", petId);

        response.then().statusCode(HttpStatus.SC_OK);

    }






}

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
package com.craftysisters.petstore.petTests;

import com.craftysisters.petstore.base.BaseTest;
import com.craftysisters.petstore.dto.PetDto;
import com.craftysisters.petstore.dto.TagDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

class PetControllerTest extends BaseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Tag("getPet")
    @DisplayName("Should be able to hit the pet endpoint")
    void getPet() throws JsonProcessingException {
        Response response = when()
                .get("/pet/{id}", 1);

        objectMapper.readValue(response.toString(), PetDto.class);
        response
                .then()
                .statusCode(HttpStatus.SC_OK);
        //.body("status", is("UP"));
    }

    @Test
    @Tag("postPet")
    @DisplayName("Should be able to create pet")
    void postPet() {
        PetDto petRequest = new PetDto();
        Long id = Long.parseLong("444414444");
        petRequest.setId(id);
        petRequest.setName("Pipi");
        petRequest.getTags().add(new TagDto(0, "amazing"));
        Response response = RestAssured.given().accept("application/json").contentType("application/json").body(petRequest)
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
                .pathParam("id", "444414444")
                .when()
                .get("/pet/{id}");


        Long id = Long.parseLong("444414444");

        PetDto petRequest = new PetDto();
        petRequest.setId(id);

        PetDto petResponse = response.as(PetDto.class);
        Assertions.assertEquals(petResponse.getId(), petRequest.getId());

        assertThat(petResponse).usingRecursiveComparison()
                .ignoringFields("tags", "category", "name", "photoUrls", "status")
                .isEqualTo(petRequest);
    }

    @Test
    @Tag("updatePetById")
    @DisplayName("Should be able to update pet using id")
    void updatePetById(){
        Long id = Long.parseLong("444414444");

        PetDto petRequest = new PetDto();
        petRequest.setId(id);
        petRequest.setName("Maja");

        Response response1 = RestAssured.given()
                .accept("application/json")
                .contentType("application/x-www-form-urlencoded")
                .pathParam("id", id)
                .formParam("name", "Maja")
                .when()
                .post("/pet/{id}");

        response1.then()
                .statusCode(HttpStatus.SC_OK);

        Response response2 = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .pathParam("id", id)
                .when()
                .get("/pet/{id}");

        PetDto petResponse = response2.as(PetDto.class);
        Assertions.assertEquals(petRequest.getName(), petResponse.getName());

    }




}

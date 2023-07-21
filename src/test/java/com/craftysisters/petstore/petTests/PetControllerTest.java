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
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

class PetControllerTest extends BaseTest {

    @Test
    @Tag("getPet")
    @DisplayName("Should be able to hit the pet endpoint")
    void getPet() {
        when()
                .get("/pet/{id}", 1)
                .then()
                .statusCode(HttpStatus.SC_OK);
        //.body("status", is("UP"));
    }

    @Test
    @Tag("postPet")
    @DisplayName("Should be able to create pet")
    void postPet() {
        PetDto petRequest = new PetDto();
        petRequest.setName("Pipi");
        petRequest.setStatus("unavailable");
        petRequest.getTags().add(new TagDto(0, "amazing"));
        Response response = RestAssured.given().accept("application/json").contentType("application/json").body(petRequest)
                .when()
                .post("/pet");

        response.then()
                .statusCode(HttpStatus.SC_OK);

        PetDto petResponse = response.as(PetDto.class);
        Assertions.assertEquals(petRequest.getName(), petResponse.getName());
        Assertions.assertNotNull(petResponse.getId());



    }
}

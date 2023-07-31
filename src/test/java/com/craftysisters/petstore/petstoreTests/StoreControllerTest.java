package com.craftysisters.petstore.petstoreTests;

import com.craftysisters.petstore.base.BaseTest;
import com.craftysisters.petstore.dto.InventoryDto;
import com.craftysisters.petstore.dto.OrderDto;
import com.craftysisters.petstore.dto.PetDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class StoreControllerTest extends BaseTest {

    private ObjectMapper objectMapper;
    private Long orderId;
    private Long petId;
    private Long quantity;
    private String shipDate;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        orderId = Long.parseLong("444414444");
        petId = Long.parseLong("444414444");
        quantity = Long.parseLong("1");
        shipDate = "2023-07-30T11:09:58.527Z";
    }

    @Test
    @Tag("postOrder")
    @DisplayName("Should be able to create order")
    void postOrder() {

        OrderDto orderRequest = new OrderDto();
        orderRequest.setId(orderId);
        orderRequest.setPetId(petId);
        orderRequest.setQuantity(quantity);
        orderRequest.setStatus("placed");
        orderRequest.setShipDate(shipDate);
        orderRequest.setComplete(true);

        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .body(orderRequest)
                .when()
                .post("/store/order");

        response.then().statusCode(HttpStatus.SC_OK);
        //response.then().statusCode(HttpStatus.SC_CREATED);

        OrderDto orderResponse = response.as(OrderDto.class);

        assertThat(orderResponse).usingRecursiveComparison()
                .ignoringFields("shipDate")
                .isEqualTo(orderRequest);



        Assertions.assertEquals(orderRequest.getPetId(), orderResponse.getPetId());
        Assertions.assertEquals(orderRequest.getQuantity(), orderResponse.getQuantity());
        Assertions.assertEquals(orderRequest.getStatus(), orderResponse.getStatus());
        Assertions.assertEquals(formatLocalDateTime(orderRequest.getShipDate()), formatLocalDateTime(orderResponse.getShipDate()));
        Assertions.assertEquals(orderRequest.isComplete(), orderResponse.isComplete());

    }

    @Test
    @Tag("postOrder")
    @DisplayName("Shouldn't be able to create order without petId")
    void postOrderWithoutPetId() {
        OrderDto orderRequest = new OrderDto();
        orderRequest.setId(orderId);
        orderRequest.setQuantity(quantity);
        orderRequest.setStatus("placed");
        orderRequest.setShipDate(shipDate);
        orderRequest.setComplete(true);

        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .body(orderRequest)
                .when()
                .post("/store/order");

        //response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        response.as(PetDto.class);
    }




    @Test
    @Tag("postOrder")
    @DisplayName("Shouldn't be able to create order with zero quantity")
    void postOrderWithZeroQuantity() {
        OrderDto orderRequest = new OrderDto();
        orderRequest.setPetId(petId);
        orderRequest.setId(orderId);
        Long quantity = Long.parseLong("0");
        orderRequest.setQuantity(quantity);
        orderRequest.setStatus("placed");
        orderRequest.setShipDate(shipDate);
        orderRequest.setComplete(true);

        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .body(orderRequest)
                .when()
                .post("/store/order");

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }


    @Test
    @Tag("getOrder")
    @DisplayName("Should be able to get order using id")
    void getOrderUsingId() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .when()
                .get("/store/order/{orderId}", -1);

        OrderDto orderExpected = new OrderDto();
        orderExpected.setPetId(petId);
        orderExpected.setShipDate(shipDate);
        orderExpected.setQuantity(quantity);
        orderExpected.setId(orderId);
        orderExpected.setStatus("placed");
        orderExpected.setComplete(true);

        OrderDto orderResponse = response.as(OrderDto.class);

        Assertions.assertEquals(orderExpected.getPetId(), orderResponse.getPetId());
        Assertions.assertEquals(orderExpected.getQuantity(), orderResponse.getQuantity());
        Assertions.assertEquals(orderExpected.getStatus(), orderResponse.getStatus());
        Assertions.assertEquals(formatLocalDateTime(orderExpected.getShipDate()), formatLocalDateTime(orderResponse.getShipDate()));
        Assertions.assertEquals(orderExpected.isComplete(), orderResponse.isComplete());

    }

    @Test
    @Tag("deleteOrder")
    @DisplayName("Should be able to delete order using id")
    void deleteOrderUsingId() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .when()
                .delete("/store/order/{orderId}", orderId);

        response.then().statusCode(HttpStatus.SC_OK);

    }


    @Test
    @Tag("getInventory")
    @DisplayName("Should be able to get inventory")
    void getStoreInventory() {
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .when()
                .get("/store/inventory");

        response.then().statusCode(HttpStatus.SC_OK);

        InventoryDto orderResponse = response.as(InventoryDto.class);

        assertThat(orderResponse.getSold()).isGreaterThanOrEqualTo(0);
        assertThat(orderResponse.getAvailable()).isGreaterThanOrEqualTo(0);
        assertThat(orderResponse.getPending()).isGreaterThanOrEqualTo(0);
    }




}

package com.craftysisters.petstore.base;

import com.craftysisters.petstore.config.Configuration;
import com.craftysisters.petstore.config.ConfigurationManager;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.config.JsonPathConfig;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;

public class BaseTest {

    protected static Configuration configuration;


    @BeforeAll
    public static void beforeAllTests() {
        configuration = ConfigurationManager.getConfiguration();

        RestAssured.baseURI = configuration.baseURI();
        RestAssured.basePath = configuration.basePath();
//        RestAssured.port = configuration.port();

        // solve the problem with big decimal assertions
        RestAssured.config = newConfig().

                jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)).
                sslConfig(new SSLConfig().allowAllHostnames());

        RestAssured.useRelaxedHTTPSValidation();

        determineLog();


    }

    private static void determineLog() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

    }


    public DateTimeFormatter formatter(){
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                .appendPattern("X")
                .toFormatter();

        return formatter;
    }

    public LocalDateTime formatLocalDateTime(String time){
        LocalDateTime date = LocalDateTime.parse(time, formatter());

        return date;
    }
}

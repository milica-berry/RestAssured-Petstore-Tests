package com.craftysisters.petstore.base;

import com.craftysisters.petstore.config.Configuration;
import com.craftysisters.petstore.config.ConfigurationManager;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.config.JsonPathConfig;
import org.junit.jupiter.api.BeforeAll;

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
}

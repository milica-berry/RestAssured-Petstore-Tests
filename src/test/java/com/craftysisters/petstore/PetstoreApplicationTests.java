package com.craftysisters.petstore;

import com.craftysisters.petstore.config.Configuration;
import com.craftysisters.petstore.config.ConfigurationManager;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;

@SpringBootTest
class PetstoreApplicationTests {

	protected static Configuration configuration;


	@BeforeAll
	public static void beforeAllTests() {
		configuration = ConfigurationManager.getConfiguration();

		RestAssured.baseURI = configuration.baseURI();
		RestAssured.basePath = configuration.basePath();
		RestAssured.port = configuration.port();

		// solve the problem with big decimal assertions
		RestAssured.config = newConfig().
				
				jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)).
				sslConfig(new SSLConfig().allowAllHostnames());

		RestAssured.useRelaxedHTTPSValidation();

//        determineLog();
	}
	@Test
	void contextLoads() {
	}

}

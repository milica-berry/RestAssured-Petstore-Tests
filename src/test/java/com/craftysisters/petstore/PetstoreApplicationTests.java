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


	@Test
	void contextLoads() {
	}

}

package com.craftysisters.petstore.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

@LoadPolicy(LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:application.properties"})
public interface Configuration extends Config {

    @Key("petstore.path")
    String basePath();

    @Key("petstore.baseUri")
    String baseURI();

    @Key("petstore.port")
    int port();

}
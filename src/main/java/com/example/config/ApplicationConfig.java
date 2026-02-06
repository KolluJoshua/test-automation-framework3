package com.example.config;

import org.aeonbits.owner.Config;

import com.example.constants.APIConstants;

/**
 * more details to use https://dev.to/eliasnogueira/easily-manage-properties-files-in-java-with-owner-1p6k
 */

@Config.Sources({"classpath:" + APIConstants.AUT_PROPERTY_FILE})
public interface ApplicationConfig extends Config {

    @Key("TestDataFileLocation")
    String testDataLocation();
  
    @Key("BaseAPIURL")
    String baseAPIURL();

    @Key("ApiTemplateLocation")
    String apiTemplateLocation();

    @Key("ApiUserName")
    String apiUserName();

    @Key("ApiUserPassword")
    String apiUserPassword();

}


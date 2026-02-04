package com.example.utils;

import org.aeonbits.owner.ConfigFactory;

import com.example.beans.*;
import com.example.config.ApplicationConfig;


public class ConfigurationDetailsUtil {

    private static com.example.beans.ConfigurationDetails configurationDetails;
    private static ConfigurationDetailsUtil configurationDetailsUtil;

    public ConfigurationDetails getConfigurationDetails() {
        if (configurationDetails == null) {
            configurationDetails = getDetailsFromPropertyFile();
        }
        return configurationDetails;
    }

    public static ConfigurationDetailsUtil getInstance() {
        if (configurationDetailsUtil == null) {
            return configurationDetailsUtil = new ConfigurationDetailsUtil();
        } else {
            return configurationDetailsUtil;
        }
    }

    public ConfigurationDetails getDetailsFromPropertyFile() {
        ConfigurationDetails configurationDetails = new ConfigurationDetails();
        ApplicationConfig appConfig = ConfigFactory.create(ApplicationConfig.class);

       
        // API Details
        APIDetails apiDetails = new APIDetails();
        apiDetails.setBaseAPIURL(appConfig.baseAPIURL());
        apiDetails.setApiTemplateLocation(appConfig.apiTemplateLocation());
        apiDetails.setApiUserName(appConfig.apiUserName());
        apiDetails.setApiUserPassword(appConfig.apiUserPassword());
        configurationDetails.setApiDetails(apiDetails);

        // Resource Details
        ResourceDetails resourceDetails = new ResourceDetails();
        resourceDetails.setTestDataFileLocation(appConfig.testDataLocation());
        configurationDetails.setResourceDetails(resourceDetails);

        return configurationDetails;
    }

}
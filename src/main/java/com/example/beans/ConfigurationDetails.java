package com.example.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigurationDetails {
    private String testDataFileLocation;
    private APIDetails apiDetails;
    private ResourceDetails resourceDetails;

}

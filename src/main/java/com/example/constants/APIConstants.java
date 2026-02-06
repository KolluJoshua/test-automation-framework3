package com.example.constants;

import java.io.File;

public class APIConstants {
    private APIConstants() {
        // Prevent instantiation
    }

    // ===== HTTP Status Codes =====
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_SERVER_ERROR = 500;


    // ===== Environment & System =====
    public static final String AUT = "QA";   //QA, STAGING etc.
    public static final String OS = System.getProperty("os.name");
    public static final String PROJECT_WORKING_DIR = System.getProperty("user.dir");

    // ===== Config Files =====
    public static final String AUT_PROPERTY_FILE = "application.properties";
    public static final String EXTENT_REPORT_FILE_NAME = "API-Automation-Report.html";

    // ===== Paths =====
    public static String REPORTS_FILE_PATH = PROJECT_WORKING_DIR + File.separator + "reports";
    public static final String SCREENSHOTS_DIR = REPORTS_FILE_PATH + File.separator + "screenshots";
    public static String LOGS_DIR_PATH = PROJECT_WORKING_DIR + File.separator + "logs";
    public static String LOGS_ARCHIVE_DIR_PATH = PROJECT_WORKING_DIR + File.separator + "logs\\archive";

  
}

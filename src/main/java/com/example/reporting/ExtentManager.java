package com.example.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.example.beans.ConfigurationDetails;
import com.example.constants.APIConstants;
import com.example.utils.CommonUtil;

import java.io.File;
import java.io.IOException;

public class ExtentManager {

    private final static String EXTENT_CONFIG = "src/test/resources/extent-config.xml";
    private static final Object EXTENT_LOCK = new Object();
    private static volatile ExtentReports extent;

    public static ExtentReports getInstance() {
        ExtentReports localRef = extent;
        if (localRef == null) {
            throw new NullPointerException("ExtentManager instance is not created. Please initialize with method initExtentReport() before running your tests.");
        }
        return localRef;
    }

    //Create an extent report instance
    public static ExtentReports initExtentReport(final ConfigurationDetails configDetails) {
        if (extent != null) {
            return extent;
        }

        synchronized (EXTENT_LOCK) {
            if (extent == null) {
                extent = createExtentReport(configDetails);
            }
            return extent;
        }
    }

    private static ExtentReports createExtentReport(final ConfigurationDetails configDetails) {
        String reportFilepath = APIConstants.REPORTS_FILE_PATH + File.separator + APIConstants.EXTENT_REPORT_FILE_NAME;
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilepath);
        try {
            sparkReporter.loadXMLConfig(EXTENT_CONFIG);
        } catch (IOException e) {
            throw new RuntimeException("Extent Config file path is invalid.");
        }

        ExtentReports reports = new ExtentReports();
        reports.attachReporter(sparkReporter);

        //Set environment details
        reports.setSystemInfo("OS", APIConstants.OS);
        reports.setSystemInfo("AUT", APIConstants.AUT);
        reports.setSystemInfo("HOST", CommonUtil.getHostName());
        return reports;
    }
}
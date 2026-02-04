package com.example.base;

import com.epam.reportportal.testng.ReportPortalTestNGListener;
import com.example.beans.ConfigurationDetails;
import com.example.constants.APIConstants;
import com.example.listeners.CustomNGListener;
import com.example.logs.LogSorter;
import com.example.reporting.ExtentManager;
import com.example.reporting.ExtentTestManager;
import com.example.support.ExcelDataReader;
import com.example.support.FilesHelper;
import com.example.utils.APIUtil;
import com.example.utils.ConfigurationDetailsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;

@Listeners({CustomNGListener.class, ReportPortalTestNGListener.class})

public class BaseTestAPI {
    private static Logger logger = LoggerFactory.getLogger(BaseTestAPI.class);
    protected static ConfigurationDetails configurationDetails;
    protected static ExcelDataReader xlsFile = null;
    protected APIUtil apiUtil;

    //execute the static block first
    static {
        configurationDetails = ConfigurationDetailsUtil.getInstance().getConfigurationDetails();
        xlsFile = new ExcelDataReader(configurationDetails.getResourceDetails().getTestDataFileLocation());
    }

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        logger.debug("initializing logs and reports, creating directories for test runs ....");
        createDirectories();
        ExtentManager.initExtentReport(configurationDetails);
        LogSorter.archiveLogFile();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        ExtentManager.getInstance().flush();
        logger.debug("in suite teardown..");
        LogSorter.sortLog();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestContext iTestContext, Method method) {
        Class<?> testClass;
        try {
            testClass = Class.forName(this.getClass().getName());
            apiUtil = new APIUtil();
            apiUtil.authenticate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        logger.info("test started: " + testClass.getCanonicalName() + "." + method.getName());
        ExtentTestManager.startTest(testClass.getCanonicalName());
    }


    private void createDirectories() {
        FilesHelper.createDirectory(APIConstants.REPORTS_FILE_PATH);
        FilesHelper.createDirectory(APIConstants.SCREENSHOTS_DIR);
    }

}


	

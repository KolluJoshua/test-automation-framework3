package com.example.module;

import com.example.base.BaseTestAPI;
import com.example.constants.APIConstants;
import com.example.exception.CustomException;
import com.example.reporting.ExtentTestManager;
import com.example.utils.CommonUtil;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

public class TestCaceNameTest extends BaseTestAPI {

    private HashMap<String, String> testData;

    @BeforeMethod
    public void setUp() {
        // Get Test data
        String TESTCASE = "TC-01";
        testData = xlsFile.readTestData("MODULE_NAME", TESTCASE);
    }

    @Test(description = "TESTCASE_DESCRIPTION")
    public void testCaseName() throws Exception {
        try {
            // PERFORM TESTCASE LOGIC
        } catch (Exception e) {
            // LOG THE ERROR
            throw new CustomException(e, "TESTCASE_DESCRIPTION. Failed");
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws Exception {
        // CLEAN UP TEST DATA
        // LOG THE TESTCASE RESULT
    }
}
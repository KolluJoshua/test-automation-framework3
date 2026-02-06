package com.example.listeners;

import com.aventstack.extentreports.Status;
import com.example.logs.RequestPayloadRecorder;
import com.example.reporting.ExtentTestManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class CustomNGListener extends TestListenerAdapter implements IInvokedMethodListener {
    private static Logger logger = LoggerFactory.getLogger(CustomNGListener.class);

    @Override
    public void onTestSuccess(final ITestResult tr) {
        RequestPayloadRecorder.persistFor(tr);
        super.onTestSuccess(tr);
    }

    @Override
    public void onTestFailure(final ITestResult tr) {
        logger.error(tr.getThrowable().toString());

        //log fail and take screenshot only when whole test status is pass
        if (ExtentTestManager.getTest().getStatus() == Status.PASS) {
            ExtentTestManager.getTest().fail(tr.getThrowable());
        }
        RequestPayloadRecorder.persistFor(tr);
        super.onTestFailure(tr);
    }

    @Override
    public void onTestSkipped(final ITestResult tr) {
        ExtentTestManager.getTest().skip("skipping the test");
        RequestPayloadRecorder.persistFor(tr);
        super.onTestSkipped(tr);
    }


}
package com.example.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe manager for ExtentReports test instances.
 * Uses ConcurrentHashMap for better performance in parallel test execution.
 */
public class ExtentTestManager {
    // ConcurrentHashMap for thread-safe access without synchronized blocks
    // Key: Thread ID (Long), Value: ExtentTest instance
    private static final ConcurrentHashMap<Long, ExtentTest> extentTestMap = new ConcurrentHashMap<>();
    private static final ExtentReports extent = ExtentManager.getInstance();

    /**
     * Retrieves the ExtentTest instance for the current thread.
     * @return ExtentTest instance for current thread, or null if not started
     */
    public static ExtentTest getTest() {
        return extentTestMap.get(getThreadId());
    }

    /**
     * Flushes the ExtentReports instance to write all test data.
     * Should be called after all tests complete.
     */
    public static synchronized void endTest() {
        extent.flush();
    }

    /**
     * Creates and registers a new test for the current thread.
     * @param testName Name of the test to create
     * @return ExtentTest instance for the current thread
     */
    public static ExtentTest startTest(String testName) {
        Long threadId = getThreadId();
        ExtentTest test = extent.createTest(testName);
        extentTestMap.put(threadId, test);
        return test;
    }

    /**
     * Removes the test instance for the current thread from the map.
     * Useful for cleanup after test completion to prevent memory leaks.
     */
    public static void removeTest() {
        extentTestMap.remove(getThreadId());
    }

    /**
     * Gets the current thread ID in a Java version compatible way.
     * Uses threadId() for Java 19+ or getId() for earlier versions.
     * @return Current thread ID
     */
    @SuppressWarnings("deprecation") // getId() used as fallback for Java < 19
    private static long getThreadId() {
        try {
            // Try Java 19+ method first
            return Thread.currentThread().threadId();
        } catch (NoSuchMethodError e) {
            // Fall back to deprecated getId() for Java < 19
            return Thread.currentThread().getId();
        }
    }
}
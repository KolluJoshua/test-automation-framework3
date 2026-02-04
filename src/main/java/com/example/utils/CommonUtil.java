package com.example.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public static void waitUntilTime(long iTimeInMilliSeconds) {
        try {
            Thread.sleep(iTimeInMilliSeconds);
        } catch (InterruptedException e) {
        }
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.warn("Host name not found for the given machine");
            return "UNKNOWN HOST";
        }
    }

    public static String getCurrentTime() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        return timeStamp;
    }

    public static String getCurrentTime2() {
        String timeStamp = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        return timeStamp;
    }

    public static String getRandomString(String prefix, int length) {
        int randomStringLen = length - prefix.length() - 1;
        String generatedString = "_" + RandomStringUtils.randomAlphanumeric(randomStringLen);
        return prefix.concat(generatedString);
    }

    public static String getMaxRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String convertToXrayTimeFormat(String timeStamp) throws ParseException {
        // Input format pattern
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        // Set input timezone if needed, by default it uses JVM default
        // inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Parse input String to Date
        Date date = inputFormat.parse(timeStamp);

        // Output format pattern
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        // Set desired output timezone, e.g. UTC
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Format Date to output pattern
        return outputFormat.format(date);
    }
}

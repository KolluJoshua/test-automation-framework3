package com.example.exception;

import org.apache.http.client.HttpResponseException;
import org.testng.Assert;

import com.example.reporting.ExtentTestManager;

import javax.net.ssl.SSLException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class CustomException extends Exception {

    public CustomException(Exception e, String customMessage) {
        super(e);
        String message = resolveMessage(e, customMessage);
        String logMessage = enrichWithStackTraceIfNeeded(e, message);
        logAndFail(logMessage);
    }
    public CustomException(Exception e) {
        super(e);
        String message = resolveMessage(e, null);
        String logMessage = enrichWithStackTraceIfNeeded(e, message);
        logAndFail(logMessage);
    }

    public String getExceptionStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }



    private String resolveMessage(Exception e, String customMessage) {
        String detail = safeMessage(e);
        String baseMessage;

        if (e instanceof SocketTimeoutException) {
            baseMessage = "API request timed out";
        } else if (e instanceof ConnectException) {
            baseMessage = "Unable to connect to API endpoint";
        } else if (e instanceof UnknownHostException) {
            baseMessage = "Unable to resolve API host";
        } else if (e instanceof SSLException) {
            baseMessage = "SSL handshake failed during API call";
        } else if (e instanceof HttpResponseException httpResponseException) {
            String reason = httpResponseException.getReasonPhrase();
            baseMessage = "API responded with HTTP ".concat(String.valueOf(httpResponseException.getStatusCode()));
            detail = reason != null && !reason.isBlank() ? reason : detail;
        } else if (e.getClass().getSimpleName().equals("HttpResponseException")) {
            baseMessage = "API responded with an HTTP error";
        } else {
            baseMessage = "API exception (" + e.getClass().getSimpleName() + ")";
        }

        return formatMessage(baseMessage, customMessage, detail);
    }

    private String enrichWithStackTraceIfNeeded(Exception e, String baseMessage) {
        if (isKnownApiException(e)) {
            return baseMessage;
        }
        return baseMessage + System.lineSeparator() + getExceptionStackTrace(e);
    }

    private void logAndFail(String message) {
        ExtentTestManager.getTest().fail(message);
        Assert.fail(message);
    }

    private boolean isKnownApiException(Exception e) {
        return e instanceof SocketTimeoutException
                || e instanceof ConnectException
                || e instanceof UnknownHostException
                || e instanceof SSLException
                || e instanceof HttpResponseException
                || e.getClass().getSimpleName().equals("HttpResponseException");
    }

    private String formatMessage(String baseMessage, String customMessage, String detail) {
        StringBuilder builder = new StringBuilder(baseMessage);
        if (customMessage != null && !customMessage.isBlank()) {
            builder.append(" - ").append(customMessage.trim());
        }
        if (detail != null && !detail.isBlank()) {
            builder.append(" : ").append(detail.trim());
        }
        return builder.toString();
    }

    private String safeMessage(Exception e) {
        return e.getMessage() != null ? e.getMessage() : e.toString();
    }
}

package com.metcash.delilah.logger;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.metcash.delilah.listener.ExtentTestNGITestListener;

public class ExtentLogger {

    public synchronized static void logInfo(String log) {
        if (ExtentTestNGITestListener.getTest().get() != null)
            ExtentTestNGITestListener.getTest().get().log(Status.INFO, log);
    }

    public synchronized static void logFail(String log) {
        if (ExtentTestNGITestListener.getTest().get() != null)
            ExtentTestNGITestListener.getTest().get().log(Status.FAIL, log);
    }

    public synchronized static void logSkip(String log) {
        if (ExtentTestNGITestListener.getTest().get() != null)
            ExtentTestNGITestListener.getTest().get().log(Status.SKIP, MarkupHelper.createLabel(log, ExtentColor.ORANGE));
    }

    public synchronized static void logDebug(String log) {
        if (ExtentTestNGITestListener.getTest().get() != null)
            ExtentTestNGITestListener.getTest().get().log(Status.DEBUG, MarkupHelper.createLabel(log, ExtentColor.LIME));
    }

    public synchronized static void logPass(String log) {
        if (ExtentTestNGITestListener.getTest().get() != null)
            ExtentTestNGITestListener.getTest().get().log(Status.PASS, log);
    }

    public synchronized static void logException(Status status, Throwable throwable) {
        if (ExtentTestNGITestListener.getTest().get() != null)
            ExtentTestNGITestListener.getTest().get().log(status, throwable);
    }
}

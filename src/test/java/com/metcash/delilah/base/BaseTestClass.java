package com.metcash.delilah.base;

import com.metcash.delilah.generic.GenericFunctions;
import com.metcash.delilah.listener.ExtentTestNGITestListener;
import com.metcash.delilah.listener.InvocationListener;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({InvocationListener.class, ExtentTestNGITestListener.class})
public class BaseTestClass {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) throws Exception {
        System.out.println("--------------------- INSIDE BEFORE SUITE ---------------------");
        GenericFunctions.initDrivers();
        GenericFunctions.addExecutionDetailsToExtent();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        System.out.println("--------------------- INSIDE AFTER SUITE ---------------------");
        GenericFunctions.quitAllBrowsers();
    }

}

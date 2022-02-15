package com.metcash.delilah.listener;

import com.metcash.delilah.driver.DriverManager;
import com.metcash.delilah.global.GlobalData;
import com.metcash.delilah.logger.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class InvocationListener implements IInvokedMethodListener {

    @Override
    public synchronized void beforeInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            Logger.log("***********************  " + result.getName()
                    + " Has Started Executing" + "  **************************");
            DriverManager.setDriver(GlobalData.freePool.remove());
        }
    }

    @Override
    public synchronized void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            GlobalData.freePool.add(DriverManager.getDriver());
        }
    }
}
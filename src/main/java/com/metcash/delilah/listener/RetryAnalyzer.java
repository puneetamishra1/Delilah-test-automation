package com.metcash.delilah.listener;

import com.metcash.delilah.global.GlobalData;
import com.metcash.delilah.logger.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int counter = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (counter < GlobalData.RETRYLIMIT) {
            counter++;
            Logger.log("========== Retrying - " + result.getName() + " ===========", false);
            return true;
        } else {
            return false;
        }
    }

}

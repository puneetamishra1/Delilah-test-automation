package com.metcash.delilah.extent;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.metcash.delilah.global.GlobalData;

import static com.metcash.delilah.global.GlobalData.*;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports createInstance(String fileName) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("ALM");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Automation Report");
        htmlReporter.config().setJS("$('.brand-logo').text('ALM');");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        return extent;
    }

    public static void addExecutionDetails_ExtentReport() {
        extent.setSystemInfo("Environment", ENVIRONMENT.toUpperCase());
        extent.setSystemInfo("Driver Type", DRIVER_TYPE.toUpperCase());
        extent.setSystemInfo("Execution Type", EXECUTION_TYPE.toUpperCase());

        if (!EXECUTION_TYPE.equalsIgnoreCase("system"))
            extent.setSystemInfo("Groups", getFomattedGroupNames(GNAME.toUpperCase()));

        extent.setSystemInfo("No of Threads", "" + GlobalData.NOOFTHREADS);

    }


    private static String getFomattedGroupNames(String groupNames) {
        StringBuilder finalStr = new StringBuilder();
        for (String str : groupNames.split(",")) {
            if (str.contains(".")) {
                finalStr.append(str.split("\\.")[1]).append("<br>");
            } else {
                finalStr.append(str).append("<br>");
            }
        }
        return finalStr.toString();
    }
}

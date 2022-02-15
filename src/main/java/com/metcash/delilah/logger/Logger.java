package com.metcash.delilah.logger;

import com.metcash.delilah.utils.Utils;
import org.apache.log4j.PropertyConfigurator;

public class Logger {
    static String filepathLog4J = Utils.getPropertyValue("Log4jFilePath");

    /***********************************************************************************************
     * Function Description : this function is use to print the log using log4j properties file
     * author: Varun Vashishtha, date: 01-May-2018
     * *********************************************************************************************/
    public static void log(String logValue) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(logValue);
        PropertyConfigurator.configure(filepathLog4J);
        logger.info(logValue);
        ExtentLogger.logInfo(logValue);
    }

    public static void log(String logValue, Boolean extent) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(logValue);
        PropertyConfigurator.configure(filepathLog4J);
        logger.info(logValue);
        if (extent)
            ExtentLogger.logInfo(logValue);
    }
}

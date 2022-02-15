package com.metcash.delilah.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class Utils {

    /***********************************************************************************************
     * Function Description : It returns the value for the provided key from config file
     * author: Varun Vashishtha, date: 01-May-2018
     * *********************************************************************************************/
    public static String getPropertyValue(String propertyName) {

        String propertyValue = "";
        try {
            FileInputStream input = new FileInputStream("./Configuration/Config.properties");
            Properties prop = new Properties();
            prop.load(input);
            propertyValue = prop.getProperty(propertyName);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return propertyValue;
    }
}

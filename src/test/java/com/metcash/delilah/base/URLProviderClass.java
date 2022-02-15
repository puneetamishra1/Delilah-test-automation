package com.metcash.delilah.base;

import com.metcash.delilah.global.GlobalData;

public class URLProviderClass {
    static String environment = GlobalData.ENVIRONMENT;


    public static String Return_ALMLoginURL() {
        if (environment.equalsIgnoreCase("local"))
            return "";
        else if (environment.equalsIgnoreCase("dev"))
            return "";
        else
            return "";
    }

}
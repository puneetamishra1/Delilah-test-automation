package com.metcash.delilah.global;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GlobalData {

    public static final int DEFAULT_IMPLICITWAIT = 10;
    public static final int ELEMENT_TIMEOUT = 20;
    public static final int RETRYLIMIT = 1;
    public static final String ENVIRONMENT = System.getProperty("environment", "staging");
    public static final String DRIVER_TYPE = System.getProperty("driver_Type", "Chrome");
    public static final int NOOFTHREADS = Integer.parseInt(System.getProperty("threads", "1"));
    public static final String EXECUTION_TYPE = System.getProperty("execution_type", "system");
    public static final String GNAME = System.getProperty("gname", "regression");

    public static BlockingQueue<WebDriver> freePool = new ArrayBlockingQueue<>(20);

    static {
        System.out.println("Environment = " + GlobalData.ENVIRONMENT);
        System.out.println("Driver Type = " + GlobalData.DRIVER_TYPE);
        System.out.println("Execution Type = " + GlobalData.EXECUTION_TYPE);
        if (GNAME.length() > 0)
            System.out.println("Group Names = " + GlobalData.GNAME);

        System.out.println("No of Threads = " + GlobalData.NOOFTHREADS);
    }
}

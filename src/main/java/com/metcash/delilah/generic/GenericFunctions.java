package com.metcash.delilah.generic;

import com.metcash.delilah.global.GlobalData;
import com.metcash.delilah.extent.ExtentManager;
import com.metcash.delilah.logger.Logger;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class GenericFunctions {

    public static synchronized WebDriver startDriver() {
        WebDriver driver = null;

        try {
            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("Chrome")) {
                Logger.log("--------Starting Chrome Driver-------");
                HashMap<String, Object> chromePrefs = new HashMap<>();
                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("download.default_directory", System.getProperty("user.dir"));
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", chromePrefs);
                options.addArguments("--disable-popup-blocking");
                driver = new ChromeDriver(options);
                driver.manage().window().maximize();
            }

            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("ChromeHeadless")) {
                Logger.log("--------Starting ChromeHeadless Driver-------");
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.setHeadless(true);
                options.addArguments("window-size=1920x1080");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(options);
            }

            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("Firefox")) {
                Logger.log("--------Starting Firefox Driver-------");
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                driver.manage().window().maximize();
            }

            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("FirefoxHeadless")) {
                Logger.log("--------Starting FirefoxHeadless Driver-------");
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                options.setHeadless(true);
                options.addArguments("window-size=1920x1080");
                driver = new FirefoxDriver(options);
            }

            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("Safari")) {
                Logger.log("--------Starting Safari Driver-------");
                driver = new SafariDriver();
                driver.manage().window().maximize();
            }

            assert driver != null;
            driver.manage().timeouts().implicitlyWait(GlobalData.DEFAULT_IMPLICITWAIT, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return driver;
    }

    public static synchronized void initDrivers() throws Exception {
        for (int i = 0; i < GlobalData.NOOFTHREADS; i++) {
            GlobalData.freePool.put(startDriver());
        }

        System.out.println(GlobalData.freePool.size());
    }


    public static synchronized void quitAllBrowsers() {
        for (WebDriver driver : GlobalData.freePool) {
            driver.quit();
        }
    }

    public static synchronized void addExecutionDetailsToExtent() {
        ExtentManager.addExecutionDetails_ExtentReport();
    }

}
package com.metcash.delilah.listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.metcash.delilah.action.ActionHelper;
import com.metcash.delilah.driver.DriverManager;
import com.metcash.delilah.extent.ExtentManager;
import com.metcash.delilah.logger.ExtentLogger;
import com.metcash.delilah.logger.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;


public class ExtentTestNGITestListener implements ITestListener, ISuiteListener {
    private static final String directory = "./extentreport/";
    private static final String oldReportsDirectory = "./Archived_Reports/";
    private static final String subDirectory = "screenshots/";
    private static String qualifiedMethodName = "";
    private static final ExtentReports extent = ExtentManager.createInstance(directory + "ALM_extentReport.html");
    private static final Hashtable<String, ExtentTest> parentTestMap = new Hashtable<>();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ThreadLocal<ExtentTest> getTest() {
        return test;
    }

    public static void setTest(ThreadLocal<ExtentTest> test) {
        ExtentTestNGITestListener.test = test;
    }

    /**************************************************************
     * Function Description:This method is used for archiving the extent report folder with date and time
     *Author: Puneeta Mishra, Date: 03-May-2018
     ***************************************************************/
    public static void copyReportToOld() {
        File oldDirectory = new File(directory);
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ssaa");
        String dateName = dateFormat.format(new Date());
        File newDirectory = new File(oldReportsDirectory + dateName);
        try {
            FileUtils.copyDirectory(oldDirectory, newDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onStart(ITestContext context) {
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String className = result.getMethod().getRealClass().getSimpleName();

        ExtentTest parent;
        if (parentTestMap.get(className) == null) {
            parent = extent.createTest(className);
            parentTestMap.put(className, parent);
        } else {
            parent = parentTestMap.get(className);
        }
        ExtentTest child = parent.createNode(result.getName(), result.getMethod().getDescription());
        System.out.println("Testcase =========== " + result.getName() + " ========== has started executing");
        qualifiedMethodName = className + "_" + result.getName();
        test.set(child);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        test.get().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
        String MethodName = result.getName();
        Logger.log("Testcase============== " + MethodName + " ============has Passed Successfully", false);
        ExtentLogger.logPass(MethodName+" has passed");
        extent.flush();

    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());
        test.get().fail(MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
        String MethodName = result.getName();
        try {
            test.get().addScreenCaptureFromPath(getPath_CaptureScreenShot());
        } catch (IOException e) {

            e.printStackTrace();
        }
        Logger.log("Testcase============== " + MethodName + " ============== has failed", false);
        ExtentLogger.logFail(MethodName+" has failed");
        extent.flush();
        try{
            ActionHelper.refresh();
            ActionHelper.goToSleep(2000);
            Alert alert = DriverManager.getDriver().switchTo().alert();
            alert.accept();
        }
        catch (Exception e){
            Logger.log("Alert is not present");
        }
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        if (result.wasRetried()) {
            extent.removeTest(getTest().get());
        }
        test.get().info(result.getThrowable());
        test.get().skip(MarkupHelper.createLabel("Test Skipped", ExtentColor.ORANGE));
        String MethodName = result.getName();
        Logger.log("Testcase================ " + MethodName + " ============== has Skipped", false);
        ExtentLogger.logSkip(MethodName+" has skipped");
        extent.flush();
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    /**************************************************************
     * Function Description:This method is used for capturing screenshots for failed testcases
     *Author: Puneeta Mishra, Date: 03-May-2018
     ***************************************************************/
    public synchronized String getPath_CaptureScreenShot() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ssaa");
            Date date = new Date();
            String dateName = dateFormat.format(date);
            String filePathExtent = subDirectory + qualifiedMethodName + "_" + dateName + ".png";
            String filePath = directory + filePathExtent;
            File f = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(f, new File(filePath));
            System.out.println("================== Took Screenshot through Selenium ==================");
            return filePathExtent;
        } catch (Exception e) {
            System.out.println(
                    "================== Some Exception occurred while getting screenshot ==================");
        }
        return null;
    }

    public void createDirectory() {
        try {
            File file = new File(directory + subDirectory);
            FileUtils.forceMkdir(file);
            FileUtils.cleanDirectory(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(ISuite arg0) {

        createDirectory();
    }

    @Override
    public void onFinish(ISuite arg0) {
        copyReportToOld();
    }


    /**************************************************************
     * Function Description:This method is called for sending an auto generated mail to the recipients with attached reports
     *Author: Puneeta Mishra, Date: 19-Nov-2018
     ***************************************************************/
    public void sendReport() {
        final String username = "automatesupplier1@gmail.com";
        final String password = "nagarro@123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("automatesupplier1@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("puneeta.mishra@nagarro.com"));
            message.setSubject("ALM Automation Report");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("Hi All," + "\n\nPFA automation execution report for ALM." + "\n\nregards\nALM Automation Team");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String file = "./extentreport/ALM.html";
            String filename = "ALM_extentReport.html";
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);


            // Send the complete message parts
            message.setContent(multipart);


            Transport.send(message);

            Logger.log("Mail sent to puneeta.mishra@nagarro.com");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
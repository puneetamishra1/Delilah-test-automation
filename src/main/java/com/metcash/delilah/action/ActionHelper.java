package com.metcash.delilah.action;

import com.metcash.delilah.driver.DriverManager;
import com.metcash.delilah.global.GlobalData;
import com.metcash.delilah.logger.ExtentLogger;
import com.metcash.delilah.logger.Logger;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class ActionHelper {
    static String userDirectory = System.getProperty("user.dir");
    static ThreadLocal<String> winHandle = new ThreadLocal<>();



    /***********************************************************************************************
     * Function Description : Opens a URL
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void openURL(String url) {
        DriverManager.getDriver().get(url);
        ExtentLogger.logInfo("Opening Url : " + url);
    }

    /***********************************************************************************************
     * Function Description : Static Wait
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void goToSleep(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /***********************************************************************************************
     * Function Description : Wait until Element is Visible
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void waitUntilElementVisible(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), GlobalData.ELEMENT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void waitUntil(String element, int timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeInSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
    }
    /***********************************************************************************************
     * Function Description : Find Elements
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static WebElement findElement(By by) {
        waitUntilElementVisible(by);
        return DriverManager.getDriver().findElement(by);
    }

    public static WebElement findElement(String xpath) {
        waitUntilElementVisible(By.xpath(xpath));
        return DriverManager.getDriver().findElement(By.xpath(xpath));
    }
    public static WebElement findElements(String xpath) {
        waitUntilElementVisible(By.xpath(xpath));
        return DriverManager.getDriver().findElement(By.xpath(xpath));
    }
    public static List<WebElement> findElements(By by) {
        waitUntilElementVisible(by);
        return DriverManager.getDriver().findElements(by);
    }

    /***********************************************************************************************
     * Function Description : Gets attribute of the WebElement
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static String getAttribute(String xpath, String attribute) {
        return findElement(xpath).getAttribute(attribute);
    }
    public static String getAttribute(By by) {
        String value = findElement(by).getAttribute("name");
        ExtentLogger.logInfo("Fetching value of " + getCallingMethodName() + " [ " + value + " ]");
        return value;
    }


    /***********************************************************************************************
     * Function Description : It checks if element is present or not
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static boolean isPresentWithWait(By by) {
        WebDriver driver = DriverManager.getDriver();
        try {
            waitUntilElementVisible(by);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setImplicitWaitInSeconds(0);
        if (driver.findElements(by).size() > 0 && driver.findElement(by).isDisplayed()) {
            return true;
        } else {
            setImplicitWaitInSeconds(GlobalData.DEFAULT_IMPLICITWAIT);
            return false;
        }
    }

    public static boolean isPresent(String xpath) {
        boolean flag = false;
        WebDriver driver = DriverManager.getDriver();
        setImplicitWaitInSeconds(0);
        if (driver.findElements(By.xpath(xpath)).size() > 0) {
            if (driver.findElement(By.xpath(xpath)).isDisplayed()) {
                flag = true;
            }
        }
        setImplicitWaitInSeconds(GlobalData.DEFAULT_IMPLICITWAIT);
        return flag;
    }

    public static boolean isPresent(By by) {
        boolean flag = false;
        WebDriver driver = DriverManager.getDriver();
        setImplicitWaitInSeconds(0);
        if (driver.findElements(by).size() > 0) {
            if (driver.findElement(by).isDisplayed()) {
                flag = true;
            }
        }
        setImplicitWaitInSeconds(GlobalData.DEFAULT_IMPLICITWAIT);
        return flag;
    }

    public static boolean isPresent(By by, int waitTimeMillis) {
        if (isPresent(by))
            return true;


        int i = 1;
        while (i * 1000 <= waitTimeMillis) {
            if (isPresent(by))
                return true;
            i++;
        }
        return false;
    }

    /***********************************************************************************************
     * Function Description :Upload files via sending path of the file to the input type WebElement
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/

    public static void uploadFile(String xpath, String input) {
        WebDriver driver = DriverManager.getDriver();
        driver.findElement(By.xpath(xpath)).sendKeys(input);
        Logger.log(input);
        ExtentLogger.logInfo("Upload " + getCallingMethodName() + " = " + input);
    }

    /***********************************************************************************************
     * Function Description : Gets text of the WebElement
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static String getText(String xpath) {
        String text = findElement(By.xpath(xpath)).getText();
        ExtentLogger.logInfo("Fetching value of " + getCallingMethodName() + " [ " + text + " ]");
        return text;
    }

    /***********************************************************************************************
     * Function Description : Gets text from a list
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static String getTextFromList(By by, String index) {
        return findElements(by).get(Integer.parseInt(index)).getText();
    }

    /***********************************************************************************************
     * Function Description :clicks on WebElement  from list
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void clickFromList(By by, String index)
    {
        findElements(by).get(Integer.parseInt(index)).click();
    }

    /***********************************************************************************************
     * Function Description : Click on WebElement
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void click(String xpath, String input) {
        By by = By.xpath(xpath.replace("xyz", input));
        findElement(by).click();
        ExtentLogger.logInfo("Click on " + getCallingMethodName() + " = " + input);
    }

    public static void click(By by) {
        findElement(by).click();
        ExtentLogger.logInfo("Click with Selenium on = " + getCallingMethodName());
    }

    public static void click(String xpath) {
        waitUntilElementVisible(By.xpath(xpath));
        findElement(xpath).click();
        ExtentLogger.logInfo("Click with Selenium on = " + getCallingMethodName());
    }

    public static void click(WebElement we) {
        we.click();
        ExtentLogger.logInfo("Click on " + getCallingMethodName());
    }

    /***********************************************************************************************
     * Function Description : Uncheck a checkbox
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void uncheckCheckbox(By by) {
        WebElement me = findElement(by);
        if (me.getAttribute("checked").equalsIgnoreCase("true")) {
            me.click();
        }
        ExtentLogger.logInfo("UnChecking Checkbox " + getCallingMethodName());
    }

    /***********************************************************************************************
     * Function Description : check a checkbox
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void checkCheckbox(By by) {
        WebElement me = findElement(by);
        if (me.getAttribute("checked").equalsIgnoreCase("false")) {
            me.click();
        }
        ExtentLogger.logInfo("Checking Checkbox " + getCallingMethodName());
    }

    /***********************************************************************************************
     * Function Description : get method name
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static String getCallingMethodName() {
        String methodName;
        try {
            String[] str = Thread.currentThread().getStackTrace()[3].getMethodName().split("_");
            StringBuilder newStr = new StringBuilder();
            for (int i = 0; i < str.length; i++) {
                if (i == 0 || i == str.length - 1) {
                }
                else
                    newStr.append(str[i]).append("_");
            }

            methodName = newStr.substring(0, newStr.length() - 1);
        } catch (Exception e) {
            methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        }
        return methodName;
    }

    /***********************************************************************************************
     * Function Description : It send data (keyboard) to th Input field
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void sendKeys(By by, String input) {
        findElement(by).clear();
        findElement(by).sendKeys(input);
        ExtentLogger.logInfo("Fill Text Box = " + getCallingMethodName() + ", with value = " + input);
    }

    public static void sendKeys(String xpath, String input) {
//        findElement(xpath).clear();
        findElement(xpath).sendKeys(input);
        ExtentLogger.logInfo("Fill Text Box = " + getCallingMethodName() + ", with value = " + input);
    }
    /***********************************************************************************************
     * Function Description : It clears the text field and sends data (keyboard) to th Input field
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void sendKeysWithClear(By by, String input ){
        sendKeys(by, input);
    }

    /***********************************************************************************************
     * Function Description : It send enter key
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void Enter(By by)
    {
        findElement(by).sendKeys(Keys.ENTER);
    }

    /***********************************************************************************************
     * Function Description : Gets page title
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static String getPageTitle() {
        String title = DriverManager.getDriver().getTitle();
        ExtentLogger.logInfo("Fetching Page Title = " + title);
        return title;

    }

    /***********************************************************************************************
     * Function Description : Navigates back to the URL
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void goBack() {
        DriverManager.getDriver().navigate().back();
    }

    /***********************************************************************************************
     * Function Description : WaitForCompletePageLoad:
     * This function is used when we want the complete page to get load without dependency on element presence.
     * Usefulness/Functionality : It can be used in cases where the JS is loaded only after complete page is loaded.
     For example: In case of Suggester testing where suggestions are loaded only after complete page is loaded.
     So, instead of using excessive sleep every time, this method can be used effectively.
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void waitForCompletePageLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), 30);
        wait.until(pageLoadCondition);
    }

    /***********************************************************************************************
     * Function Description : Closes the window
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void closeNewWindow() {
        DriverManager.getDriver().close();
    }

    /***********************************************************************************************
     * Function Description : Waits until the In-visibility of a WebELement
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void WaitUntilInvisibilityOfElement(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), 180);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));

    }
    /***********************************************************************************************
     * Function Description : Gets current URL
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static String getCurrentURL() {
        ActionHelper.waitForCompletePageLoad();
        return DriverManager.getDriver().getCurrentUrl();
    }

    /***********************************************************************************************
     * Function Description : This method is to send PAGE_DOWN keys
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void pageDown() {
        Actions action = new Actions(DriverManager.getDriver());
        action.sendKeys(Keys.PAGE_DOWN).build().perform();
    }
    /***********************************************************************************************
     * Function Description : This method is to send PAGE_UP keys
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void pageUp() {
        Actions action = new Actions(DriverManager.getDriver());
        action.sendKeys(Keys.PAGE_UP).build().perform();
    }
    /***********************************************************************************************
     * Function Description : It returns the selected WebElement
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static boolean isSelected(By by) {
        WebDriver driver = DriverManager.getDriver();
        return driver.findElement(by).isSelected();
    }

    /***********************************************************************************************
     * Function Description : Switch to new window
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void switchtoNewWindow() {
        ActionHelper.goToSleep(1000);
        WebDriver driver = DriverManager.getDriver();
        winHandle.set(driver.getWindowHandle());

        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
        ActionHelper.goToSleep(1000);
    }
    /***********************************************************************************************
     * Function Description : Switch to main window
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void switchToMainWindow() {
        WebDriver driver = DriverManager.getDriver();
        for (String winHandle : driver.getWindowHandles()) {
            if (!winHandle.equals(ActionHelper.winHandle.get())) {
                driver.close();
            }
        }
        ActionHelper.goToSleep(1000);
        driver.switchTo().window(ActionHelper.winHandle.get());
        ActionHelper.goToSleep(1000);
    }
    /***********************************************************************************************
     * Function Description : Refresh the page
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void refresh() {
        Logger.log("Refresh the page");
        DriverManager.getDriver().navigate().refresh();
    }
    /***********************************************************************************************
     * Function Description : Delete all cookies of the current page
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static void deleteAllCookies() {
        DriverManager.getDriver().manage().deleteAllCookies();
    }

    /***********************************************************************************************
     * Function Description : Sets implicit Wait by accepting timeout in seconds
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/

    public static String setImplicitWaitInSeconds(int timeOut) {
        DriverManager.getDriver().manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
        return "Timeout set to " + timeOut + " seconds.";
    }

    /***********************************************************************************************
     * Function Description : Sets implicit Wait by accepting timeout in milliseconds
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static String SetImplicitWaitInMilliSeconds(int timeOut) {
        DriverManager.getDriver().manage().timeouts().implicitlyWait(timeOut, TimeUnit.MILLISECONDS);
        return "Timeout set to " + timeOut + " milli seconds.";
    }

    /***********************************************************************************************
     * Function Description : Find the size of WebElement (count)
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public static int findElementsSize(String xpath) {
        WebDriver driver = DriverManager.getDriver();
        ExtentLogger.logInfo("Click with Selenium on = " + getCallingMethodName());
        return driver.findElements(By.xpath(xpath)).size();
    }
    /**************************************************************
     * Function Description: Enters a provided text into a text box Function
     * author: Puneeta Mishra, date: 01-May-2018
     ***************************************************************/
    public static String fill_Txt(String xpath, String data) {
        findElement(xpath).clear();
        goToSleep(2000);
        findElement(xpath).click();
        findElement(xpath).sendKeys(data);
        ExtentLogger.logInfo("Fill Text Box = " + getCallingMethodName() + ", with value = " + data);
        return "Element entered in text box successfully";
    }

    public static String enterText(String xpath, String data) {
        findElement(xpath).clear();
        goToSleep(2000);
        findElement(xpath).click();
        findElement(xpath).sendKeys(data);
        ExtentLogger.logInfo("Fill Text Box = " + getCallingMethodName() + ", with value = " + data);
        return "Element entered in text box successfully";
    }
    /**************************************************************
     * Function Description: This method will accept the alert box
     * author: Puneeta Mishra, date: 21-Jul-2020
     ***************************************************************/
    public static String alert() {
        WebDriver driver = DriverManager.getDriver();
        driver.switchTo(). alert().accept();
        ExtentLogger.logInfo("Accept alert Box = " + getCallingMethodName() );
        return "Accept the alert boc";
    }

    /**************************************************************
     * Function Description: This method will set config values
     * author: Puneeta Mishra, date: 21-Jul-2020
     ***************************************************************/
    public static void setPropertyValue(String environment, String execution_Type, String driver_Type) {
        Logger.log("Setting config variables");

        Properties prop = new Properties();
        OutputStream output = null;

        try {
            prop.setProperty("environment", environment);
            prop.setProperty("execution_Type", execution_Type);
            prop.setProperty("driver_Type", driver_Type);
            prop.setProperty("alm_TestData", "./TestCasesCreation/alm_TestData.xlsx");
            prop.setProperty("Log4jFilePath", "Log4j.properties");
            prop.setProperty("Browser", driver_Type);
            prop.setProperty("ChromeDriverPath", "./drivers/chromedriver.exe");

            // save properties to project root folder
            prop.store(new FileOutputStream("./configuration/Config.properties"), null);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    /*****************************************************************************************************
     * Function Description : This Function is use to get one day previous date than current date
     * Author: Varun Vashishtha, date: 24-May-2018
     * *************************************************************************************************/
    public static String getPreviousDate() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy/");

        // Create a calendar object with today date. Calendar is in java.util package.
        Calendar calendar = Calendar.getInstance();

        // Move calendar to yesterday
        calendar.add(Calendar.DATE, -1);

        // Get current date of calendar which point to the yesterday now
        Date yesterday = calendar.getTime();

        return dateFormat.format(yesterday);
    }

    /*****************************************************************************************************
     * Function Description : This Function is use to get random future date in "dd/MM/yyyy" format.
     * Author: Varun Vashishtha, date: 25-May-2018
     @return : date in dd/MM/yyyy format.
      * *************************************************************************************************/
    public static String getRandomFutureDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        String str_date1 = (dateFormat.format(cal.getTime()));
        String str_date2 = "31/12/2099";

        try {
            cal.setTime(dateFormat.parse(str_date1));

            long value1 = cal.getTimeInMillis();

            cal.setTime(dateFormat.parse(str_date2));
            long value2 = cal.getTimeInMillis();

            long value3 = (long) (value1 + Math.random() * (value2 - value1));
            cal.setTimeInMillis(value3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(cal.getTime());
    }

    /***********************************************************************************************
     * Function Description : This method used to get day of a particular date
     * author: Puneeta mishra, date: 12-May-2020
     *  *********************************************************************************************/
    public static String getDayStringOld(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = sdf.parse(date);
        DateFormat format2 = new SimpleDateFormat("EEEE");
        String finalDay = format2.format(d1);
        Logger.log("Week day=" + finalDay);
        return finalDay;
    }

    /*******************************************************************************************
     * Function Description : This Function is use to get  fixed future  date in "dd/MM/yyyy" format. Need to pass number of days.
     * Author: Kapil Gupta, date: 14-June-2018
     @return : Fixed future date in dd/MM/yyyy format.
      * ****************************************************************************************/
    public static String getFixedFutureDate(int Add_Days) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, Add_Days); // Adding 5 days
        return sdf.format(c.getTime());
    }

    /*******************************************************************************************
     * Function Description : This Function is use to get  current date in "dd/MM/yyyy" format.
     * Author: Varun Vashishtha, date: 25-May-2018
     @return : date in dd/MM/yyyy format.
      * ****************************************************************************************/
    public static String getCurrentDate() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return (dateFormat.format(date));
    }

    /*****************************************************************************************************
     * Function Description : This Function move your cursor to particular element.
     * Author: Varun Vashishtha, date: 26-May-2018.
     @param : Element on which you want to hover cursor.
      * *************************************************************************************************/
    public static void moveCursorTo(WebElement element) {
        Actions builder = new Actions(DriverManager.getDriver());
        Action moveElement = builder
                .moveToElement(element)
                .build();
        moveElement.perform();
    }

    /***********************************************************************************************
     * Function Description : This method used to check if the file exist
     * author: Puneeta mishra, date: 23-May-2018
     * *********************************************************************************************/
    public static boolean downloadedFileExists(String filename, String extension) {
        File newFile = new File(userDirectory + "/" + filename + "." + extension);
        Logger.log("Verify the downloaded file exists or not");
        File[] files = new File(userDirectory).listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().contains(filename)) {
                file.renameTo(newFile);
                return true;

            }
        }
        return false;

    }

    /***********************************************************************************************
     * Function Description : This method used delete an existing file
     * author: Puneeta mishra, date: 24-May-2018
     * *********************************************************************************************/
    public static boolean deleteFile(String filename) {
        Logger.log("Delete the files with name:-"+filename);
        File[] files = new File(System.getProperty("user.dir")).listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().contains(filename)) {
                file.delete();
                Logger.log("Downloaded file Deleted:-" + filename);
                return true;
            }
        }
        return false;

    }

    /***********************************************************************************************
     * Function Description : This method used to generate random number.
     * author: Kapil Gupta, date: 04-june-2018
     * @return : Return random integer value.
     * *********************************************************************************************/
    public static int randomNumber() {
        Random random = new Random();
        return random.nextInt(99999);
    }


    /***********************************************************************************************
     * Function Description : This method is used to generate GTIN
     * author: Puneeta Mishra, date: 26-june-2018
     * *********************************************************************************************/
    public static String GTIN_generatorNew() {
        long num, multipleofTen, gtin = 0;
        int count = 12, i;
        String dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String date = dateFormat.substring(5, 18);
        date = date.replace(".", "");
        int data = Integer.parseInt(date);
        long generatedLong = 999000000000L + data;
        String TradeGtin = "" + generatedLong;

        while (generatedLong != 0) {
            num = generatedLong % 10;
            if (count % 2 == 0) {
                gtin = gtin + num * 3;
            } else {
                gtin = gtin + num;

            }
            count--;
            generatedLong = generatedLong / 10;
        }

        if (gtin % 10 == 0) {
            multipleofTen = 10;
        } else {
            multipleofTen = gtin % 10;
        }
        long newgting = 10 - multipleofTen;
        TradeGtin = TradeGtin + newgting;
        Logger.log("Trade Gtin Generated is :" + TradeGtin);
        return TradeGtin;
    }

    public static String ConsumerBase_PLU() {
        String dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String date = dateFormat.substring(11, 19);
        date = date.replace(".", "");
        String consumerbasePLU = "" + date;
        Logger.log("Consumer Base PLU :-- " + consumerbasePLU);
        return consumerbasePLU;
    }

    /***********************************************************************************************
     * Function Description : This method used to get default selected value of a dropdown
     * author: Puneeta mishra, date: 05-April-2019
     * *********************************************************************************************/
    public static String getDefaultSelectedDropdownValue(String element) {
        Select select = new Select(DriverManager.getDriver().findElement(By.xpath(element)));
        return select.getFirstSelectedOption().getText();
    }

    /***********************************************************************************************
     * Function Description : This method used to connect to a mail box
     * author: Puneeta mishra, date: 05-April-2019
     * *********************************************************************************************/

    public static String connectToMailBox(String SubjectLine, String email_id, String password) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props, null);
        Store store = session.getStore();
        store.connect("imap.gmail.com", email_id, password);
//        store.connect("outlook.office365.com", "puneeta.mishra@nagarro.com", "P@ssw0rd2345");
        Folder selectedFolder = store.getFolder("Inbox");
        selectedFolder.open(Folder.READ_WRITE);
        Message[] messages = selectedFolder.getMessages();
        String result = "";

        for(int i = messages.length-1;i>=0;i++) {

//            System.out.println("Inbox box email count: " + messages.length);

            Message message = messages[i];

//            System.out.println(message.getSubject());

            if (message.getSubject().contains(SubjectLine)) {

                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();

                result = getTextFromMimeMultipart(mimeMultipart);

                System.out.println(result);

                break;
            }}
            return result;
       }

    /***********************************************************************************************
     * Function Description : Fetches text from the mail body
     * author: Puneeta mishra, date: 05-April-2019
     * *********************************************************************************************/
    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException
    {

        StringBuilder result = new StringBuilder();

        int count = mimeMultipart.getCount();

        for (int i = 0; i < count; i++) {

            BodyPart bodyPart = mimeMultipart.getBodyPart(i);

            if (bodyPart.isMimeType("text/plain")) {

                result.append("\n").append(bodyPart.getContent());

                break; // without break same text appears twice in my tests

            } else if (bodyPart.isMimeType("text/html")) {

                String html = (String) bodyPart.getContent();

                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());

            } else if (bodyPart.getContent() instanceof MimeMultipart){

                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));

            }

        }

        return result.toString();

    }

    /***********************************************************************************************
     * Function Description : This method deletes a mail
     * author: Puneeta mishra, date: 05-April-2019
     * *********************************************************************************************/

    public static void deleteMail(String SubjectLine, String email, String password) throws Exception
    {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props, null);
        Store store = session.getStore();
        store.connect("imap.gmail.com", email ,password);
//        store.connect("outlook.office365.com", "puneeta.mishra@nagarro.com", "P@ssw0rd2345");
        Folder selectedFolder = store.getFolder("Inbox");
        selectedFolder.open(Folder.READ_WRITE);
        Message[] messages = selectedFolder.getMessages();
        String result = "";

        for(int i = messages.length-1;i>=0;i++) {

            Message message = messages[i];

//            System.out.println(message.getSubject());

            if (message.getSubject().contains(SubjectLine)) {

                message.setFlag(Flags.Flag.DELETED,true);

                break;
            }}

    }

    /***********************************************************************************************
     * Function Description : This method compares dates
     * author: Puneeta mishra, date: 05-April-2019
     * *********************************************************************************************/
    public static String compareDates(String Date1, String Date2) throws Exception {

        SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = sdformat.parse(Date1);
        SimpleDateFormat sdformat1 = new SimpleDateFormat("dd/MM/yyyy");
        Date d2 = sdformat1.parse(Date2);
        String result = "";
        if (d1.compareTo(d2) > 0) {
            System.out.println("Date 1 occurs after Date 2");
            result = "Date 1 > Date 2";
        } else if (d1.compareTo(d2) < 0) {
            System.out.println("Date 1 occurs before Date 2");
            result = "Date 1 < Date 2";
        }

        return result;
    }

    /***********************************************************************************************
     * Function Description : It cleans the chrome driver exe
     * author: Puneeta mishra, date: 05-April-2019
     * *********************************************************************************************/
    public static void cleanup() throws Exception {
        Runtime.getRuntime().exec("taskkill /F /IM ChromeDriver.exe");
    }


    /**************************************************************
     * Function Description:To scroll the page Function parameters: String xpath
     * author: Puneeta Mishra, date: 01-May-2018
     ***************************************************************/
    public static void scroll_The_Page(String xpath) {
        Logger.log("Scroll the page");
        WebElement Element = findElement(By.xpath(xpath));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView();", Element);
    }

    /*****************************************************************************************************
     * Function Description : This Function is use to get and print test case execution status after end
     of each test case.
     * Author: Varun Vashishtha, date: 16-May-2018
     * *************************************************************************************************/

    public static void testCaseExecutionStatus(ITestResult result, String testName) {
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {
                Logger.log("");
                Logger.log("***********************" + testName + " has PASSED. " + "**************************");
            } else if (result.getStatus() == ITestResult.FAILURE) {
                Logger.log("");
                Logger.log("***********************" + testName + " has FAILED. " + "**************************");
            } else if (result.getStatus() == ITestResult.SKIP) {
                Logger.log("");
                Logger.log("***********************" + testName + " has SKIPPED. " + "**************************");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*****************************************************************************************************
     * Function Description : This Function verifies whether given element present in the list or not.
     * Author: Varun Vashishtha, date: 08-May-2018
     * *************************************************************************************************/
    public static Boolean checkElementPresentOrNotInList(String value, String locator) {
        List<WebElement> List = DriverManager.getDriver().findElements(By.xpath(locator));
        for (WebElement element : List) {
//            Logger.log(element.getText());
            if ((element.getText()).equals(value)) {
                Logger.log(element.getText() + "  Is present");
                ExtentLogger.logInfo(element.getText() + "  Is present");
                return true;
            }
        }
        return false;
    }

    /***********************************************************************************************
     * Function Description : Selects the provided value in a customized drop down
     *Author: Puneeta Mishra, Date: 09-May-2018
     ***********************************************************************************************/
    public static void select_DropDown(String element, String xpath, String selectText) {
        WebDriver driver = DriverManager.getDriver();
        driver.findElement(By.xpath(element)).click();
        if (findElementsSize(xpath)> 0) {
            List<WebElement> DropDownValueContainer;
            DropDownValueContainer = driver.findElements(By.xpath(xpath));
            for (WebElement option : DropDownValueContainer) {
                if (option.getText().equals(selectText)) {
                    Logger.log("element is present");
                    option.click();
                    break;
                }
            }
        }

    }

    /***********************************************************************************************
     * Function Description : This method is to move the focus to an element
     * author: Prashant Chaudhary, date: 05-July-2021
     * *********************************************************************************************/
    public static void moveToElement(String xpath){
        Actions action = new Actions(DriverManager.getDriver());
        WebElement element = DriverManager.getDriver().findElement(By.xpath(xpath));
        action.moveToElement(element).build().perform();
    }

    /***********************************************************************************************
     * Function Description : This method is to dismiss the alert
     * author: Prashant Chaudhary, date: 06-Jan-2022
     * *********************************************************************************************/
    public static void dismissAlert() {
        WebDriver driver = DriverManager.getDriver();
        driver.switchTo().alert().dismiss();
    }

    /***********************************************************************************************
     * Function Description : This method is to read drag and drop any file
     * author: Prashant Chaudhary, date: 06-Jan-2022
     * *********************************************************************************************/
    public static void dragAndDrop(String fromElementXpath, String toElementXpath){
        WebDriver driver = DriverManager.getDriver();
        Actions builder = new Actions(driver);
        Action dragAndDrop = builder.clickAndHold(findElement(fromElementXpath))
                .moveToElement(findElement(toElementXpath))
                .release(findElement(toElementXpath))
                .build();
        dragAndDrop.perform();
    }

    /***********************************************************************************************
     * Function Description : This method is to maximize the window
     * author: Prashant Chaudhary, date: 06-Jan-2022
     * *********************************************************************************************/
    public static void maximizeWindow(){
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize();
    }

    /***********************************************************************************************
     * Function Description : This method is to read the data from text file
     * author: Prashant Chaudhary, date: 06-Jan-2022
     * *********************************************************************************************/
    public static String readDataFromTextFile(String fileName) throws IOException {
        List<Character> temps = new ArrayList<>();
        File file1 = new File(fileName);
        FileReader fr = new FileReader(file1);
        long length = file1.length();
        for (long i = 0; i < length; i++) {
            temps.add((char) fr.read());
        }
        StringBuilder sb = new StringBuilder();
        for (Character ch: temps) {
            sb.append(ch);
        }
        String actualVal = sb.toString();
        String actualText = StringUtils.chomp(actualVal);
        fr.close();
        return actualText;
    }

    /***********************************************************************************************
     * Function Description : This method is to delete the downloaded file
     * author: Prashant Chaudhary, date: 06-Jan-2022
     * *********************************************************************************************/
    public static void deleteParticularFile(String filename) {
        Logger.log("Delete the files with name:-"+filename);
        File file = new File(filename);
        file.delete();
        Logger.log("Downloaded file Deleted:-" + filename);
    }

    /***********************************************************************************************
     * Function Description : This method is used to go to bottom of page
     * author: Prashant Chaudhary, date: 07-Jan-2022
     * *********************************************************************************************/
    public static void pageBottom() {
        JavascriptExecutor js = ((JavascriptExecutor) DriverManager.getDriver());
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        goToSleep(1000);
    }

    /***********************************************************************************************
     * Function Description : This method is to go to top of the page
     * author: Prashant Chaudhary, date: 07-Jan-2022
     * *********************************************************************************************/
    public static void pageTop() {
        JavascriptExecutor js = ((JavascriptExecutor) DriverManager.getDriver());
        js.executeScript("window.scrollTo(0, -document.body.scrollHeight)");
        goToSleep(1000);
    }

    public static void addWait(int milliSeconds){
        if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("Safari")) {
            Logger.log("Safari browser wait");
            ActionHelper.goToSleep(milliSeconds);
        }
    }

    /***********************************************************************************************
     * Function Description : This method is to switch to new latest window
     * author: Prashant Chaudhary, date: 25-Jan-2022
     * *********************************************************************************************/
    public static void switchToMainWindowFromAnotherTab(){
        Logger.log("Switch to main window when another browser instance is opened");
        ActionHelper.closeNewWindow();
        ActionHelper.goToSleep(1000);
        ArrayList<String> list = new ArrayList<>();
        WebDriver driver = DriverManager.getDriver();
        Set<String> windows = driver.getWindowHandles();
        int winSize = windows.size();
        Iterator<String> I1= windows.iterator();
        for (int i=0;i<winSize;i++){
            list.add(I1.next());
        }
        int listSize = list.size();
        ActionHelper.goToSleep(1000);
        driver.switchTo().window(list.get(listSize-1));
    }

    /***********************************************************************************************
     * Function Description : This method is to switch to previous opened window
     * author: Prashant Chaudhary, date: 25-Jan-2022
     * *********************************************************************************************/
    public static void switchToNewWindowFromMainTab(){
        Logger.log("Switch to new window from main window");
        ActionHelper.goToSleep(1000);
        ArrayList<String> list = new ArrayList<>();
        WebDriver driver = DriverManager.getDriver();
        Set<String> windows = driver.getWindowHandles();
        int winSize = windows.size();
        Iterator<String> I1= windows.iterator();
        for (int i=0;i<winSize;i++){
            list.add(I1.next());
        }
        int listSize = list.size();
        ActionHelper.goToSleep(1000);
        driver.switchTo().window(list.get(listSize-1));
    }
}

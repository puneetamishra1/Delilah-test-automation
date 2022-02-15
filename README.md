# ALM-Test-Automation
Selenium TestNG UI test automation suite for Metcash ALM

## How to get code from Git

Clone code using URL : https://github.com/ALM-Portal/ALM-Test-Automation.git

## Pre-requisites
Following are pre-requisites to run the automation scripts for ALM application on any environment

### Installation of Java

1. Download and install jdk 11.0.2
2. Copy the path of bin folder which is installed in JDK folder. And set _JAVA_HOME_ in user variable for windows Environment.
3. Also append the path till bin folder in PATH variable of Systems variables.
4. Go to command prompt and type “java -version” command and check if its installed.

 ![image](https://user-images.githubusercontent.com/94113274/150922323-d793ae05-068e-4314-9d9c-0160645ebe22.png)
 
### Maven Setup

1. Download Apache maven 3.8.3 (Binary zip archive) from “https://maven.apache.org/download.cgi”.
2. Unzip the file to a specific folder (recommended in C drive).
3. Copy the path of maven folder and set _MAVEN_HOME_ in user variables in the Windows environment.
4. Also append the path till bin folder in PATH variable of System variables.
5. To verify it, run “mvn –version” in the command prompt.

  ![image](https://user-images.githubusercontent.com/94113274/150922787-9d617435-df7b-4df1-abd5-78e716924d5d.png)

### How to execute scripts

1.  Install the latest version of chrome on the system.
2.  Navigate to the project folder having “src” folder in command prompt and type Maven command “mvn clean install”.
3.  HTML report and Excel report of execution will be available in “extentreport” (parallel to src folder) folder with snapshots attached for the failed cases.

>Note: We need to ensure that excel sheet is not open while execution of scripts.

### Maven Commands to execute scripts on different environment

`mvn clean test -Dthreads=1 -Denvironment="EnvName" -Dexecution_Type= "TAGNAME" -Ddriver_Type="BrowserName"`

Example :

    EnvName = dev/staging2/local 

    TAGNAME = Regression/Sanity

    BrowserName = Chrome/Safari
          
### How to execute Script for Safari Browser

1. Open Command Prompt 
2. Navigate to alm-test-automation folder
3. Set DRIVER_TYPE = Safari in _GlobalData.java_ in code

    `public static final String DRIVER_TYPE = System.getProperty("driver_Type", "Safari");`

4. Safari Automation setup : https://www.tutorialspoint.com/does-selenium-support-safari-browser 
5. Type the maven command as per the environment (mentioned above)
6. After full execution reports will be generated at the below given path _“alm-test-automation\extentreport”_

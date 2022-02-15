package com.metcash.delilah.testsetup;

import com.metcash.delilah.global.GlobalData;
import com.metcash.delilah.listener.AnnotationTransformer;
import org.testng.xml.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateTestNGXml {

    private static final String BASE_PACKAGE = "com.metcash.alm.testsuite";
    private static final String FILENAME_TESTNGXML = "testng.xml";

    public static void main(String[] args) throws Exception {
        createXml();
    }

    static void createXml()
            throws Exception {

        XmlSuite suite = new XmlSuite();
        suite.setName("IndieDirect");
        suite.addListener(AnnotationTransformer.class.getName());
        suite.setParallel(XmlSuite.ParallelMode.METHODS);
        suite.setThreadCount(GlobalData.NOOFTHREADS);
        suite.setPreserveOrder(true);

        // Add Test Suite
        XmlTest test = new XmlTest(suite);
        test.setName("Automation Suite");
        test.setThreadCount(GlobalData.NOOFTHREADS);

        List<XmlPackage> packages;
        List<XmlClass> classes;

        switch (GlobalData.EXECUTION_TYPE) {
            case "system":
                packages = new ArrayList<>();
                packages.add(new XmlPackage(".*"));
                test.setPackages(packages);
                break;

            case "package":
                packages = new ArrayList<>();
                if (GlobalData.GNAME == null) {
                    packages.add(new XmlPackage(".*"));
                } else {
                    String[] packageArr = GlobalData.GNAME.split(",");
                    for (String s : packageArr) {
                        packages.add(new XmlPackage(BASE_PACKAGE + "." + s));
                    }
                }
                test.setPackages(packages);
                break;

            case "group":
                packages = new ArrayList<>();
                packages.add(new XmlPackage(".*"));
                test.setPackages(packages);
                String[] groups = GlobalData.GNAME.split(",");
                test.setIncludedGroups(new ArrayList<>(Arrays.asList(groups)));
                break;

            case "class":
                classes = new ArrayList<>();
                String[] classArr = GlobalData.GNAME.split(",");
                for (String s : classArr) {
                    try {
                        XmlClass xmlClass = new XmlClass();
                        xmlClass.setName(BASE_PACKAGE + "." + s);
                        classes.add(xmlClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                test.setClasses(classes);
                break;

            case "method":
                classes = new ArrayList<>();
                String[] pair = GlobalData.GNAME.split(":");
                XmlClass testClass = new XmlClass();
                testClass.setName(BASE_PACKAGE + "." + pair[0]);
                List<XmlInclude> methods = new ArrayList<>();
                methods.add(new XmlInclude(pair[1]));
                testClass.setIncludedMethods(methods);
                classes.add(testClass);
                test.setClasses(classes);
                break;

            default:
                break;
        }

        FileWriter writer = new FileWriter(FILENAME_TESTNGXML);
        writer.write(suite.toXml());
        writer.flush();
        writer.close();

        System.out.println("--------- Created TestNG XML ----------");
    }
}

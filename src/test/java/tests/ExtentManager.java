package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class ExtentManager {

    private static ExtentReports extent;
    private static String reportPath;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            reportPath = "output/ExtentReport_" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("auraDOCV4 Automation Report");
            sparkReporter.config().setReportName("auraDOCS Login Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Project", "auraDOCV4");
            extent.setSystemInfo("Environment", "QA");
        }
        return extent;
    }

    public static String getReportPath() {
        return reportPath;
    }
}
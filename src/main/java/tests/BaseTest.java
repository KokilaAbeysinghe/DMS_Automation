package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import utilities.DriverManager;
import utilities.EmailUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import utilities.ExcelReader;

public class BaseTest {

    protected static WebDriver driver;
    protected static LoginPage loginPage;
    protected static ExtentReports extent = ExtentManager.getInstance();
    protected ExtentTest test;

    @BeforeClass
    public void setUpClass() {
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
    }

    @BeforeMethod
    public void setUp(java.lang.reflect.Method method) {
        try {
            loginPage.openLoginPage(getUrl());
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("[INFO] Login form not found — likely already authenticated. Continuing.");
        }
        test = extent.createTest(method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = takeScreenshot(result.getName());
            test.fail(result.getThrowable());
            if (screenshotPath != null) {
                try {
                    test.addScreenCaptureFromPath(screenshotPath);
                } catch (Exception e) {
                    System.err.println("Could not attach screenshot to report: " + e.getMessage());
                }
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test skipped");
        }

    }

    @AfterClass
    public void tearDownClass() {
        DriverManager.quitDriver();
    }

    @AfterSuite
    public void flushReport() {
        extent.flush();

        EmailUtil.sendEmail(
                "auraDOCS Login Test Results",
                "The automated login test suite has finished running. See attached report.",
                ExtentManager.getReportPath()
        );
    }
    private String takeScreenshot(String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Files.createDirectories(Paths.get("screenshots"));
            File dest = new File("screenshots/" + testName + "_" + timestamp + ".png");
            Files.copy(src.toPath(), dest.toPath());
            return dest.getPath();
        } catch (IOException e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
            return null;
        }
    }

    protected String getUrl() {
        return ExcelReader.get("loginUrl");
    }
}
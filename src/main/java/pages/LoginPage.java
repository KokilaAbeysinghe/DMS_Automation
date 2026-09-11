package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;


public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }



    @FindBy(css = "input[type='email'], input[name='username'], input[name='email']")
    private WebElement usernameField;

    @FindBy(css = "input[type='password'], input[name='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//button[normalize-space()='Submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[normalize-space()='Reset'] | //*[normalize-space()='Reset']")
    private WebElement resetButton;

    @FindBy(linkText = "Forgot password?")
    private WebElement forgotPasswordLink;

    @FindBy(xpath = "//*[contains(normalize-space(),'Sign in with microsoft')]")
    private WebElement signInWithMicrosoftButton;

    @FindBy(css = "[data-test='dashboard'], .dashboard, #dashboard")
    private WebElement dashboardIndicator;

    @FindBy(xpath = "//span[contains(text(),'Invalid username or password')]")
    private WebElement errorMessage;


    public void open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        pause();
    }

    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
        pause();
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordField));
        passwordField.clear();
        passwordField.sendKeys(password);
        pause();
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        pause();
    }

    public void clickReset() {
        wait.until(ExpectedConditions.elementToBeClickable(resetButton)).click();
        pause();
    }

    public void clickForgotPassword() {
        wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
        pause();
    }

   public void clickSignInWithMicrosoft() {
        wait.until(ExpectedConditions.elementToBeClickable(signInWithMicrosoftButton)).click();
        wait.until(ExpectedConditions.urlContains("microsoftonline.com"));
       pause();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSubmit();
        pause();
    }

    public void openLoginPage(String url) {
        open(url);
    }

    public void enterEmail(String email) {
        enterUsername(email);
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmailFieldEmpty() {
        return usernameField.getAttribute("value").isEmpty();
    }

    public boolean isPasswordFieldEmpty() {
        return passwordField.getAttribute("value").isEmpty();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("login")));
            return !driver.getCurrentUrl().toLowerCase().contains("login");
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
    }

    public boolean isUsernameFieldInvalid() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Object isValid = js.executeScript("return arguments[0].validity.valid;", usernameField);
        return !((Boolean) isValid);
    }

    public String getUsernameValidationMessage() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return arguments[0].validationMessage;", usernameField);
    }
    private static final long PAUSE_MILLIS = 2000;
    private void pause() {
        try {
            Thread.sleep(PAUSE_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class UploadPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public UploadPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[contains(normalize-space(),'Document Capture')]")
    private WebElement documentCaptureBanner;

    @FindBy(id = "documents")
    private WebElement fileInput;

    @FindBy(xpath = "//button[contains(@class,'start')]")
    private WebElement uploadButton;

    @FindBy(xpath = "//span[normalize-space()='Clear']")
    private WebElement clearButton;

    public boolean isOnDocumentCapturePage() {
        return wait.until(ExpectedConditions.visibilityOf(documentCaptureBanner)).isDisplayed();
    }

    public boolean isUploadButtonDisabled() {
        return "true".equals(fileInput.getAttribute("disabled"))
                || uploadButton.getAttribute("disabled") != null;

    }

    public void selectFile(String absoluteFilePath) {

        List<WebElement> matches = driver.findElements(By.id("documents"));
        System.out.println("[DIAGNOSTIC] Elements matching id='documents': " + matches.size());

        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("documents")));

        System.out.println("[DIAGNOSTIC] Input displayed? " + input.isDisplayed());
        System.out.println("[DIAGNOSTIC] Input enabled? " + input.isEnabled());

        input.sendKeys(absoluteFilePath);

        System.out.println("[DIAGNOSTIC] Input value after sendKeys: " + input.getAttribute("value"));
        pause();
    }

    public void clickUpload() {

        List<WebElement> startMatches = driver.findElements(By.xpath("//button[contains(@class,'start')]"));
        System.out.println("[DIAGNOSTIC] Elements matching class*='start': " + startMatches.size());
        for (int i = 0; i < startMatches.size(); i++) {
            System.out.println("[DIAGNOSTIC]   [" + i + "] class=\"" + startMatches.get(i).getAttribute("class")
                    + "\" text=\"" + startMatches.get(i).getText() + "\"");
        }

        System.out.println("[DIAGNOSTIC] uploadButton disabled attr: " + uploadButton.getAttribute("disabled"));
        System.out.println("[DIAGNOSTIC] uploadButton displayed? " + uploadButton.isDisplayed());

        wait.until(ExpectedConditions.elementToBeClickable(uploadButton)).click();
        pause();
    }

    public void clickClear() {
        wait.until(ExpectedConditions.elementToBeClickable(clearButton)).click();
        pause();
    }

    public void uploadFile(String absoluteFilePath) {
        selectFile(absoluteFilePath);
        clickUpload();
        pause();
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
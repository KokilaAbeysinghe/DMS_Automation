package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TopBarPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public TopBarPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "searchButton")
    private WebElement searchButton;

    @FindBy(css = ".fa-caret-down")
    private WebElement searchDropdownCaret;

    @FindBy(xpath = "//button[contains(normalize-space(),'Upload')]")
    private WebElement uploadButton;

    @FindBy(css = "button[title='File Explorer']")
    private WebElement fileExplorerButton;

    // ---- Search ----
    public boolean isSearchButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOf(searchButton)).isDisplayed();
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public void clickSearchDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(searchDropdownCaret)).click();
    }

    // ---- Upload ----
    public boolean isUploadButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOf(uploadButton)).isDisplayed();
    }

    public void clickUpload() {
        wait.until(ExpectedConditions.elementToBeClickable(uploadButton)).click();
    }

    // ---- File Explorer ----
    public boolean isFileExplorerButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOf(fileExplorerButton)).isDisplayed();
    }

    public void clickFileExplorer() {
        wait.until(ExpectedConditions.elementToBeClickable(fileExplorerButton)).click();
    }
}
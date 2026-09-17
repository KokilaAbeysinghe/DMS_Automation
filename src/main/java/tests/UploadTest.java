package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.TopBarPage;
import pages.UploadPage;
import utilities.ExcelReader;

public class UploadTest extends BaseTest {

    private TopBarPage topBarPage;
    private UploadPage uploadPage;

    private void goToUploadPage() {
        if (loginPage.isLoginPageDisplayed()) {
            loginPage.login(ExcelReader.get("validEmail"), ExcelReader.get("validPassword"));
        }
        topBarPage = new TopBarPage(driver);
        topBarPage.clickUpload();
        uploadPage = new UploadPage(driver);
    }

    @Test(priority = 1)
    public void testNavigateToUploadPage() {
        goToUploadPage();
        Assert.assertTrue(uploadPage.isOnDocumentCapturePage(), "Did not land on Document Capture page");
    }

    @Test(priority = 2)
    public void testUploadButtonDisabledBeforeFileSelected() {
        goToUploadPage();
        Assert.assertTrue(uploadPage.isUploadButtonDisabled(), "Upload button should be disabled with no file selected");
    }

    @Test(priority = 3)
    public void testValidFileUploadSucceeds() {
        goToUploadPage();
        uploadPage.uploadFile("C:\\Users\\KokilaAbeysinghe\\Downloads\\Untitled.txt");
    }


}

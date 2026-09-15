package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.ExcelReader;


public class LoginTest extends BaseTest {

    // 1. Open login page
    @Test(priority = 1)
    public void openLoginPage() {
        String currentUrl = BaseTest.loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("auradocs.com"),
                "Login page did not load. Current URL: " + currentUrl);
    }

    // 2. Enter invalid Email and valid Password, click Submit
    @Test(priority = 2)
    public void invalidEmailValidPassword() {
        BaseTest.loginPage.login(ExcelReader.get("invalidEmail"), ExcelReader.get("validPassword"));
        Assert.assertTrue(BaseTest.loginPage.isErrorMessageDisplayed(),
                "Expected an error message for invalid email, but none was shown.");
    }

    // 3. Enter valid Email and invalid Password, click Submit
    @Test(priority = 3)
    public void validEmailInvalidPassword() {
        BaseTest.loginPage.login(ExcelReader.get("validEmail"), ExcelReader.get("wrongPassword"));
        Assert.assertTrue(BaseTest.loginPage.isErrorMessageDisplayed(),
                "Expected an error message for wrong password, but none was shown.");
    }

    // 4. Click Submit without entering Email and Password
    @Test(priority = 4)
    public void emptyEmailAndPassword() {
        BaseTest.loginPage.clickSubmit();
        Assert.assertTrue(BaseTest.loginPage.isErrorMessageDisplayed(),
                "Expected a validation message when both fields are empty.");
    }

    // 5. Leave Email empty, enter Password, click Submit
    @Test(priority = 5)
    public void emptyEmailOnly() {
        BaseTest.loginPage.enterPassword(ExcelReader.get("validPassword"));
        BaseTest.loginPage.clickSubmit();
        Assert.assertTrue(BaseTest.loginPage.isErrorMessageDisplayed(),
                "Expected a validation message when email is empty.");
    }

    // 6. Enter Email, leave Password empty, click Submit
    @Test(priority = 6)
    public void emptyPasswordOnly() {
        BaseTest.loginPage.enterEmail(ExcelReader.get("validEmail"));
        BaseTest.loginPage.clickSubmit();
        Assert.assertTrue(BaseTest.loginPage.isErrorMessageDisplayed(),
                "Expected a validation message when password is empty.");
    }

    // 7. Enter invalid Email format (e.g., abc@), click Submit
    @Test(priority = 7)
    public void malformedEmailFormat() {
        BaseTest.loginPage.enterEmail(ExcelReader.get("malformedEmail"));
        BaseTest.loginPage.enterPassword(ExcelReader.get("validPassword"));
        BaseTest.loginPage.clickSubmit();
        Assert.assertTrue(BaseTest.loginPage.isErrorMessageDisplayed(),
                "Expected a format-validation message for a malformed email.");
    }

    // 8. Enter data in fields and click Reset
    @Test(priority = 8)
    public void resetClearsFields() {
        BaseTest.loginPage.enterEmail(ExcelReader.get("validEmail"));
        BaseTest.loginPage.enterPassword(ExcelReader.get("validPassword"));
        BaseTest.loginPage.clickReset();
        Assert.assertTrue(BaseTest.loginPage.isEmailFieldEmpty(), "Email field was not cleared by Reset.");
        Assert.assertTrue(BaseTest.loginPage.isPasswordFieldEmpty(), "Password field was not cleared by Reset.");
    }

    // 9. Click "Forgot password?" link
    @Test(priority = 9)
    public void forgotPasswordLinkNavigates() {
        String urlBeforeClick = BaseTest.loginPage.getCurrentUrl();
        BaseTest.loginPage.clickForgotPassword();
        String urlAfterClick = BaseTest.loginPage.getCurrentUrl();
        Assert.assertNotEquals(urlAfterClick, urlBeforeClick,
                "URL did not change after clicking 'Forgot password?'.");
    }

    // 10. Attempt multiple failed logins
    @Test(priority = 10)
    public void multipleFailedLoginAttempts() {
        String[] wrongPasswords = { ExcelReader.get("wrongPassword1"), ExcelReader.get("wrongPassword2"), ExcelReader.get("wrongPassword3") };

        for (String wrongPassword : wrongPasswords) {
            BaseTest.loginPage.openLoginPage(ExcelReader.get("loginUrl")); // refresh page between attempts
            BaseTest.loginPage.login(ExcelReader.get("validEmail"), wrongPassword);
            Assert.assertTrue(BaseTest.loginPage.isErrorMessageDisplayed(),
                    "Expected an error message for failed attempt with password: " + wrongPassword);
        }

    }

    // 11. Click "Sign in with Microsoft" button and verify Microsoft login page loads
    @Test(priority = 11)
    public void signInWithMicrosoftButton() {
        BaseTest.loginPage.clickSignInWithMicrosoft();
        String currentUrl = BaseTest.loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login.microsoftonline.com"),
                "Microsoft login page did not load. Current URL: " + currentUrl);
    }

    // 12. Try accessing system without login
    @Test(priority = 12)
    public void accessSystemWithoutLogin() {

        String protectedUrl = ExcelReader.get("loginUrl") + "dashboard";
        BaseTest.driver.get(protectedUrl);

        String currentUrl = BaseTest.driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("openid-connect/auth") || currentUrl.equals(ExcelReader.get("loginUrl")),
                "App allowed access to a protected page without logging in. Landed on: " + currentUrl);
    }

    // 13. Enter valid Email and valid Password, click Submit (MUST run last)
    @Test(priority = 13)
    public void validEmailValidPassword() {
        BaseTest.loginPage.login(ExcelReader.get("validEmail"), ExcelReader.get("validPassword"));
        Assert.assertTrue(BaseTest.loginPage.isLoginSuccessful(),
                "Login with valid credentials failed.");
    }
}
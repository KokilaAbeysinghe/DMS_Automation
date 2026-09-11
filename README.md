# auraDOCS V4 - Login Page Automation (Selenium + TestNG + Java)

This is a simple, beginner-friendly Selenium project that automates 12 login
page scenarios for https://v4.auradocs.com/

## Folder structure

```
auraDOCS-Login-Automation/
├── pom.xml                          <- Maven dependencies (Selenium, TestNG, WebDriverManager)
├── testng.xml                       <- Defines which test class/suite to run
├── test_data/
│   └── config.properties            <- URL + test credentials (EDIT THIS FIRST)
├── src/
│   ├── main/java/
│   │   ├── pages/
│   │   │   └── LoginPage.java       <- Page Object: all locators + actions for the login page
│   │   └── utilities/
│   │       ├── DriverFactory.java   <- Starts/stops the Chrome browser
│   │       └── ConfigReader.java    <- Reads values from config.properties
│   └── test/java/
│       └── tests/
│           ├── BaseTest.java        <- Opens browser before each test, closes it after
│           └── LoginTest.java       <- All 12 test scenarios (the file you'll look at most)
```

This matches the structure you already had (`pages`, `utilities`, `test`, `test_data`).

## What you need to do before running (IMPORTANT)

This project can't guess your app's actual HTML, so two things are placeholders:

### 1. Fill in real credentials
Open `test_data/config.properties` and replace the placeholder values with a
real account that exists in auraDOCS (email + password that actually logs in).

### 2. Replace the placeholder locators in `LoginPage.java`
Every locator (`By.id("email")`, etc.) is a guess. You must inspect the real
page and swap in the real ones. Here's exactly how, since this is your first project:

1. Open https://v4.auradocs.com/ in Chrome.
2. Right-click the **Email** field → **Inspect**.
3. In the DevTools panel that opens, look at the highlighted HTML, e.g.:
   ```html
   <input type="email" id="username" name="loginEmail" class="form-input">
   ```
4. Pick the most reliable attribute (id > name > a stable class) and update
   the matching line in `LoginPage.java`:
   ```java
   private By emailField = By.id("username"); // <- update to match what you saw
   ```
5. Repeat this for: password field, Submit button, Reset button,
   "Forgot password?" link, "Sign in with Microsoft" button, and the
   error message element that appears after a failed login.
6. For `dashboardHeader` in `LoginPage.java` — log in manually once, inspect
   any element that ONLY appears after a successful login (like a welcome
   header or menu), and use that locator. This is how the test confirms
   login actually worked.

Every placeholder in the code has a `// TODO` comment next to it so they're
easy to find.

## How to run this project in IntelliJ

1. Unzip the folder and open it in IntelliJ: **File > Open** → select the
   `auraDOCS-Login-Automation` folder.
2. IntelliJ will detect it's a Maven project and download the dependencies
   automatically (watch the progress bar at the bottom). If it doesn't,
   right-click `pom.xml` → **Maven > Reload Project**.
3. Do the two setup steps above (credentials + locators).
4. Right-click `testng.xml` → **Run 'testng.xml'**.
   - This runs all 12 tests in order, in a real Chrome window.
   - Alternatively, right-click `LoginTest.java` → **Run 'LoginTest'** to run
     just that class, or click the green arrow next to any single `@Test`
     method to run just that one scenario.

You do **not** need to manually download ChromeDriver — `WebDriverManager`
(already included) detects your Chrome version and downloads the matching
driver automatically the first time you run a test.

## The 12 scenarios (in `LoginTest.java`)

Each scenario is its own `@Test` method, and `priority` numbers control the
order they run in. Valid login is `priority = 13`, so it always runs **last**,
after every negative/edge case has been tried:

| # | Method name | Scenario |
|---|---|---|
| 1 | `openLoginPage` | Open login page |
| 2 | `invalidEmailValidPassword` | Invalid Email + valid Password |
| 3 | `validEmailInvalidPassword` | Valid Email + invalid Password |
| 4 | `emptyEmailAndPassword` | Submit with both fields empty |
| 5 | `emptyEmailOnly` | Email empty, Password filled |
| 6 | `emptyPasswordOnly` | Email filled, Password empty |
| 7 | `malformedEmailFormat` | Invalid email format (e.g. `abc@`) |
| 8 | `resetClearsFields` | Fill fields, click Reset |
| 9 | `forgotPasswordLinkNavigates` | Click "Forgot password?" |
| 10 | `multipleFailedLoginAttempts` | 3 failed logins in a row |
| 11 | `signInWithMicrosoftButton` | Click "Sign in with Microsoft" |
| 12 | `accessSystemWithoutLogin` | Try opening a protected page directly |
| 13 | `validEmailValidPassword` | Valid Email + valid Password (last) |

## Notes for a first project

- Every test opens a **fresh browser** (see `BaseTest.java`) so one test's
  leftover state can never accidentally affect another test.
- All test data (URL, emails, passwords) lives in `config.properties`, not
  hard-coded in the test file — so when a password changes, you edit one file.
- `LoginPage.java` follows the **Page Object Model**: locators and actions for
  a page live together in one class, and the test file just calls simple
  methods like `loginPage.login(email, password)`. This is the standard
  pattern used in real Selenium projects, so it's worth getting comfortable
  with it now.
- If a test fails after you've filled in real locators, right-click the test
  → **Run**, and read the assertion message — it tells you exactly what was
  expected vs what happened.
- `accessSystemWithoutLogin` (#12) assumes there's a "dashboard" style page
  you can guess the URL of. If auraDOCS doesn't expose a guessable protected
  URL, you may need to adjust that one test based on how the app actually
  behaves.

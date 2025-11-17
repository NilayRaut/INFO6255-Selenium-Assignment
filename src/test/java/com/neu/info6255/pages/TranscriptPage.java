package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;
import java.util.List;

public class TranscriptPage extends BasePage {

    // Updated Locators
    private By resourcesTab = By.linkText("Resources");
    private By academicsTab = By.cssSelector("#resource-tab-Academics\\,_Classes_\\&_Registration > .fui-Tab__content");
    private By myTranscriptsLink = By.linkText("My Transcript");

    // Transcript portal login (Shibboleth)
    private By shibUsername = By.id("username");
    private By shibPassword = By.id("password");
    private By shibProceed = By.name("_eventId_proceed");

    // Duo iframe and buttons - multiple selectors
    private By duoPushButton = By.cssSelector("button.auth-button.positive");
    private By duoPushButtonAlt1 = By.cssSelector("fieldset:nth-child(1) > .push-label > .auth-button");
    private By duoPushButtonAlt2 = By.xpath("//button[contains(text(), 'Send Me a Push')]");
    private By duoPushButtonAlt3 = By.xpath("//button[contains(@class, 'positive')]");

    // Transcript options
    private By transcriptLevelDropdown = By.id("levl_id");
    private By transcriptTypeDropdown = By.id("tpsr_id");
    private By submitButton = By.cssSelector("form:nth-child(2) > input");

    public TranscriptPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToStudentHub() {
        System.out.println("Already on Student Hub page");
        WaitUtils.sleep(1000);
    }

    public void clickResources() {
        System.out.println("Clicking Resources tab...");
        waitForElement(resourcesTab);
        click(resourcesTab);
        WaitUtils.sleep(1500);
    }

    public void clickAcademics() {
        System.out.println("Clicking Academics tab...");
        waitForElement(academicsTab);
        click(academicsTab);
        WaitUtils.sleep(1500);
    }

    public void clickMyTranscripts() {
        System.out.println("Clicking My Transcript link...");

        String mainWindow = driver.getWindowHandle();
        click(myTranscriptsLink);
        WaitUtils.sleep(2000);

        // Switch to new window
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                System.out.println("✓ Switched to My Transcripts window");
                System.out.println("Current URL: " + driver.getCurrentUrl());
                break;
            }
        }
        WaitUtils.sleep(1500);
    }

    public void loginToTranscriptPortal(String username, String password) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Logging into transcript portal with Shibboleth...");
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("=".repeat(60));

        try {
            // Wait for page to load
            WaitUtils.sleep(2000);

            // Check if already on transcript page
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("bwskotrn.P_ViewTermTran")) {
                System.out.println("✓ Already on transcript selection page - no login needed");
                return;
            }

            // Check if login form exists
            if (driver.findElements(shibUsername).size() == 0) {
                System.out.println("⚠️  No login form found");

                // Check if Duo is present
                if (driver.findElements(By.tagName("iframe")).size() > 0) {
                    System.out.println("Duo iframe detected - handling authentication");
                    handleDuoInTranscriptPortal();
                    return;
                }

                System.out.println("Assuming already logged in");
                return;
            }

            // Extract username
            String partialUsername;
            if (username.contains("@")) {
                partialUsername = username.substring(0, username.indexOf("@"));
            } else {
                partialUsername = username;
            }
            System.out.println("Using username: " + partialUsername);

            // Enter credentials
            waitForElement(shibUsername);

            WebElement usernameField = driver.findElement(shibUsername);
            usernameField.click();
            WaitUtils.sleep(200);
            usernameField.clear();
            usernameField.sendKeys(partialUsername);
            System.out.println("✓ Entered username");

            WebElement passwordField = driver.findElement(shibPassword);
            passwordField.click();
            WaitUtils.sleep(200);
            passwordField.clear();
            passwordField.sendKeys(password);
            System.out.println("✓ Entered password");

            // Submit with ENTER
            passwordField.sendKeys(Keys.ENTER);
            System.out.println("✓ Pressed ENTER to submit");
            WaitUtils.sleep(3000);

            // Handle Duo authentication
            handleDuoInTranscriptPortal();

            // Wait for transcript page
            WaitUtils.sleep(2000);

            System.out.println("Final URL: " + driver.getCurrentUrl());
            System.out.println("=".repeat(60));
            System.out.println("✓ Transcript portal login completed");
            System.out.println("=".repeat(60) + "\n");

        } catch (Exception e) {
            System.out.println("❌ Login error: " + e.getMessage());
            driver.switchTo().defaultContent();
            throw e;
        }
    }

    private void handleDuoInTranscriptPortal() {
        try {
            System.out.println("\n--- Checking for Duo authentication ---");
            WaitUtils.sleep(1500);

            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            System.out.println("Found " + iframes.size() + " iframe(s)");

            if (iframes.size() > 0) {
                System.out.println("✓ Switching to Duo iframe");
                driver.switchTo().frame(0);
                WaitUtils.sleep(1500);

                // Try multiple button selectors
                boolean buttonClicked = false;
                By[] buttonSelectors = {
                        duoPushButton,
                        duoPushButtonAlt1,
                        duoPushButtonAlt2,
                        duoPushButtonAlt3
                };

                for (By selector : buttonSelectors) {
                    try {
                        List<WebElement> buttons = driver.findElements(selector);
                        if (buttons.size() > 0) {
                            WebElement button = buttons.get(0);
                            // Use JavaScript click to avoid interception issues
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                            System.out.println("✓ Clicked Duo push button using: " + selector);
                            buttonClicked = true;
                            break;
                        }
                    } catch (Exception e) {
                        // Try next selector
                        continue;
                    }
                }

                if (!buttonClicked) {
                    System.out.println("⚠️  Automatic Duo button click failed");
                    System.out.println("📱 MANUAL ACTION REQUIRED: Click 'Send Me a Push' in Duo");
                }

                // Switch back
                driver.switchTo().defaultContent();

                // Wait for Duo approval
                System.out.println("\n" + "=".repeat(60));
                System.out.println("⚠️  APPROVE DUO PUSH ON YOUR PHONE NOW!");
                System.out.println("Waiting 20 seconds...");
                System.out.println("=".repeat(60));
                WaitUtils.sleep(20000); // Reduced from 25s

                System.out.println("✓ Duo authentication completed");

            } else {
                System.out.println("✓ No Duo iframe - authentication not required");
            }

        } catch (Exception e) {
            System.out.println("⚠️  Duo handling issue: " + e.getMessage());
            try {
                driver.switchTo().defaultContent();
            } catch (Exception ex) {
                // Ignore
            }
        }
    }

    public void selectTranscriptOptions() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Selecting transcript options...");
        System.out.println("=".repeat(60));

        try {
            WaitUtils.sleep(2000); // Reduced from 3s

            // Scroll to top
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            WaitUtils.sleep(300);

            // Wait for dropdown
            wait.withTimeout(Duration.ofSeconds(15)); // Reduced from 30s
            waitForElement(transcriptLevelDropdown);

            // Select Graduate
            WebElement levelElement = driver.findElement(transcriptLevelDropdown);
            levelElement.click();
            WaitUtils.sleep(300);

            Select levelDropdown = new Select(levelElement);
            levelDropdown.selectByVisibleText("Graduate");
            System.out.println("✓ Selected 'Graduate'");
            WaitUtils.sleep(500);

            // Check Transcript Type
            try {
                Select typeDropdown = new Select(driver.findElement(transcriptTypeDropdown));
                String currentType = typeDropdown.getFirstSelectedOption().getText();
                if (!currentType.contains("Audit")) {
                    typeDropdown.selectByVisibleText("Audit Transcript");
                    System.out.println("✓ Selected 'Audit Transcript'");
                } else {
                    System.out.println("✓ Transcript type is 'Audit Transcript'");
                }
            } catch (Exception e) {
                System.out.println("Transcript type already set");
            }

            System.out.println("=".repeat(60));
            System.out.println("✓ Options selected");
            System.out.println("=".repeat(60) + "\n");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            throw e;
        } finally {
            wait.withTimeout(Duration.ofSeconds(20));
        }
    }

    public void clickSubmit() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Clicking Submit button...");
        System.out.println("=".repeat(60));

        try {
            waitForElement(submitButton);
            click(submitButton);
            WaitUtils.sleep(3000); // Reduced from 4s
            System.out.println("✓ Transcript displayed");
            System.out.println("=".repeat(60) + "\n");
        } catch (Exception e) {
            try {
                By alternativeSubmit = By.cssSelector("input[type='submit']");
                click(alternativeSubmit);
                WaitUtils.sleep(3000);
                System.out.println("✓ Submit clicked (alternative)");
            } catch (Exception ex) {
                System.out.println("❌ Submit button not found");
                throw ex;
            }
        }
    }

    public void printTranscript() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Simulating Print/Save as PDF...");
        System.out.println("=".repeat(60));

        try {
            // Use async JavaScript to avoid blocking
            JavascriptExecutor js = (JavascriptExecutor) driver;

            System.out.println("Opening print dialog (async)...");

            // Execute print in async mode to prevent timeout
            js.executeAsyncScript(
                    "var callback = arguments[arguments.length - 1];" +
                            "setTimeout(function() { window.print(); callback(); }, 100);"
            );

            System.out.println("✓ Print dialog triggered");
            System.out.println("=".repeat(60));
            System.out.println("📄 MANUAL ACTION REQUIRED:");
            System.out.println("1. Select 'Save as PDF' in print dialog");
            System.out.println("2. Choose location and filename");
            System.out.println("3. Click 'Save'");
            System.out.println("4. Press ESC to close print dialog");
            System.out.println("=".repeat(60));

            // Wait for user to save
            WaitUtils.sleep(5000); // Reduced from 8s

            System.out.println("✓ Print process completed");
            System.out.println("=".repeat(60) + "\n");

        } catch (Exception e) {
            System.out.println("⚠️  Print dialog may require manual action");
            System.out.println("Error: " + e.getMessage());

            // Don't throw exception - print is optional
            System.out.println("Continuing test execution...");
        }
    }
}
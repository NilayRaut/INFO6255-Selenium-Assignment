package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class TranscriptPage extends BasePage {

    // Updated Locators from IDE recording
    private By resourcesTab = By.linkText("Resources");
    private By academicsTab = By.cssSelector("#resource-tab-Academics\\,_Classes_\\&_Registration > .fui-Tab__content");
    private By myTranscriptsLink = By.linkText("My Transcript");

    // Transcript portal login (Shibboleth)
    private By shibUsername = By.id("username");
    private By shibPassword = By.id("password");
    private By shibProceed = By.name("_eventId_proceed");

    // Duo iframe
    private By duoPushButton = By.cssSelector("fieldset:nth-child(1) > .push-label > .positive");

    // Transcript options
    private By transcriptLevelDropdown = By.id("levl_id");
    private By transcriptTypeDropdown = By.id("tpsr_id");
    private By submitButton = By.cssSelector("form:nth-child(2) > input");

    public TranscriptPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToStudentHub() {
        System.out.println("Already on Student Hub page");
        WaitUtils.sleep(2000);
    }

    public void clickResources() {
        System.out.println("Clicking Resources tab...");
        waitForElement(resourcesTab);
        click(resourcesTab);
        WaitUtils.sleep(2000);
    }

    public void clickAcademics() {
        System.out.println("Clicking Academics tab...");
        waitForElement(academicsTab);
        click(academicsTab);
        WaitUtils.sleep(2000);
    }

    public void clickMyTranscripts() {
        System.out.println("Clicking My Transcript link...");

        String mainWindow = driver.getWindowHandle();
        click(myTranscriptsLink);
        WaitUtils.sleep(3000);

        // Switch to new window
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                System.out.println("Switched to My Transcripts window");
                break;
            }
        }
        WaitUtils.sleep(2000);
    }

    public void loginToTranscriptPortal(String username, String password) {
        System.out.println("Logging into transcript portal with Shibboleth...");

        try {
            // Check if login is needed
            if (driver.findElements(shibUsername).size() == 0) {
                System.out.println("Already logged in to transcript portal");
                return;
            }

            // Enter credentials - use partial username
            String partialUsername = username.substring(0, username.indexOf("@"));

            waitForElement(shibUsername);
            driver.findElement(shibUsername).click();
            driver.findElement(shibUsername).sendKeys(partialUsername);
            System.out.println("Entered username: " + partialUsername);

            driver.findElement(shibPassword).sendKeys(password);
            System.out.println("Entered password");

            driver.findElement(shibPassword).submit(); // Use submit instead of click
            WaitUtils.sleep(5000);

            // Handle Duo in iframe
            try {
                System.out.println("Looking for Duo iframe...");
                driver.switchTo().frame(0);

                waitForElement(duoPushButton);
                click(duoPushButton);
                System.out.println("Clicked Duo 'Send Me a Push' button");

                driver.switchTo().defaultContent();

                System.out.println("\n⚠️  APPROVE DUO PUSH ON YOUR PHONE NOW!");
                WaitUtils.sleep(30000);

            } catch (Exception e) {
                System.out.println("Duo may not be required or already approved");
                driver.switchTo().defaultContent();
            }

        } catch (Exception e) {
            System.out.println("Transcript login error: " + e.getMessage());
        }
    }

    public void selectTranscriptOptions() {
        System.out.println("Selecting transcript options...");

        try {
            WaitUtils.sleep(3000); // Wait for page load

            // Select Graduate
            waitForElement(transcriptLevelDropdown);
            Select levelDropdown = new Select(driver.findElement(transcriptLevelDropdown));
            levelDropdown.selectByVisibleText("Graduate");
            System.out.println("✓ Selected 'Graduate'");
            WaitUtils.sleep(1000);

            // Transcript Type should already be "Audit Transcript" by default
            // But let's verify and select if needed
            try {
                Select typeDropdown = new Select(driver.findElement(transcriptTypeDropdown));
                if (!typeDropdown.getFirstSelectedOption().getText().contains("Audit")) {
                    typeDropdown.selectByVisibleText("Audit Transcript");
                }
                System.out.println("✓ Transcript type is 'Audit Transcript'");
            } catch (Exception e) {
                System.out.println("Transcript type already set or not found");
            }

        } catch (Exception e) {
            System.out.println("Error selecting options: " + e.getMessage());
        }
    }

    public void clickSubmit() {
        System.out.println("Clicking Submit button...");

        try {
            waitForElement(submitButton);
            click(submitButton);
            WaitUtils.sleep(5000);
            System.out.println("✓ Transcript displayed");
        } catch (Exception e) {
            System.out.println("Submit button not found, trying alternative...");
            // Try finding any submit button
            try {
                driver.findElement(By.cssSelector("input[type='submit']")).click();
                WaitUtils.sleep(5000);
            } catch (Exception ex) {
                System.out.println("Could not find submit button");
            }
        }
    }

    public void printTranscript() {
        System.out.println("✓ Transcript page displayed - Print simulated");
    }
}
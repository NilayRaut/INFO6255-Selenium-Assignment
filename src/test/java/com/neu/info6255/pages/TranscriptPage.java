package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class TranscriptPage extends BasePage {

    // Updated Locators from Selenium IDE recording
    private By resourcesTab = By.linkText("Resources");
    private By academicsTab = By.cssSelector("#resource-tab-Academics\\,_Classes_\\&_Registration > .fui-Tab__content");
    private By myTranscriptsLink = By.linkText("My Transcript");

    // Transcript page locators (after new window opens)
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By proceedButton = By.name("_eventId_proceed");
    private By duoIframe = By.cssSelector("iframe");
    private By duoSendPushButton = By.cssSelector("fieldset:nth-child(1) > .push-label > .positive");
    private By transcriptLevelDropdown = By.id("levl_id");
    private By submitButton = By.cssSelector("form:nth-child(2) > input");

    public TranscriptPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToStudentHub() {
        // Already on Student Hub after login
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

        // Store main window handle
        String mainWindow = driver.getWindowHandle();

        click(myTranscriptsLink);
        WaitUtils.sleep(3000);

        // Switch to new window
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindow)) {
                driver.switchTo().window(windowHandle);
                System.out.println("Switched to My Transcripts window");
                break;
            }
        }

        WaitUtils.sleep(2000);
    }

    public void loginToTranscriptPortal(String username, String password) {
        System.out.println("Logging into transcript portal...");

        try {
            // Enter username
            waitForElement(usernameField);
            type(usernameField, username);
            System.out.println("Entered username");

            // Enter password
            type(passwordField, password);
            System.out.println("Entered password");

            // Click proceed
            click(proceedButton);
            WaitUtils.sleep(5000);

            // Handle Duo in iframe
            System.out.println("Handling Duo authentication in iframe...");
            driver.switchTo().frame(0);

            try {
                waitForElement(duoSendPushButton);
                click(duoSendPushButton);
                System.out.println("Clicked 'Send Me a Push' button");
            } catch (Exception e) {
                System.out.println("Could not find Duo button, may already be authenticated");
            }

            driver.switchTo().defaultContent();

            // Wait for Duo approval
            System.out.println("\n⚠️  APPROVE DUO PUSH ON YOUR PHONE");
            WaitUtils.sleep(30000);

        } catch (Exception e) {
            System.out.println("Login may already be complete or error: " + e.getMessage());
        }
    }

    public void selectTranscriptOptions() {
        System.out.println("Selecting transcript options...");

        try {
            // Select Graduate level
            waitForElement(transcriptLevelDropdown);
            Select dropdown = new Select(driver.findElement(transcriptLevelDropdown));
            dropdown.selectByVisibleText("Graduate");
            System.out.println("Selected 'Graduate' level");
            WaitUtils.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error selecting options: " + e.getMessage());
        }
    }

    public void clickSubmit() {
        System.out.println("Clicking Submit button...");
        click(submitButton);
        WaitUtils.sleep(5000);
    }

    public void printTranscript() {
        System.out.println("Transcript displayed - Print functionality simulated");
        // Note: Actual printing would require handling system dialog
    }
}
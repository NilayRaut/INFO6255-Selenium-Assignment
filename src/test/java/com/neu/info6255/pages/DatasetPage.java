package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DatasetPage extends BasePage {

    // Updated Locators from Selenium IDE recording
    private By moreLinksButton = By.id("more-links-button");
    private By digitalRepositoryLink = By.cssSelector("div:nth-child(5) > .zero-margin");
    private By datasetsLink = By.linkText("Datasets");
    private By zipFileLink = By.linkText("Zip File");

    public DatasetPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToOneSearch() {
        driver.get("https://onesearch.library.northeastern.edu/discovery/search?vid=01NEU_INST:NU");
        System.out.println("Navigated to OneSearch");
        WaitUtils.sleep(3000);
    }

    public void clickDigitalRepository() {
        System.out.println("Clicking Digital Repository...");

        // Store main window
        String mainWindow = driver.getWindowHandle();

        try {
            waitForElement(digitalRepositoryLink);
            click(digitalRepositoryLink);
            WaitUtils.sleep(3000);

            // Switch to new window
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(mainWindow)) {
                    driver.switchTo().window(windowHandle);
                    System.out.println("Switched to Digital Repository window");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error clicking Digital Repository: " + e.getMessage());
        }
    }

    public void clickDatasets() {
        System.out.println("Clicking Datasets link...");
        waitForElement(datasetsLink);
        click(datasetsLink);
        WaitUtils.sleep(3000);
    }

    public void openFirstDataset() {
        System.out.println("Opening first dataset (not needed for this test)");
        // Dataset is already visible, no need to open
    }

    public void clickZipFile() {
        System.out.println("Attempting to click Zip File link...");

        // Store main window
        String mainWindow = driver.getWindowHandle();

        try {
            waitForElement(zipFileLink);
            click(zipFileLink);
            WaitUtils.sleep(2000);

            // Switch to download window
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(mainWindow)) {
                    driver.switchTo().window(windowHandle);
                    driver.close();
                    driver.switchTo().window(mainWindow);
                    break;
                }
            }

            System.out.println("✓ Zip file link was accessible - TEST SHOULD HAVE FAILED!");

        } catch (Exception e) {
            System.out.println("Expected failure occurred: " + e.getMessage());
            throw e; // Re-throw to fail the test
        }
    }
}
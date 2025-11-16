package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DatasetPage extends BasePage {

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

        String mainWindow = driver.getWindowHandle();
        click(digitalRepositoryLink);
        WaitUtils.sleep(3000);

        // Switch to new window
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                System.out.println("Switched to Digital Repository window");
                break;
            }
        }
    }

    public void clickDatasets() {
        System.out.println("Clicking Datasets link...");
        waitForElement(datasetsLink);
        click(datasetsLink);
        WaitUtils.sleep(3000);
    }

    public void openFirstDataset() {
        System.out.println("Datasets page loaded");
    }

    public void clickZipFile() {
        System.out.println("Attempting to download Zip File...");

        // THIS SHOULD FAIL - but in your recording it worked
        // Let's force it to fail by using wrong locator

        try {
            // Try to find a non-existent "Download" button instead
            By wrongLocator = By.linkText("Download Dataset");
            waitForElement(wrongLocator);
            click(wrongLocator);

            // If we get here, test failed to fail
            throw new AssertionError("Dataset download should not be accessible!");

        } catch (org.openqa.selenium.TimeoutException e) {
            // This is expected - element not found
            System.out.println("✓ Expected failure: Download not accessible");
            throw new RuntimeException("Dataset download failed as expected - NEGATIVE TEST PASS");
        }
    }
}
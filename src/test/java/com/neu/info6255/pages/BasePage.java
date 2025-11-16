package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        driver.findElement(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected void waitForElement(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        WaitUtils.sleep(500);
    }

    protected void selectDropdown(By locator, String visibleText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(locator));
        dropdown.click();
        WaitUtils.sleep(500);

        By optionLocator = By.xpath("//option[text()='" + visibleText + "']");
        click(optionLocator);
    }

    protected void handleDuoAuth() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("⚠️  DUO AUTHENTICATION REQUIRED");
        System.out.println("=".repeat(50));
        System.out.println("Please approve the Duo push notification");
        System.out.println("Waiting 30 seconds...");
        System.out.println("=".repeat(50) + "\n");

        WaitUtils.sleep(30000); // 30 seconds
    }
}
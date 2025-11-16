package com.neu.info6255.base;

import com.neu.info6255.utils.ScreenshotUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String currentScenario;

    @BeforeMethod
    public void setUp() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Setting up browser...");
        System.out.println("=".repeat(60));

        // Setup ChromeDriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        System.out.println("✓ Browser setup complete");
        System.out.println("=".repeat(60) + "\n");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("\n❌ Test FAILED: " + result.getName());
            takeScreenshot("FAILURE");
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.println("\n✅ Test PASSED: " + result.getName());
        }

        if (driver != null) {
            System.out.println("Closing browser...");
            driver.quit();
        }
    }

    protected void takeScreenshot(String step) {
        ScreenshotUtils.takeScreenshot(driver, currentScenario, step);
    }
}
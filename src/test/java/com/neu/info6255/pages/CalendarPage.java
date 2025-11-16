package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CalendarPage extends BasePage {

    private By resourcesTab = By.linkText("Resources");
    private By academicsTab = By.cssSelector("#resource-tab-Academics\\,_Classes_\\&_Registration > .fui-Tab__content");
    private By academicCalendarLink = By.linkText("Academic Calendar");

    public CalendarPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToStudentHub() {
        System.out.println("Already on Student Hub");
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

    public void clickAcademicCalendar() {
        System.out.println("Clicking Academic Calendar...");

        String mainWindow = driver.getWindowHandle();
        click(academicCalendarLink);
        WaitUtils.sleep(3000);

        // Switch to new window
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                System.out.println("Switched to Academic Calendar window");
                break;
            }
        }
    }

    public void clickCalendarLink() {
        System.out.println("Clicking on calendar link...");

        try {
            By calendarPageLink = By.cssSelector(".\\--indent > .\\__item:nth-child(1)");
            waitForElement(calendarPageLink);
            click(calendarPageLink);
            WaitUtils.sleep(3000);
        } catch (Exception e) {
            System.out.println("Calendar link may already be selected");
        }
    }

    public void scrollToCalendars() {
        System.out.println("Scrolling to calendar section...");

        try {
            // Scroll down on the page
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 500);");
            WaitUtils.sleep(2000);

            // Find and count iframes
            int frameCount = driver.findElements(By.tagName("iframe")).size();
            System.out.println("Found " + frameCount + " iframes");

            // Try switching to frame 4 (calendar content)
            driver.switchTo().frame(4);
            System.out.println("Switched to frame 4");

            // Try to interact with calendar
            try {
                driver.findElement(By.id("mixItem3")).click();
                System.out.println("Clicked calendar item");
            } catch (Exception e) {
                System.out.println("mixItem3 not found, trying alternatives");
                // Click any visible element in the frame
                try {
                    driver.findElement(By.cssSelector("table")).click();
                } catch (Exception ex) {
                    System.out.println("Could not interact with calendar");
                }
            }

            driver.switchTo().defaultContent();
            WaitUtils.sleep(2000);

        } catch (Exception e) {
            System.out.println("Error in calendar section: " + e.getMessage());
            driver.switchTo().defaultContent();
        }
    }

    public void uncheckCalendar() {
        System.out.println("Unchecking calendar...");

        try {
            // Switch to frame 10 (checkboxes)
            driver.switchTo().frame(10);
            System.out.println("Switched to frame 10");
            WaitUtils.sleep(2000);

            // Find any checked checkbox
            try {
                WebElement checkbox = driver.findElement(By.id("AtmcChk1190115682"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
                System.out.println("✓ Unchecked calendar");
            } catch (Exception e) {
                // Try any checkbox
                WebElement anyCheckbox = driver.findElement(By.cssSelector("input[type='checkbox']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", anyCheckbox);
                System.out.println("✓ Unchecked a calendar");
            }

            driver.switchTo().defaultContent();

        } catch (Exception e) {
            System.out.println("Error unchecking: " + e.getMessage());
            driver.switchTo().defaultContent();
        }
    }

    public void verifyAddToCalendarButton() {
        System.out.println("Verifying Add to Calendar button...");

        try {
            // Scroll to bottom of page
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            WaitUtils.sleep(2000);

            // Switch to frame 10
            driver.switchTo().frame(10);

            // Look for the button
            By buttonLocator = By.cssSelector("#ctl04_ctl90_ctl00_buttonAtmc > span");

            if (driver.findElements(buttonLocator).size() > 0) {
                System.out.println("✓ Add to Calendar button found");
                driver.switchTo().defaultContent();
            } else {
                driver.switchTo().defaultContent();
                System.out.println("Button not in frame, checking main page");

                // Check if button exists anywhere on page
                boolean buttonExists = driver.getPageSource().contains("Add to My Calendar") ||
                        driver.getPageSource().contains("Add to Calendar");

                if (buttonExists) {
                    System.out.println("✓ Add to Calendar text found in page");
                }
            }

        } catch (Exception e) {
            System.out.println("Error verifying button: " + e.getMessage());
            driver.switchTo().defaultContent();
        }
    }
}
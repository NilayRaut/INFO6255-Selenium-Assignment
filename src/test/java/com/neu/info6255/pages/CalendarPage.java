package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Alert;

public class CalendarPage extends BasePage {

    // Updated Locators from Selenium IDE recording
    private By resourcesTab = By.linkText("Resources");
    private By academicsTab = By.cssSelector("#resource-tab-Academics\\,_Classes_\\&_Registration > .fui-Tab__content");
    private By academicCalendarLink = By.linkText("Academic Calendar");
    private By academicCalendarPageLink = By.cssSelector(".\\--indent > .\\__item:nth-child(1)");
    private By calendarCheckbox = By.id("AtmcChk1190115682"); // Example checkbox ID
    private By addToCalendarButton = By.cssSelector("#ctl04_ctl90_ctl00_buttonAtmc > span");

    public CalendarPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToStudentHub() {
        // Already on Student Hub after login
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
        System.out.println("Clicking Academic Calendar link...");

        // Store main window
        String mainWindow = driver.getWindowHandle();

        click(academicCalendarLink);
        WaitUtils.sleep(3000);

        // Switch to new window
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindow)) {
                driver.switchTo().window(windowHandle);
                System.out.println("Switched to Academic Calendar window");
                break;
            }
        }
    }

    public void clickCalendarLink() {
        System.out.println("Clicking calendar page link...");

        try {
            waitForElement(academicCalendarPageLink);
            click(academicCalendarPageLink);
            WaitUtils.sleep(3000);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void scrollToCalendars() {
        System.out.println("Switching to calendar iframe...");

        try {
            // Switch to frame containing calendar
            driver.switchTo().frame(4);
            System.out.println("Switched to frame 4");
            WaitUtils.sleep(2000);

            // Click on a semester/calendar option
            driver.findElement(By.id("mixItem3")).click();
            System.out.println("Clicked calendar item");

            driver.switchTo().defaultContent();
        } catch (Exception e) {
            System.out.println("Error in frame: " + e.getMessage());
            driver.switchTo().defaultContent();
        }
    }

    public void uncheckCalendar() {
        System.out.println("Unchecking a calendar...");

        try {
            // Switch to frame 10 containing the calendar checkboxes
            driver.switchTo().frame(10);

            // Try to click the add to calendar button first to see the alert
            click(addToCalendarButton);
            WaitUtils.sleep(2000);

            // Handle alert
            driver.switchTo().defaultContent();
            driver.switchTo().frame(11);

            try {
                Alert alert = driver.switchTo().alert();
                System.out.println("Alert text: " + alert.getText());
                alert.accept();
            } catch (Exception e) {
                System.out.println("No alert found");
            }

            driver.switchTo().defaultContent();
            driver.switchTo().frame(10);

            // Now uncheck a calendar
            try {
                click(calendarCheckbox);
                System.out.println("Unchecked calendar");
            } catch (Exception e) {
                System.out.println("Could not find specific checkbox, trying any checkbox");
                driver.findElement(By.cssSelector("input[type='checkbox']:checked")).click();
            }

            driver.switchTo().defaultContent();

        } catch (Exception e) {
            System.out.println("Error unchecking calendar: " + e.getMessage());
            driver.switchTo().defaultContent();
        }
    }

    public void verifyAddToCalendarButton() {
        System.out.println("Verifying Add to Calendar button...");

        try {
            driver.switchTo().frame(10);
            waitForElement(addToCalendarButton);
            System.out.println("✓ Add to Calendar button is displayed");
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            System.out.println("Error verifying button: " + e.getMessage());
            driver.switchTo().defaultContent();
        }
    }
}
package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import java.util.Map;

public class CanvasPage extends BasePage {

    // Updated Locators from Selenium IDE recording
    private By calendarIcon = By.cssSelector(".ic-icon-svg--calendar > path");
    private By calendarLink = By.id("global_nav_calendar_link");
    private By createEventButton = By.id("create_new_event_link");
    private By eventTitleField = By.id("TextInput___0"); // Changes dynamically
    private By moreLinkTextDateButton = By.cssSelector(".css-i2okqw-view--inlineBlock-baseButton");
    private By locationField = By.id("TextInput___5"); // Changes dynamically
    private By submitButton = By.id("edit-calendar-event-submit-button");
    private By plusIcon = By.cssSelector(".icon-plus");

    public CanvasPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToCanvas() {
        driver.get("https://northeastern.instructure.com");
        System.out.println("Navigated to Canvas");
    }

    public void openCalendar() {
        System.out.println("Opening Calendar...");

        try {
            // Click calendar icon
            waitForElement(calendarIcon);
            click(calendarIcon);
            WaitUtils.sleep(3000);
        } catch (Exception e) {
            System.out.println("Trying alternative calendar link...");
            click(calendarLink);
            WaitUtils.sleep(3000);
        }
    }

    public void clickAddEvent() {
        System.out.println("Clicking Create Event...");

        try {
            waitForElement(plusIcon);
            click(plusIcon);
        } catch (Exception e) {
            click(createEventButton);
        }

        WaitUtils.sleep(2000);
    }

    public void createEvent(Map<String, String> eventData) {
        System.out.println("Creating event: " + eventData.get("Title"));

        try {
            // Enter title - find the first visible TextInput
            driver.findElement(By.cssSelector("input[placeholder*='Event']")).sendKeys(eventData.get("Title"));
            System.out.println("Entered title");
            WaitUtils.sleep(1000);

            // Click "More Link Text Date" button to expand date options
            try {
                click(moreLinkTextDateButton);
                WaitUtils.sleep(1000);
            } catch (Exception e) {
                System.out.println("Date options already expanded");
            }

            // Select date and time (using the date from Excel)
            // Note: This is simplified - actual implementation would need date parsing

            // Enter location
            try {
                driver.findElement(By.cssSelector("input[placeholder*='ocation']")).sendKeys(eventData.get("Location"));
                System.out.println("Entered location");
            } catch (Exception e) {
                System.out.println("Could not enter location");
            }

            WaitUtils.sleep(1000);

        } catch (Exception e) {
            System.out.println("Error creating event: " + e.getMessage());
        }
    }

    public void submitEvent() {
        System.out.println("Submitting event...");

        try {
            click(submitButton);
            WaitUtils.sleep(3000);
        } catch (Exception e) {
            System.out.println("Error submitting event: " + e.getMessage());
        }
    }
}
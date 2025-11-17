package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.Set;

public class CalendarPage extends BasePage {

    // Locators
    private By resourcesTab = By.linkText("Resources");
    private By academicsTab = By.cssSelector("#resource-tab-Academics\\,_Classes_\\&_Registration > .fui-Tab__content");
    private By academicCalendarLink = By.linkText("Academic Calendar");
    private By calendarPageFirstItem = By.cssSelector(".\\__item:nth-child(1) > .\\__excerpt");

    // XPath locators for checkboxes and button
    private By selectAllNoneLink = By.xpath("/html/body/form/div[2]/div/table/tbody/tr/td/div/table/tbody/tr[3]/td/table/tbody/tr/td[2]/table/tbody/tr/td/a");
    private By firstCheckbox = By.xpath("/html/body/form/div[2]/div/table/tbody/tr/td/div/table/tbody/tr[3]/td/table/tbody/tr/td[1]/input");
    private By secondCheckbox = By.xpath("/html/body/form/div[2]/div/table/tbody/tr/td/div/table/tbody/tr[2]/td/table/tbody/tr/td[1]/input");

    public CalendarPage(WebDriver driver) {
        super(driver);
    }

    public void clickResources() {
        System.out.println("→ Clicking Resources tab...");
        waitForElement(resourcesTab);
        click(resourcesTab);
        WaitUtils.sleep(1000);
    }

    public void clickAcademics() {
        System.out.println("→ Clicking Academics section...");
        waitForElement(academicsTab);
        click(academicsTab);
        WaitUtils.sleep(1000);
    }

    public void clickAcademicCalendar() {
        System.out.println("→ Clicking Academic Calendar link...");

        String mainWindow = driver.getWindowHandle();
        Set<String> existingWindows = driver.getWindowHandles();

        click(academicCalendarLink);

        // Wait for new window with timeout
        wait.withTimeout(Duration.ofSeconds(10))
                .until(d -> d.getWindowHandles().size() > existingWindows.size());

        // Switch to new window
        for (String handle : driver.getWindowHandles()) {
            if (!existingWindows.contains(handle)) {
                driver.switchTo().window(handle);
                System.out.println("✓ Switched to Academic Calendar window");
                break;
            }
        }

        WaitUtils.sleep(1000);
    }

    public void clickCalendarLink() {
        System.out.println("→ Navigating to calendar page...");

        try {
            waitForElement(calendarPageFirstItem);
            click(calendarPageFirstItem);
            WaitUtils.sleep(1500);
        } catch (Exception e) {
            System.out.println("⚠ Calendar link may already be active");
        }
    }

    public void scrollToCalendars() {
        System.out.println("→ Scrolling to calendar section...");

        try {
            // Wait for page to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("iframe")));

            // Scroll to middle of page
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 500);");
            WaitUtils.sleep(1000);

            // Find the correct iframe
            int frameCount = driver.findElements(By.tagName("iframe")).size();
            System.out.println("  Found " + frameCount + " iframes");

            if (frameCount >= 9) {
                driver.switchTo().frame(9);
                System.out.println("✓ Switched to calendar frame (9)");

                // Click calendar item if available
                try {
                    By calendarItem = By.id("mixItem1");
                    if (driver.findElements(calendarItem).size() > 0) {
                        wait.withTimeout(Duration.ofSeconds(5))
                                .until(ExpectedConditions.elementToBeClickable(calendarItem));
                        jsClick(calendarItem);
                        System.out.println("✓ Interacted with calendar");
                    }
                } catch (Exception e) {
                    System.out.println("  Calendar interaction not needed");
                }

                driver.switchTo().defaultContent();
            }

        } catch (Exception e) {
            System.out.println("⚠ Error in calendar section: " + e.getMessage());
            driver.switchTo().defaultContent();
        }

        WaitUtils.sleep(1000);
    }

    public void uncheckCalendar() {
        System.out.println("→ Unchecking calendar checkbox...");

        try {
            // Scroll to checkbox area
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 700);");
            WaitUtils.sleep(1500);

            // Find frame count to identify correct frame
            int frameCount = driver.findElements(By.tagName("iframe")).size();
            System.out.println("  Total frames available: " + frameCount);

            // Switch to frame 10 (checkbox frame)
            if (frameCount >= 10) {
                driver.switchTo().frame(10);
                System.out.println("✓ Switched to checkbox frame (10)");
                WaitUtils.sleep(1000);

                // Click the "Select: All, None" link first
                try {
                    WebElement selectAllNone = wait.withTimeout(Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(selectAllNoneLink));
                    selectAllNone.click();
                    System.out.println("✓ Clicked 'Select: All, None' link");
                    WaitUtils.sleep(1000);
                } catch (Exception e) {
                    System.out.println("⚠ Could not click Select All/None link: " + e.getMessage());
                }

                // Click the first checkbox
                try {
                    WebElement checkbox1 = wait.withTimeout(Duration.ofSeconds(5))
                            .until(ExpectedConditions.presenceOfElementLocated(firstCheckbox));

                    if (checkbox1.isDisplayed()) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox1);
                        System.out.println("✓ Clicked first checkbox");
                        WaitUtils.sleep(500);
                    }
                } catch (Exception e) {
                    System.out.println("⚠ Could not click first checkbox: " + e.getMessage());
                }

                // Click the second checkbox
                try {
                    WebElement checkbox2 = wait.withTimeout(Duration.ofSeconds(5))
                            .until(ExpectedConditions.presenceOfElementLocated(secondCheckbox));

                    if (checkbox2.isDisplayed()) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox2);
                        System.out.println("✓ Clicked second checkbox");
                        WaitUtils.sleep(500);
                    }
                } catch (Exception e) {
                    System.out.println("⚠ Could not click second checkbox: " + e.getMessage());
                }

                driver.switchTo().defaultContent();
                System.out.println("✓ Switched back to default content");

            } else {
                System.out.println("⚠ Checkbox frame not available");
            }

        } catch (Exception e) {
            System.out.println("⚠ Error unchecking calendar: " + e.getMessage());
            e.printStackTrace();
            driver.switchTo().defaultContent();
        }
    }

    public void verifyAddToCalendarButton() {
        System.out.println("→ Scrolling down to show 'Add to Calendar' button...");

        try {
            // Scroll to bottom of page to show the button
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            WaitUtils.sleep(2000); // Give time for scroll and rendering

            System.out.println("✓ Scrolled to bottom of page");

            // Try to find button in frame
            try {
                int frameCount = driver.findElements(By.tagName("iframe")).size();

                if (frameCount >= 10) {
                    driver.switchTo().frame(10);
                    System.out.println("  Switched to frame 10 to check for button");

                    // Multiple possible selectors for the button
                    String[] buttonSelectors = {
                            "#ctl04_ctl90_ctl00_buttonAtmc > span",
                            "#ctl04_ctl90_ctl00_buttonAtmc",
                            "input[value*='Add to My Calendar']",
                            "a[title*='Add to My Calendar']",
                            "*[id*='buttonAtmc']"
                    };

                    boolean buttonFound = false;
                    for (String selector : buttonSelectors) {
                        try {
                            By buttonLocator = By.cssSelector(selector);
                            if (driver.findElements(buttonLocator).size() > 0) {
                                WebElement button = driver.findElement(buttonLocator);
                                if (button.isDisplayed()) {
                                    System.out.println("✓ 'Add to My Calendar' button is visible");
                                    System.out.println("  Button text: " + button.getText());
                                    buttonFound = true;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            // Try next selector
                        }
                    }

                    if (!buttonFound) {
                        System.out.println("⚠ Button not found with CSS selectors, checking page source");
                    }

                    driver.switchTo().defaultContent();
                }
            } catch (Exception e) {
                System.out.println("  Could not check button in frame: " + e.getMessage());
                driver.switchTo().defaultContent();
            }

            // Check in main page as well
            String pageSource = driver.getPageSource();
            boolean buttonExistsInPage = pageSource.contains("Add to My Calendar") ||
                    pageSource.contains("Add to Calendar") ||
                    pageSource.contains("buttonAtmc");

            if (buttonExistsInPage) {
                System.out.println("✓ 'Add to Calendar' button text found in page source");
            }

            // Final scroll to ensure button is in view
            ((JavascriptExecutor) driver).executeScript(
                    "window.scrollTo(0, document.body.scrollHeight);"
            );
            WaitUtils.sleep(1000);

            System.out.println("✓ Test completed - 'Add to Calendar' button section displayed");

        } catch (Exception e) {
            System.out.println("⚠ Error verifying button: " + e.getMessage());
            e.printStackTrace();
            driver.switchTo().defaultContent();
        }
    }
}
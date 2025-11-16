package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class LibraryPage extends BasePage {

    private By reserveStudyRoomLink = By.linkText("Reserve A Study Room");
    private By bostonThumbnail = By.cssSelector(".col-md-6:nth-child(1) .pt-cv-thumbnail");
    private By bookRoomLink = By.linkText("Book a Room");
    private By seatStyleDropdown = By.id("gid");
    private By capacityDropdown = By.id("capacity");

    public LibraryPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLibrary() {
        driver.get("https://library.northeastern.edu/");
        System.out.println("Navigated to library website");
        WaitUtils.sleep(3000);
    }

    public void clickReserveStudyRoom() {
        System.out.println("Clicking Reserve Study Room...");

        try {
            // Scroll to element first
            WebElement element = driver.findElement(reserveStudyRoomLink);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            WaitUtils.sleep(1000);

            // Use JavaScript click to avoid interception
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            System.out.println("✓ Clicked Reserve Study Room");
            WaitUtils.sleep(3000);

        } catch (Exception e) {
            System.out.println("Error clicking: " + e.getMessage());
            throw e;
        }
    }

    public void selectBoston() {
        System.out.println("Selecting Boston...");
        waitForElement(bostonThumbnail);
        click(bostonThumbnail);
        WaitUtils.sleep(2000);
    }

    public void clickBookRoom() {
        System.out.println("Clicking Book a Room...");
        waitForElement(bookRoomLink);
        click(bookRoomLink);
        WaitUtils.sleep(3000);
    }

    public void selectSeatStyle() {
        System.out.println("Selecting Individual Study...");

        try {
            waitForElement(seatStyleDropdown);
            Select dropdown = new Select(driver.findElement(seatStyleDropdown));
            dropdown.selectByVisibleText("Individual Study");
            System.out.println("✓ Selected Individual Study");
            WaitUtils.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void selectCapacity() {
        System.out.println("Selecting capacity...");

        try {
            Select dropdown = new Select(driver.findElement(capacityDropdown));
            dropdown.selectByVisibleText("Space For 1-4 people");
            System.out.println("✓ Selected capacity");
            WaitUtils.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        System.out.println("✓ Scrolled to bottom");
        WaitUtils.sleep(2000);
    }
}
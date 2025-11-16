package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class LibraryPage extends BasePage {

    // Updated Locators from Selenium IDE recording
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
        waitForElement(reserveStudyRoomLink);
        click(reserveStudyRoomLink);
        WaitUtils.sleep(3000);
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
        System.out.println("Selecting seat style: Individual Study...");

        try {
            waitForElement(seatStyleDropdown);
            Select dropdown = new Select(driver.findElement(seatStyleDropdown));
            dropdown.selectByVisibleText("Individual Study");
            WaitUtils.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error selecting seat style: " + e.getMessage());
        }
    }

    public void selectCapacity() {
        System.out.println("Selecting capacity: Space For 1-4 people...");

        try {
            Select dropdown = new Select(driver.findElement(capacityDropdown));
            dropdown.selectByVisibleText("Space For 1-4 people");
            WaitUtils.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error selecting capacity: " + e.getMessage());
        }
    }

    public void scrollToBottom() {
        scrollToElement(By.tagName("footer"));
        WaitUtils.sleep(2000);
    }
}
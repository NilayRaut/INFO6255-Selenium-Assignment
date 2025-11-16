package com.neu.info6255.tests;

import com.neu.info6255.base.BaseTest;
import com.neu.info6255.pages.LibraryPage;
import com.neu.info6255.pages.LoginPage;
import com.neu.info6255.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;

public class Scenario3_LibraryReservationTest extends BaseTest {

    @Test(priority = 3, description = "Reserve a spot in Snell Library")
    public void testReserveLibrarySpot() {
        currentScenario = "Scenario3_LibraryReservation";

        System.out.println("\n" + "=".repeat(70));
        System.out.println("SCENARIO 3: LIBRARY ROOM RESERVATION");
        System.out.println("=".repeat(70) + "\n");

        // Get login credentials
        Map<String, String> credentials = ExcelUtils.getLoginCredentials();
        String username = credentials.get("Username");
        String password = credentials.get("Password");

        // Step a: Open library website
        LibraryPage libraryPage = new LibraryPage(driver);
        libraryPage.navigateToLibrary();
        takeScreenshot("01_Library_Homepage");

        // Step b: Select 'Reserve a Study Room'
        libraryPage.clickReserveStudyRoom();
        takeScreenshot("02_Reserve_Study_Room_Page");

        // Step c: Select 'Boston'
        libraryPage.selectBoston();
        takeScreenshot("03_Boston_Selected");

        // Step d: Click on 'Book a Room'
        libraryPage.clickBookRoom();
        takeScreenshot("04_Book_Room_Page");

        // Login if required
        if (driver.getCurrentUrl().contains("login") || driver.getCurrentUrl().contains("oauth")) {
            System.out.println("Login required for library booking...");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login(username, password);
            takeScreenshot("05_After_Login");
        }

        // Step e: Select seat style and capacity
        libraryPage.selectSeatStyle();
        takeScreenshot("06_Seat_Style_Selected");

        libraryPage.selectCapacity();
        takeScreenshot("07_Capacity_Selected");

        // Step f: Scroll down to end of page
        libraryPage.scrollToBottom();
        takeScreenshot("08_Scrolled_to_Bottom");

        // Assertion - verify filtering options applied
        Assert.assertTrue(driver.getPageSource().contains("Individual Study") ||
                        driver.getPageSource().contains("Space for"),
                "Study room filters should be applied");

        System.out.println("\n" + "=".repeat(70));
        System.out.println("✓ SCENARIO 3: LIBRARY RESERVATION - PASSED");
        System.out.println("=".repeat(70) + "\n");
    }
}
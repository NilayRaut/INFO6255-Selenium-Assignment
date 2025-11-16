package com.neu.info6255.tests;

import com.neu.info6255.base.BaseTest;
import com.neu.info6255.pages.CalendarPage;
import com.neu.info6255.pages.LoginPage;
import com.neu.info6255.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;

public class Scenario5_AcademicCalendarTest extends BaseTest {

    @Test(priority = 5, description = "Update the Academic Calendar")
    public void testUpdateAcademicCalendar() {
        currentScenario = "Scenario5_AcademicCalendar";

        System.out.println("\n" + "=".repeat(70));
        System.out.println("SCENARIO 5: UPDATE ACADEMIC CALENDAR");
        System.out.println("=".repeat(70) + "\n");

        // Get login credentials
        Map<String, String> credentials = ExcelUtils.getLoginCredentials();
        String username = credentials.get("Username");
        String password = credentials.get("Password");

        // Step a: Navigate to Student Hub and login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToNEU();
        takeScreenshot("01_NEU_Portal");

        loginPage.login(username, password);
        takeScreenshot("02_After_Login");

        // Navigate to calendar
        CalendarPage calendarPage = new CalendarPage(driver);

        // Step b: Click on Resources
        calendarPage.clickResources();
        takeScreenshot("03_Resources_Tab");

        // Click on 'Academics, Classes & Registration'
        calendarPage.clickAcademics();
        takeScreenshot("04_Academics_Section");

        // Step c: Click on 'Academic Calendar'
        calendarPage.clickAcademicCalendar();
        takeScreenshot("05_Academic_Calendar_Link");

        // Step d: Click on 'Academic Calendar' under Registrar
        calendarPage.clickCalendarLink();
        takeScreenshot("06_Calendar_Page");

        // Step e: Scroll down to calendars
        calendarPage.scrollToCalendars();
        takeScreenshot("07_Calendar_Section");

        // Step f: Uncheck any one checkbox
        calendarPage.uncheckCalendar();
        takeScreenshot("08_Calendar_Unchecked");

        // Step g: Verify 'Add to My Calendar' button is displayed
        calendarPage.verifyAddToCalendarButton();
        takeScreenshot("09_Add_to_Calendar_Button");

        // Assertion
        Assert.assertTrue(driver.getPageSource().contains("Add to My Calendar") ||
                        driver.getPageSource().contains("Add to Calendar"),
                "Add to Calendar button should be visible");

        System.out.println("\n" + "=".repeat(70));
        System.out.println("✓ SCENARIO 5: ACADEMIC CALENDAR - PASSED");
        System.out.println("=".repeat(70) + "\n");
    }
}
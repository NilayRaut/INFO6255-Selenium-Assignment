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

        // Get credentials from Excel
        Map<String, String> credentials = ExcelUtils.getLoginCredentials();
        String username = credentials.get("Username");
        String password = credentials.get("Password");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToNEU();
        takeScreenshot("01_NEU_Portal");

        loginPage.login(username, password);
        takeScreenshot("02_After_Login");

        // Navigate through calendar
        CalendarPage calendarPage = new CalendarPage(driver);

        calendarPage.clickResources();
        takeScreenshot("03_Resources_Tab");

        calendarPage.clickAcademics();
        takeScreenshot("04_Academics_Section");

        calendarPage.clickAcademicCalendar();
        takeScreenshot("05_Academic_Calendar_Window");

        calendarPage.clickCalendarLink();
        takeScreenshot("06_Calendar_Page");

        calendarPage.scrollToCalendars();
        takeScreenshot("07_Calendar_Section");

        calendarPage.uncheckCalendar();
        takeScreenshot("08_Calendar_Unchecked");

        calendarPage.verifyAddToCalendarButton();
        takeScreenshot("09_Verification_Complete");

        // Assertion - verify we're on the calendar page
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource();
        String pageTitle = driver.getTitle();

        boolean onCalendarPage = currentUrl.contains("calendar") ||
                currentUrl.contains("registrar") ||
                pageSource.contains("Academic Calendar") ||
                pageTitle.toLowerCase().contains("calendar");

        Assert.assertTrue(onCalendarPage,
                "Should be on Academic Calendar page. Current URL: " + currentUrl);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("✓ SCENARIO 5: ACADEMIC CALENDAR - PASSED");
        System.out.println("=".repeat(70) + "\n");
    }
}
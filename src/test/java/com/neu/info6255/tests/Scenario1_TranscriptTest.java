package com.neu.info6255.tests;

import com.neu.info6255.base.BaseTest;
import com.neu.info6255.pages.LoginPage;
import com.neu.info6255.pages.TranscriptPage;
import com.neu.info6255.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;

public class Scenario1_TranscriptTest extends BaseTest {

    @Test(priority = 1, description = "Download the latest transcript")
    public void testDownloadTranscript() {
        currentScenario = "Scenario1_Transcript";

        System.out.println("\n" + "=".repeat(70));
        System.out.println("SCENARIO 1: DOWNLOAD TRANSCRIPT");
        System.out.println("=".repeat(70) + "\n");

        // Get login credentials from Excel
        Map<String, String> credentials = ExcelUtils.getLoginCredentials();
        String username = credentials.get("Username");
        String password = credentials.get("Password");

        // Step a: Log in to My NEU portal
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToNEU();
        takeScreenshot("01_Navigate_to_NEU");

        loginPage.login(username, password);
        takeScreenshot("02_After_Login");

        // Step b: Launch the Student Hub portal (already on it after login)
        TranscriptPage transcriptPage = new TranscriptPage(driver);
        transcriptPage.navigateToStudentHub();
        takeScreenshot("03_Student_Hub");

        // Step c: Hit the Resources tab
        transcriptPage.clickResources();
        takeScreenshot("04_Resources_Tab");

        // Step d: Go to 'Academics, Classes & Registration'
        transcriptPage.clickAcademics();
        takeScreenshot("05_Academics_Section");

        // Step e: Go to 'My Transcripts'
        transcriptPage.clickMyTranscripts();
        takeScreenshot("06_My_Transcripts_Page");

        // Login to transcript portal (new window requires separate login)
        transcriptPage.loginToTranscriptPortal(username, password);
        takeScreenshot("06a_Transcript_Login");

        // Step f: Select 'Graduate' and 'Audit Transcript'
        transcriptPage.selectTranscriptOptions();
        takeScreenshot("07_Select_Options");

        transcriptPage.clickSubmit();
        takeScreenshot("08_Transcript_Displayed");

        // Step g: Right-click and print (save as PDF)
        transcriptPage.printTranscript();
        takeScreenshot("09_Print_Dialog");

        // Assertion - verify URL contains transcript
        Assert.assertTrue(driver.getCurrentUrl().contains("transcript") ||
                        driver.getPageSource().contains("Academic Transcript"),
                "Transcript page should be displayed");

        System.out.println("\n" + "=".repeat(70));
        System.out.println("✓ SCENARIO 1: DOWNLOAD TRANSCRIPT - PASSED");
        System.out.println("=".repeat(70) + "\n");
    }
}
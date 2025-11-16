package com.neu.info6255.tests;

import com.neu.info6255.base.BaseTest;
import com.neu.info6255.pages.DatasetPage;
import com.neu.info6255.pages.LoginPage;
import com.neu.info6255.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;

public class Scenario4_DatasetDownloadTest extends BaseTest {

    @Test(priority = 4, description = "Download a DATASET - NEGATIVE SCENARIO (Must Fail)",
            expectedExceptions = Exception.class)
    public void testDownloadDataset_ShouldFail() {
        currentScenario = "Scenario4_Dataset_Negative";

        System.out.println("\n" + "=".repeat(70));
        System.out.println("SCENARIO 4: DATASET DOWNLOAD (NEGATIVE TEST - SHOULD FAIL)");
        System.out.println("=".repeat(70) + "\n");

        // Get login credentials
        Map<String, String> credentials = ExcelUtils.getLoginCredentials();
        String username = credentials.get("Username");
        String password = credentials.get("Password");

        // Step a: Open OneSearch and click Digital Repository
        DatasetPage datasetPage = new DatasetPage(driver);
        datasetPage.navigateToOneSearch();
        takeScreenshot("01_OneSearch_Page");

        datasetPage.clickDigitalRepository();
        takeScreenshot("02_Digital_Repository");

        // Login if required
        if (driver.getCurrentUrl().contains("login") || driver.getCurrentUrl().contains("oauth")) {
            System.out.println("Login required...");
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login(username, password);
            takeScreenshot("03_After_Login");
        }

        // Step b: Click on 'Datasets'
        datasetPage.clickDatasets();
        takeScreenshot("04_Datasets_Page");

        datasetPage.openFirstDataset();
        takeScreenshot("05_Dataset_Details");

        // Step c: Click on "Zip File" - THIS SHOULD FAIL
        try {
            datasetPage.clickZipFile();
            takeScreenshot("06_Attempted_Download");

            // If we reach here, the test failed to fail
            Assert.fail("Test should have failed but didn't - Zip file was accessible");

        } catch (Exception e) {
            takeScreenshot("07_Expected_Failure");
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓ SCENARIO 4: DATASET DOWNLOAD - FAILED AS EXPECTED");
            System.out.println("=".repeat(70) + "\n");

            // Re-throw to mark test as failed (expected)
            throw e;
        }
    }
}
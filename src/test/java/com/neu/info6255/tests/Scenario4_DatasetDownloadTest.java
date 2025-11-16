package com.neu.info6255.tests;

import com.neu.info6255.base.BaseTest;
import com.neu.info6255.pages.DatasetPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Scenario4_DatasetDownloadTest extends BaseTest {

    @Test(priority = 4, description = "Download a DATASET - NEGATIVE SCENARIO (Must Fail)")
    public void testDownloadDataset_ShouldFail() {
        currentScenario = "Scenario4_Dataset_Negative";

        System.out.println("\n" + "=".repeat(70));
        System.out.println("SCENARIO 4: DATASET DOWNLOAD (NEGATIVE TEST - MUST FAIL)");
        System.out.println("=".repeat(70) + "\n");

        DatasetPage datasetPage = new DatasetPage(driver);
        datasetPage.navigateToOneSearch();
        takeScreenshot("01_OneSearch_Page");

        datasetPage.clickDigitalRepository();
        takeScreenshot("02_Digital_Repository");

        datasetPage.clickDatasets();
        takeScreenshot("04_Datasets_Page");

        datasetPage.openFirstDataset();
        takeScreenshot("05_Dataset_Details");

        // This should throw RuntimeException with message about failure
        boolean testFailed = false;
        String failureMessage = "";

        try {
            datasetPage.clickZipFile();
            takeScreenshot("06_Unexpected_Success");

        } catch (RuntimeException e) {
            testFailed = true;
            failureMessage = e.getMessage();
            takeScreenshot("07_Expected_Failure");
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓ SCENARIO 4: NEGATIVE TEST PASSED (Failed as expected)");
            System.out.println("Failure reason: " + failureMessage);
            System.out.println("=".repeat(70) + "\n");
        }

        // Assert that the test failed
        Assert.assertTrue(testFailed, "Dataset download MUST fail for negative test");
    }
}
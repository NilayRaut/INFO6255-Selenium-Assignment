package com.neu.info6255.pages;

import com.neu.info6255.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Microsoft Azure AD login locators
    private By emailField = By.name("loginfmt");
    private By nextButton = By.id("idSIButton9");
    private By passwordField = By.name("passwd");
    private By signInButton = By.id("idSIButton9");
    private By staySignedInButton = By.id("idSIButton9");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Starting Microsoft Azure AD Login Process");
        System.out.println("=".repeat(60));
        System.out.println("Username: " + username);

        try {
            // Wait for Microsoft login page
            WaitUtils.sleep(5000);

            // Step 1: Enter email
            System.out.println("\nStep 1: Entering email...");
            waitForElement(emailField);
            type(emailField, username);
            System.out.println("✓ Email entered");
            WaitUtils.sleep(1000);

            // Step 2: Click Next
            System.out.println("\nStep 2: Clicking 'Next' button...");
            click(nextButton);
            System.out.println("✓ Next clicked");
            WaitUtils.sleep(3000);

            // Step 3: Enter password
            System.out.println("\nStep 3: Entering password...");
            waitForElement(passwordField);
            type(passwordField, password);
            System.out.println("✓ Password entered");
            WaitUtils.sleep(1000);

            // Step 4: Click Sign In
            System.out.println("\nStep 4: Clicking 'Sign in' button...");
            click(signInButton);
            System.out.println("✓ Sign in clicked");
            WaitUtils.sleep(5000);

            // Step 5: Handle "Stay signed in?" prompt
            System.out.println("\nStep 5: Checking for 'Stay signed in?' prompt...");
            try {
                if (driver.findElements(staySignedInButton).size() > 0) {
                    System.out.println("Found 'Stay signed in?' prompt - clicking Yes");
                    click(staySignedInButton);
                    WaitUtils.sleep(3000);
                }
            } catch (Exception e) {
                System.out.println("No 'Stay signed in?' prompt found - continuing");
            }

            // Step 6: Handle Duo authentication
            handleDuoAuth();

            System.out.println("\n" + "=".repeat(60));
            System.out.println("✅ Login Process Completed");
            System.out.println("=".repeat(60) + "\n");

        } catch (Exception e) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("❌ LOGIN ERROR");
            System.out.println("=".repeat(60));
            System.out.println("Error: " + e.getMessage());
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("=".repeat(60) + "\n");
            throw e;
        }
    }

    public void navigateToNEU() {
        driver.get("https://me.northeastern.edu");
        System.out.println("Navigated to NEU portal: https://me.northeastern.edu");
        WaitUtils.sleep(3000);
    }

    @Override
    protected void handleDuoAuth() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⚠️  DUO AUTHENTICATION REQUIRED");
        System.out.println("=".repeat(60));
        System.out.println("ACTION NEEDED:");
        System.out.println("1. Check your phone for Duo Mobile notification");
        System.out.println("2. Approve the login request");
        System.out.println("3. Or press Enter on the Duo prompt");
        System.out.println("\nWaiting 30 seconds for authentication...");
        System.out.println("=".repeat(60) + "\n");

        WaitUtils.sleep(30000); // 30 seconds

        System.out.println("✓ Duo authentication window completed");
    }
}
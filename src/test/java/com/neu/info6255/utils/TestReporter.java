package com.neu.info6255.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestReporter {

    private static List<TestResult> results = new ArrayList<>();

    public static class TestResult {
        String scenarioName;
        String actual;
        String expected;
        String status;

        public TestResult(String scenarioName, String actual, String expected, String status) {
            this.scenarioName = scenarioName;
            this.actual = actual;
            this.expected = expected;
            this.status = status;
        }
    }

    public static void addResult(String scenarioName, String actual, String expected, String status) {
        results.add(new TestResult(scenarioName, actual, expected, status));
    }

    public static void generateHTMLReport() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "test-output/TestReport_" + timestamp + ".html";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n<head>\n");
            writer.write("<title>INFO6255 Selenium Test Report</title>\n");
            writer.write("<style>\n");
            writer.write("body { font-family: Arial, sans-serif; margin: 20px; }\n");
            writer.write("h1 { color: #333; }\n");
            writer.write("table { border-collapse: collapse; width: 100%; margin-top: 20px; }\n");
            writer.write("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n");
            writer.write("th { background-color: #4CAF50; color: white; }\n");
            writer.write("tr:nth-child(even) { background-color: #f2f2f2; }\n");
            writer.write(".pass { color: green; font-weight: bold; }\n");
            writer.write(".fail { color: red; font-weight: bold; }\n");
            writer.write("</style>\n");
            writer.write("</head>\n<body>\n");
            writer.write("<h1>INFO6255 Selenium Assignment - Test Report</h1>\n");
            writer.write("<p>Execution Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "</p>\n");
            writer.write("<table>\n");
            writer.write("<tr><th>Test Scenario Name</th><th>Actual</th><th>Expected</th><th>Pass/Fail</th></tr>\n");

            for (TestResult result : results) {
                String statusClass = result.status.equalsIgnoreCase("PASS") ? "pass" : "fail";
                writer.write("<tr>");
                writer.write("<td>" + result.scenarioName + "</td>");
                writer.write("<td>" + result.actual + "</td>");
                writer.write("<td>" + result.expected + "</td>");
                writer.write("<td class='" + statusClass + "'>" + result.status + "</td>");
                writer.write("</tr>\n");
            }

            writer.write("</table>\n");
            writer.write("</body>\n</html>");

            System.out.println("\n✓ HTML Report generated: " + fileName);

        } catch (IOException e) {
            System.err.println("Failed to generate HTML report: " + e.getMessage());
        }
    }
}
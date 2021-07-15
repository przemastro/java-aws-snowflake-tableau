package com.company.poc.pages;


import com.company.poc.utils.Configuration;
import org.junit.Assert;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class TableauPage extends MainPage {

    public void navigateToReport(String url) {
        driver.get(url);
    }

    public void updateJSScript(String url, String reportName) throws IOException {
        String data= "";
        data = Files.readString(Paths.get(Configuration.filePath+"\\src\\test\\resources\\jsScripts\\template.js"));

        data = data.replace("$url", "\""+url+"\"");
        data = data.replace("$reportName", "\""+reportName+"\"");

        Files.write(
                Paths.get(Configuration.filePath+"\\src\\test\\resources\\jsScrips\\script.js"),
                data.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.WRITE);
    }

    public void verifyReportIsgenerated(String data) throws InterruptedException {
        driver.get("file:\\\\"+Configuration.filePath+"\\src\\test\\resources\\index.html");

        Thread.sleep(10000);
        LogEntries log = driver.manage().logs().get(LogType.BROWSER);
        List<LogEntry> logs = log.getAll();
        for (LogEntry e : logs) {
            if (data != null) {
                if (e.toString().contains("script.js")) {
                    Assert.assertTrue(e.toString().contains(data));
                }
            }
        }
    }
}

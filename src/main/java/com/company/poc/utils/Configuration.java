package com.company.poc.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

@Slf4j
public class Configuration {

    public static final String SQL_SCRIPTS_PATH = "src/test/resources/sqlScripts/";
    public static final String TXT_PATH = "src/test/resources/txtFiles/";
    public static final String JS_SCRIPTS_PATH = "src/test/resources/jsScripts/";
    public static WebDriver driver;
    public static String filePath = new File("").getAbsolutePath();

    public static ConfigDataModel setupConfiguration() throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File("src/test/resources/functional-automated-tests.yaml"), ConfigDataModel.class);
    }

    public static  WebDriver setupChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        var logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        options.setCapability("goog:loggingPrefs", logPrefs);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        driver = new ChromeDriver(options);
        return driver;
    }

    public static void setupDriver() {
        setupChromeDriver();
    }

    public static void quitDriver() {
        driver.quit();
    }
}

package com.company.poc;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = {"src/test/java/com/company/poc/features/e2e/"},
        glue = {"com/company/poc/steps/aws"},
        tags = {"@Test"})
public class BackendRunner {
}

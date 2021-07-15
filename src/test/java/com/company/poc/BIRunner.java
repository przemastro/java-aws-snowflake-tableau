package com.company.poc;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = {"src/test/java/com/company/poc/features/bi/"},
        glue = {"com/company/poc/steps/bi"},
        tags = {"@Test"})
public class BIRunner {
}

package com.company.poc.steps.bi;

import com.company.poc.utils.Configuration;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class WebTestSteps {

    @Before
    public void beforeScenario() {
        Configuration.setupDriver();
    }

    @After
    public void afterScenario() {
        Configuration.quitDriver();
    }
}

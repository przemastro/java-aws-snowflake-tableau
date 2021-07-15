package com.company.poc.steps.bi;

import com.company.poc.pages.TableauPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import java.io.IOException;

public class TableauSteps {

    private final TableauPage tableauPage = new TableauPage();

    @Given("I navigate to {string}")
    public void iNavigateTo(String url) {
        urlAddress = url;
        tableauPage.navigateToReport(url);
    }

    @And("I verify {string} report is generated with data {string}")
    public void iVerifyReportIsGeneratedWithData(String reportName, String data) throws IOException, InterruptedException {
        tableauPage.updateJSScript(urlAddress, reportName);
        tableauPage.verifyReportIsgenerated(data);
    }
}

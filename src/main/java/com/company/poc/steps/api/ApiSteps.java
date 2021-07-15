package com.company.poc.steps.api;

import com.company.poc.utils.ApiRest;
import cucumber.api.java.en.Given;

import java.io.IOException;

public class ApiSteps extends ApiRest {

    @Given("I send {string} request to {string} endpoint")
    public void sendRequestToEndpoint(String method, String endpoint) throws IOException {
        request(method, endpoint);
    }
}

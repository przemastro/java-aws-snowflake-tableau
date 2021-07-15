package com.company.poc.utils;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.internal.net.minidev.json.JSONObject;

import java.io.IOException;

@Slf4j
public class ApiRest {

    private Response lastResponse;
    private String lastRequestBody;
    private String lastToken;
    JSONObject requestParams;

    public void request(String method, String endpoint, String body, String token) throws IOException {
        var config = Configuration.setupConfiguration();
        final String requestUrl = config.getApiUrl() + endpoint;

        lastRequestBody = body;

        RequestSpecification request = RestAssured.given();
        request.accept("*/*");
        request.body(body);

        log.info("Sent " + method + " request to " + requestUrl);
        if(!body.equals("")) {
            request.contentType(body);
            log.info("Request body:\n" + body);
        }
        if(!token.equals("")) {
            request.header("Authorization", "Bearer $token");
        }
        switch (method.toUpperCase()) {
            case "GET":
                lastResponse = request.get(requestUrl);
                break;
            case "POST":
                lastResponse = request.post(requestUrl);
                break;
            case "PUT":
                lastResponse = request.put(requestUrl);
                break;
            case "DELETE":
                lastResponse = request.delete(requestUrl);
                break;
            default:
                lastResponse = null;
        }
        if (lastResponse != null) {
            log.info("Response body:\n" + lastResponse.asString());
        }
    }

    public void request(String method, String endpoint) throws IOException {
        request(method, endpoint, "", "");
    }

    public void getToken(String login, String password) throws IOException {
        authentication(login, password);
    }

    public void authentication(String login, String password) throws IOException {
        var config = Configuration.setupConfiguration();
        final String requestUrl = config.getApiUrl();
        final String authenticationEndpoint = config.getAuthenticationEndpoint();

        requestParams = new JSONObject();
        requestParams.put("user", login);
        requestParams.put("password", password);

        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestParams.toJSONString());
        log.info("Getting Token ...");
        lastResponse = request.post(requestUrl+authenticationEndpoint);
        lastToken= getLastResponseBodyToken();
    }

    public void sendAuthenticationRequest(String method, String endpoint) throws IOException {
        request(method, endpoint, "", lastToken);
    }

    public String getLastRequestBody() {
        return lastRequestBody;
    }

    public String getLastResponseBodyToken() {
        return lastResponse.then().extract().path("token").toString();
    }

    public Response getLastResponse() {
        return lastResponse;
    }
}

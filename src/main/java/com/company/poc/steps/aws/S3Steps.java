package com.company.poc.steps.aws;

import com.company.poc.utils.S3Loader;
import cucumber.api.java.en.Given;

import java.io.IOException;

public class S3Steps extends S3Loader {

    @Given("I load {string} to {string} bucket and {string} catalog")
    public void iLoadToBucket(String fileName, String bucketName, String catalog) throws IOException, InterruptedException {
        loader(fileName, bucketName, catalog);
    }

    @Given("I run s3 step")
    public void iRunS3Step() throws IOException, InterruptedException {
        s3Client();
    }
}

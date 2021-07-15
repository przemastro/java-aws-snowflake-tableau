package com.company.poc.utils;

import cucumber.api.java.hu.Ha;
import groovy.util.logging.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

@Slf4j
public class S3Loader {

    protected String s3AccessKey;
    protected String s3SecretKey;
    protected String s3SessionToken;

    public S3Client s3Client() throws IOException {
        var config = Configuration.setupConfiguration();

        HashMap<String, String> properties = getProperties(config);

        s3AccessKey = properties.get("awsAccessKey");
        s3SecretKey = properties.get("awsSecretKey");
        s3SessionToken = properties.get("awsSessionToken");

        log.info("Setting Credentials");

        AwsCredentials credentials = AwsSessionCredentials.create(
                s3AccessKey,
                s3SecretKey,
                s3SessionToken);

        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

        S3Client s3Client = S3Client.builder()
                .credentialsProvider(provider)
                .region(Region.AF_SOUTH_1)
                .build();
        return s3Client;
    }

    public HashMap<String, String> getProperties(ConfigDataModel config) {
        HashMap<String, String> properties = mavenProperties();
        HashMap<String, String> yamlProperties = yamlProperties(config);
        yamlProperties.forEach(
                (key, value) -> properties.merge(key, value, (v1, v2) -> !v1.isEmpty() ? v1 : v1 + "," + v2)
        );
        return properties;
    }

    public HashMap<String, String> mavenProperties() {
        HashMap<String, String> mavenProperties = new HashMap<>();
        mavenProperties.put("awsAccessKey", System.getProperty("AwsAccessKey"));
        mavenProperties.put("awsSecretKey", System.getProperty("AwsSecretKey"));
        mavenProperties.put("awsSessionToken", System.getProperty("AwsSessionToken"));
        mavenProperties.put("awsUsername", System.getProperty("AwsUsername"));
        mavenProperties.put("awsPassword", System.getProperty("AwsPassword"));
        mavenProperties.put("awsRegion", System.getProperty("AwsRegion"));
        return mavenProperties;
    }

    public HashMap<String, String> yamlProperties(ConfigDataModel config) {
        HashMap<String, String> yamlProperties = new HashMap<>();
        yamlProperties.put("awsAccessKey", config.getAccessKey());
        yamlProperties.put("awsSecretKey", config.getSecretKey());
        yamlProperties.put("awsSessionToken", config.getSessionToken());
        yamlProperties.put("awsUsername", config.getUsername());
        yamlProperties.put("awsPassword", config.getPassword());
        return yamlProperties;
    }

    public void loader(String fileName, String bucketName, String catalog) throws IOException, InterruptedException {
        var config = Configuration.setupConfiguration();
        String filesCatalog = config.getFiles();
        log.info("Uploading file - " + fileName + " to bucket - " + bucketName);
        String newName = fileName+"_"+System.currentTimeMillis()+".txt";

        renameFile(fileName, newName);
        Path path = new File(filesCatalog + newName).toPath();

        try {
            s3Client().putObject(PutObjectRequest
                    .builder().bucket(bucketName).key(catalog + "/" + newName)
                    .build(), path);
            renameFile(newName, fileName);
        } catch (Exception e) {
            log.info("file not loaded");
            renameFile(newName, fileName);
        }
    }

    public void renameFile(String oldName, String newName) {
        File file = new File("src/test/resources/files/"+oldName);
        File file2 = new File("src/test/resources/files/"+newName);
        file.renameTo(file2);
    }
}

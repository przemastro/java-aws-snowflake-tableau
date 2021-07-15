package com.company.poc.utils;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ConfigDataModel {
    String snowflakeUrl;
    String snowflakeUserName;
    String snowflakePassword;
    String snowflakeRole;
    String snowflakeDatabase;
    String snowflakeWarehouse;
    String apiUrl;
    String authenticationEndpoint;
    String accessKey;
    String secretKey;
    String sessionToken;
    String s3Region;
    String username;
    String password;
    String files;
}

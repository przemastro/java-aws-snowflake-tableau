package com.company.poc.utils;

import com.company.poc.steps.snowflake.SnowflakeSteps;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.SnowflakeStatement;
import org.junit.Assert;

import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
import java.sql.*;
import java.util.*;

@Slf4j
public class QueryExecutor {

    private static final String NEWLINE_REGEX = "[\r\n]+";
    private List<Map<String, Object>> result;
    protected String dbConnectionUrl;
    public String database;
    public String role;

    public void setDatabaseAndRole(String testDb, String testRole) {
        database = testDb;
        role = testRole;
    }

    private void saveResult(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            return;
        }
        int columnNumber = resultSet.getMetaData().getColumnCount();
        List<String> columnNames = new ArrayList<>();
        for (var i = 1; i<=columnNumber; i++) {
            columnNames.add(resultSet.getMetaData().getColumnName(i));
        }
        log.info("Saving result set");
        result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new HashMap<>());
            for (var i = 1; i <= columnNumber; i++) {
                result.get(result.size() - 1)
                        .put(columnNames.get(i - 1), resultSet.getObject(i));
            }
        }
    }

    private Connection createConnectionToDB() throws SQLException, IOException {
        var config = Configuration.setupConfiguration();
        var props = new Properties();
        HashMap<String, String> properties = getProperties(config);

        environmentProperties(props, properties.get("snowflakeUserName"), properties.get("snowflakePassword"),
                properties.get("snowflakeRole"), properties.get("snowflakeWarehouse"), properties.get("snowflakeDatabase"));

        if (System.getProperty("SnowflakeUrl") == null) {
            dbConnectionUrl = Configuration.setupConfiguration().getSnowflakeUrl();
        }
        else {
            dbConnectionUrl = System.getProperty("SnowflakeUrl");
        }
        log.info("Connection to DB");
        return DriverManager.getConnection(dbConnectionUrl, props);
    }

    public HashMap<String, String> getProperties(ConfigDataModel config) {
        HashMap<String, String> properties = mavenProperties();
        HashMap<String, String> yamlProperties = yamlProperties(config);
        yamlProperties.forEach(
                (key, value) -> properties.merge(key, value, (v1, v2)) -> !v1.isEmpty() ? v1 : v1 + "," + v2)
        );
        return properties;
    }

    public HashMap<String, String> mavenProperties() {
        HashMap<String, String> mavenProperties = new HashMap<>();
        mavenProperties.put("snowflakeUserName", System.getProperty("snowflakeUserName"));
        mavenProperties.put("snowflakePassword", System.getProperty("snowflakePassword"));
        mavenProperties.put("snowflakeRole", System.getProperty("snowflakeRole"));
        mavenProperties.put("snowflakeWarehouse", System.getProperty("snowflakeWarehouse"));
        mavenProperties.put("snowflakeDatabase", System.getProperty("snowflakeDatabase"));
        return mavenProperties;
    }

    public HashMap<String, String> yamlProperties(ConfigDataModel config) {
        HashMap<String, String> yamlProperties = new HashMap<>();
        yamlProperties.put("snowflakeUserName", config.getSnowflakeUsername());
        yamlProperties.put("snowflakePassword", config.getSnowflakePassword());
        yamlProperties.put("snowflakeRole", config.getSnowflakeRole());
        yamlProperties.put("snowflakeWarehouse", config.getSnowflakeWarehouse());
        yamlProperties.put("snowflakeDatabase", config.getSnowflakeDatabase());
        return yamlProperties;
    }

    private Properties environmentProperties(Properties props, String userName, String password, String role, String warehouse, String database) {
        props.put("user", userName);
        props.put("password", password);
        props.put("role", "role");
        props.put("warehouse", warehouse);
        props.put("database", database);
        return props;
    }

    public void execQuery(String query, String schema, long stmtNumber) throws SQLException, IOException {
        execQuery(query, null, schema, stmtNumber);
    }

    private QueryExecutor execQuery(String query, List<String> parameters, String schema, long stmtNumber) throws SQLException, IOException {
        String preparedQuery = query.replaceAll(NEWLINE_REGEX, "");
        var con = createConnectionToDB();

        if(!schema.equals("")) {
            try(var cstmt = con.createStatement()) {
                log.info("executing use schema statement");
                cstmt.execute("USE SCHEMA " + schema + ";");
            }
        }
        try(PreparedStatement stmt = con.prepareStatement(preparedQuery)) {
            if(parameters != null) {
                for(var i = 0; i<parameters.size();i++) {
                    stmt.setString(i+1,parameters.get(i));
                }
            }
            log.info("Executing Multistatement Query");
            stmt.unwrap(SnowflakeStatement.class).setParameter("MULTI_STATEMENT_COUNT", stmtNumber);
            stmt.execute();
            saveResult(stmt.getResultSet());
        }
        return this;
    }

    public void countRowsInTable(String query, int expectedResult, QueryModel queryModel, String schema, long stmtNumber) throws IOException, SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(queryModel.getTableName());
        log.info("Executing Query - " + query);
        execQuery(query, parameters, schema, stmtNumber);
        var actualResult = Integer.parseInt(result.get(0).values().toArray()[0].toString());
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void validateValueInTable(String query, String expectedResult, String tableName, String schema, long stmtNumber) throws IOException, SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(tableName);
        log.info("Executing Query - " + query);
        execQuery(query, parameters, schema, stmtNumber);
        var actualResult = result.get(0).values().toArray()[0].toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    public String validateTable(String query, QueryModel queryModel, String schema, long stmtNumber) throws IOException, SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(queryModel.getTableName());
        log.info("Executing Query - " + query);
        execQuery(query, parameters, schema, stmtNumber);
        return result.toString();
    }
}

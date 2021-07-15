package com.company.poc.steps.snowflake;

import com.company.poc.utils.DataUtil;
import com.company.poc.utils.QueryExecutor;
import com.company.poc.utils.QueryModel;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SnowflakeSteps extends QueryExecutor {

    @Given("I run query from file {string}")
    public void iRunQueryFromFile(String fileName) throws IOException, SQLException {
        final String query = DataUtil.readQueryFromFile(fileName);
        if(query != null) {
            execQuery(query, "", DataUtil.countFilesLines(fileName));
        }
    }

    @Given("I run query {string} on schema {string}")
    public void iRunQuery(String query, String schema) throws IOException, SQLException {
            execQuery(query, schema, 1);
    }

    @Given("I verify number of rows in Table {string} equals {string} using query from file {string} on schema {string}")
    public void iCountRowsInTableUsingQueryFromFile(String tableName, String expectedResult, String fileName, String schema) throws IOException, SQLException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.tableName(tableName);
        final String query = DataUtil.readQueryFromFile(fileName);
        if(query != null) {
            execQuery(query, Integer.parseInt(expectedResult), queryBuilder.build(), schema, DataUtil.countFilesLines(fileName));
        }
    }

    @And("I verify column values in Table {string} using query from file {string} on schema {string}")
    public void iVerifyColumnValuesInTableUsingQueryFromFileOnSchema(String tableName, String fileName, String schema, DataTable dataTable) throws IOException, SQLException {
        List<Map<String, String>> data = dataTable.asMaps();
        for (Map<String, String> form : data) {
            String columnName = form.get("columnName");
            String expectedResult = form.get("expectedValue");

            QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
            queryBuilder.tableName(tableName);
            if(query != null) {
                query = query.replace("columnName", columnName);
                validateValueInTable(query, expectedResult, queryBuilder.build(), schema, DataUtil.countFilesLines(fileName));
            }
        }
    }

    @And("I validate table {string} on schema {string} using query {string} and {string} expected result")
    public void iValidateTablesUsingQueryAndExpectedResult(String tableName, String schema, String queryFileName, String txtFileName) throws IOException, SQLException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.tableName(tableName);
        String query = DataUtil.readQueryFromFile(queryFileName);
        String txtFile = DataUtil.readStringFromFile(txtFileName);
        String actualResult = validateTable(query, queryBuilder.build(), schema, DataUtil.countFilesLines(fileName));
        Assert.assertEquals(txtFile, actualResult);
    }

}

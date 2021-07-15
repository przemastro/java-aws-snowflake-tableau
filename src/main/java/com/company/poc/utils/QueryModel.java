package com.company.poc.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryModel {

    private String parameterValue;
    private String parameterName;
    private String procedureName;
    private String tableName;
    private String filterName;
    private String filterValue;
    private String columnName;
}

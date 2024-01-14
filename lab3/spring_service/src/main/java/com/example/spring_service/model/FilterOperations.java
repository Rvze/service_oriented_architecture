package com.example.spring_service.model;

public enum FilterOperations {
    EQ("eq"),
    NEQ("neq"),
    LESS("less"),
    MORE("more");
    private String value;

    FilterOperations(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

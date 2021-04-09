package com.javatasks.model.enums;

import java.util.Arrays;
import java.util.List;

public enum MethodParameters {
    CREATE_USER(Arrays.asList("-fn", "-ln", "-un")),
    ADD_TASK(Arrays.asList("-un", "-tt", "-td"));

    private final List<String> parameters;


    MethodParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
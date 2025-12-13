package com.fer.apuw.lab.tweetie.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RoleType {

    ROLE_USER,
    ROLE_ADMIN;

    @JsonCreator
    public static RoleType fromValue(String value) {
        if (value == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown role type: " + value)
                );
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
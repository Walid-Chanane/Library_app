package com.library.backend.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account"); // in folder /resources/templates

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}

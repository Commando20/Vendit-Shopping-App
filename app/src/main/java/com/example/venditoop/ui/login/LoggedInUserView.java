package com.example.venditoop.ui.login;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private final String displayName;

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
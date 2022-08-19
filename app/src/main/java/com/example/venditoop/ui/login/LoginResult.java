package com.example.venditoop.ui.login;

import androidx.annotation.Nullable;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    /**
     *
     * @param error a login error
     */
    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    /**
     *
     * @param success a succesful login
     */
    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    /**
     *
     * @return a LoggedInUserView when login was successful
     */
    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    /**
     *
     * @return the error that was encountered
     */
    @Nullable
    Integer getError() {
        return error;
    }
}
package com.example.venditoop.data.model;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    private final String username;

    /**
     Constructs a LoggedInUser object that verifies that a user is logged in and uses
     the user's information
     @param username the username of the user
     @invariant username != null
     */
    public LoggedInUser(String username){ this.username = username; }

    /**
     Accessor function to grab the username of the user who is logged in
     @precondition username != null
     @postcondition the value of the user's username is now assigned to this accessor method
     @return the username of the user
     */
    public String getDisplayName() { return username; }
}
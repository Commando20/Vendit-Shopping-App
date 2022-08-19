package com.example.venditoop.data.model.user_models;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * This class will hold the user information when they are logged in
 */
public class User {
    private String name;
    private String email;

    /**
     Constructs a user object that holds user information when a new user is created through
     registration
     @param name the display name of the user
     @param email the email of the user
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     Accessor function to grab the email of the user
     @precondition email != null
     @postcondition the value of email is now assigned to this accessor method
     @return the email of a new user
     */
    public String getEmail() { return email; }

    /**
     Mutator function to set the email of a user when a new user is created
     @param email the email used by the user when creating their account
     */
    public void setEmail(String email) { this.email = email; }

    /**
     Accessor function to grab the name of a product
     @precondition name != null
     @postcondition the value of name is now assigned to this accessor method
     @return the name of a new user
     */
    public String getName() { return name; }

    /**
     Mutator function to set the name of a user
     @param name the name inputted by the user
     */
    public void setName(String name) { this.name = name; }
}

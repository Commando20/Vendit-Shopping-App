package com.example.venditoop.data.model.user_models;

import org.junit.Test;
import static org.junit.Assert.*;

import junit.framework.TestCase;

class UserTest {

    @Test
    void getEmail() {
        String email = "wdure2018@fau.edu";
        User u = new User("Wil", email);
        assertEquals(u.getEmail(), email );
    }

    @Test
    void setEmail() {
        String email = "wdure2018@fau.edu";
        User u = new User("Wil", "");
        u.setEmail(email);
        assertEquals(u.getEmail(), email );
    }

    @Test
    void getName() {
        String name = "Wil";
        User u = new User(name, "wdure2018@fau.edu");
        assertEquals(u.getName(), name );
    }

    @Test
    void setName() {
        String name = "Wil";
        User u = new User("", "wdure2018@fau.edu");
        u.setName(name);
        assertEquals(u.getName(), name );
    }
}
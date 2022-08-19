package com.example.venditoop.data.model.data_stores;

import junit.framework.TestCase;

import org.junit.Test;

public class ProductTest extends TestCase {

    String id = "a";
    String name = "drone";
    String description = "very cool drone";
    double price = 12.00;
    String img = null;
    long quantity = 4;

    Product t = new Product(id, name, description, price, img, quantity);
    @Test
    public void testGetId() {
        assertEquals(id, t.getId());
    }

    @Test
    public void testTestGetName() {
        assertEquals(name, t.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(description, t.getDescription());
    }

    @Test
    public void testGetImage() {
        assertEquals(img, t.getImage());
    }

    @Test
    public void testGetPrice() {
        assertEquals(price, t.getPrice());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(quantity, t.getQuantity());
    }

    @Test
    public void testSetQuantity() {
        long quantity = 10;
        t.setQuantity(quantity);
    }
}
package com.example.venditoop.data.model.data_stores;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public interface DataInterface {

    /**
     Method that adds a specific product in the inventory or the user's cart
     */
    void addProduct(Product p);

    /**
     Method that deletes a specific product in the inventory or the user's cart
     */
    void deleteProduct(Product p);

    /**
     Method that gets a specific product in the inventory or the user's cart
     */
    Product getProduct(String id);
}

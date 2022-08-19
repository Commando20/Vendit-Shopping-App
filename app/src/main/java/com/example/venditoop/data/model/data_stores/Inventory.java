package com.example.venditoop.data.model.data_stores;

import java.util.ArrayList;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * The current inventory. This class will hold the current inventory (fetched from Firebase) which
 * holds a list of products
 */
public class Inventory implements DataInterface {
    public ArrayList<Product> product_inventory;

    /**
     Method that adds a product to the inventory
     @param p the current product being added to the inventory
     */
    public void addProduct(Product p){
        product_inventory.add(p);
    }

    /**
     Method that deletes a product from the inventory
     @param p the current product being deleted from the inventory
     */
    public void deleteProduct(Product p){
        product_inventory.remove(p);
    }

    /**
     Method that loops through the inventory to find a specific product
     @param id the id of a product in the inventory
     @return the specific product found in the inventory
     */
    public Product getProduct(String id){
        for (Product product: product_inventory) {
            if (product.id.equals(id)) {
                System.out.println();
                return product;
            }
        }
        return null;
    }
}

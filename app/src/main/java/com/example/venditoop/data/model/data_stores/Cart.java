package com.example.venditoop.data.model.data_stores;

import java.util.ArrayList;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * The Cart object. This is class will hold the user's cart which is a list of products that
 * they would like to purchase
 */
public class Cart extends ProductStorage implements DataInterface {
    private final ArrayList<Product> products;

    /**
     Constructs a Cart object that will hold a list of Products that the user would like to
     purchase
     @param products an ArrayList of Products that the user would like to purchase
     @invariant products != null
     */
    public Cart(ArrayList<Product> products){ this.products = products; }

    /**
     Method that adds a selected product to the user's cart
     @param p the selected product the user would like to purchase
     */
    public void addProduct(Product p){ products.add(p); }

    /**
     Method that deletes a selected product from the user's cart
     @param p the selected product the user would like to remove from their cart
     */
    public void deleteProduct(Product p){ products.remove(p); }

    /**
     Method overridden from ProductStorage that raises the quantity of an item in the user's
     cart if they want to buy more of that item (cannot exceed amount in inventory)
     @param p the selected product the user would like to increase their quantity of
     */
    @Override
    public void raiseQuantity(Product p) { super.raiseQuantity(p); }

    /**
     Method overridden from ProductStorage that lowers the quantity of an item in the user's
     cart if they want to buy less of that item
     @param p the selected product the user would like to decrease their quantity of
     */
    @Override
    public void lowerQuantity(Product p) { super.lowerQuantity(p); }

    /**
     Accessor function that loops through a user's cart to find a specific product to perfrom various
     operations with
     @param id the id of a product in the user's cart (generated from Firebase)
     @precondition id != null
     @postcondition the value of an object Product is now assigned to this accessor method
     @return The specific product based on the id that was used
     */
    public Product getProduct(String id){
        for (Product product: products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    /**
     Accessor function that loops through a user's cart and computes the total price of the entire cart
     @precondition product list != null
     @postcondition the value of the total price is now assigned to this accessor method
     @return The total value of the entire cart
     */
    public double getTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }

    /**
     Method that checks if the cart contains any content within it
     @return A true or false value to determine if a user's cart is empty
     */
    public boolean checkCart(ArrayList<Product> cart){
        if (cart.isEmpty()) {
            return false;  
        } else {
            return true;
        }
    }
}

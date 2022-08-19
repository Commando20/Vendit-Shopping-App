package com.example.venditoop.data.model.data_stores;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * This class holds methods to update the quantity of a product in the user's cart
 */
public abstract class ProductStorage {

    /**
     Method overridden from ProductStorage that raises the quantity of an item in the user's
     cart if they want to buy more of that item
     @param p the selected product the user would like to increase their quantity of
     */
    public void raiseQuantity(Product p){
        p.setQuantity((int) (p.getQuantity() + 1));
    }

    /**
     Method overridden from ProductStorage that lowers the quantity of an item in the user's
     cart if they want to buy less of that item
     @param p the selected product the user would like to decrease their quantity of
     */
    public void lowerQuantity(Product p) {
        if(p.getQuantity() < 1){
            return;
        }
        p.setQuantity((int) (p.getQuantity() - 1));
    }
}

package com.example.venditoop.data.model.data_stores;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * The Product class. This class will hold various information pertaining to a specific product. This
 * product class will be in a list that will belong to either the inventory or the user's cart
 */
public class Product {
    protected String id;
    protected long quantity;
    private final String name, description, img;
    private final double price;

    /**
     Constructs a Product object that initializes a Product with various information
     @param id the id of a Product (generated from Firebase)
     @param name the name of the product
     @param description the description of the product
     @param price the price of the product
     @param img the image URL of the product (a local image is grabbed from the user's local device
     and then pushed to Firebase Storage. A link that is generated from Firebase storage is then
     fetched and is used when initializing a product
     @param quantity the available quantity of the product in the inventory
     @invariant id != null
     @invariant name != null
     @invariant description != null
     @invariant price != null
     @invariant img != null
     @invariant quantity != null
     */
    public Product(String id, String name, String description, double price, String img, long quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.img = img;
        this.quantity = quantity;
    }

    /**
     Accessor function to grab the id of a product
     @precondition id != null
     @postcondition the value of id is now assigned to this accessor method
     @return the id of a product
     */
    public String getId() { return id; }

    /**
     Accessor function to grab the name of a product
     @precondition name != null
     @postcondition the value of name is now assigned to this accessor method
     @return the name of a product
     */
    public String getName() { return name; }

    /**
     Accessor function to grab the description of a product
     @precondition description != null
     @postcondition the value of description is now assigned to this accessor method
     @return the description of a product
     */
    public String getDescription() { return description; }

    /**
     Accessor function to grab the image URL of a product
     @precondition image URL != null
     @postcondition the value of image URL is now assigned to this accessor method
     @return the image URL of a product
     */
    public String getImage() { return img; }

    /**
     Accessor function to grab the price of a product
     @precondition price != null
     @postcondition the value of price is now assigned to this accessor method
     @return the price of a product
     */
    public double getPrice() { return price; }

    /**
     Accessor function to grab the quantity of a product
     @precondition quantity != null
     @postcondition the value of quantity is now assigned to this accessor method
     @return the quantity of a product
     */
    public long getQuantity() { return quantity; }

    /**
     Mutator function to set the base salary of an employee
     @param quantity the available quantity of a product
     */
    public void setQuantity(long quantity) { this.quantity = quantity; }
}

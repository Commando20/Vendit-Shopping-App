package com.example.venditoop.ui.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venditoop.R;
import com.example.venditoop.data.model.data_stores.Inventory;
import com.example.venditoop.data.model.data_stores.Product;
import com.example.venditoop.databinding.ActivityShopBinding;
import com.example.venditoop.ui.ShopAdapter;
import com.example.venditoop.ui.cart.CartActivity;
import com.example.venditoop.ui.newItem.newItemActivity;
import com.example.venditoop.ui.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Activity that renders the shopping screen. The current inventory will be rendered on this page. 
 * All product information is visible on this screen and users can click on the buttons of each
 * card item to add that product to their cart
 */
public class ShopActivity extends AppCompatActivity implements View.OnClickListener {
    private ShopAdapter.RecylcerViewCLickListener listener;
    private ShopAdapter productsShopAdapter;
    private final ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Product> cartList = new ArrayList<>();

    /**
     Method that intializes the activity and renders all components and functionality on the screen
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get rid of pesky title bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        ActivityShopBinding binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //onClickListener for when the user wants to add an item to the inventory
        FloatingActionButton addItemToInventory = findViewById(R.id.addInventory);
        addItemToInventory.setOnClickListener(this);

        //onClickListener for when the user adds an item to their cart
        setOnClickListener();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("inventory").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    for (DocumentSnapshot document : task.getResult()) {
                        Product product = new Product(document.getId(), document.getString("name"),
                                document.getString("description"), document.getLong("price"),
                                document.getString("imageURL"), document.getLong("quantity"));

                        products.add(product);
                    }

                    // Initializing adapter class and passing arraylist to it.
                    productsShopAdapter = new ShopAdapter(this, products, listener);

                    // Setting a layout manager for our recycler view.
                    RecyclerView itemList = findViewById(R.id.itemsList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    itemList.setAdapter(productsShopAdapter);
                    itemList.setLayoutManager(linearLayoutManager);
                } catch (NullPointerException e) {
                    Log.e("ShopActivity", "An error has occurred", e);
                }
            } else {
                Log.e("ShopActivity", "Error getting documents.", task.getException());
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartList);
        editor.putString("Cart" , json);
        editor.apply();

        // Get bottom nav id so an item select listener can be set for switching between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_shop);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_cart: //If user goes to their cart page
                    Intent cart = new Intent(ShopActivity.this, CartActivity.class);
                    startActivity(cart);
                    break;
                case R.id.navigation_profile: //If user goes to their profile page
                    Intent profile = new Intent(ShopActivity.this, ProfileActivity.class);
                    startActivity(profile);
                    break;
            }
            return true;
        });
    }

    /**
     onClickListener method for when a button in a card item is clicked
     */
    private void setOnClickListener() {
        listener = (view, position) -> {
            //If the button to add an item to the user's cart is clicked, the listener is executed.
            //The position of that product will be fetched the product will be added to the user's
            //cart
            Product item = products.get(position);
            item.setQuantity(1);
            cartList.add(item);

            Toast.makeText(getApplicationContext(), item.getName() + " has been added to your cart",
                    Toast.LENGTH_LONG).show();
        };
    }

    /**
     onClickListener method for when the user clicks the green button to add an item
     to the inventory
     @param view the current view component that has a onClickListener
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(ShopActivity.this, newItemActivity.class);
        startActivity(intent);
    }
}

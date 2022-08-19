package com.example.venditoop.ui.cart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venditoop.R;
import com.example.venditoop.data.model.data_stores.Cart;
import com.example.venditoop.data.model.data_stores.Product;
import com.example.venditoop.databinding.ActivityCartBinding;
import com.example.venditoop.ui.CartAdapter;
import com.example.venditoop.ui.checkout.CheckoutActivity;
import com.example.venditoop.ui.profile.ProfileActivity;
import com.example.venditoop.ui.shop.ShopActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Activity that renders the cart screen. This screen will show the user's cart, which will be a list
 * of all the products that they would like to purchase. The user is able to update the quantity of
 * a product they would like to buy or simply remove an item entirely from their cart. The user
 * can either continue shopping for other products or proceed to checkout to purchase the products
 */
public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private CartAdapter.RecylcerViewCLickListener listener;
    private CartAdapter cartShopAdapter;
    private Cart cart;
    private final ArrayList<Product> cartList = ShopActivity.cartList;
    private RecyclerView itemList;
    DecimalFormat form = new DecimalFormat("0.00");
    private double total = 0;
    TextView totalText;

    /**
     Method that intializes the activity and renders all components and functionality on the screen
     @param savedInstanceState a saved instance of the current state so that if the activity needs
     to be recreated, no prior information is lost
     */
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get rid of pesky title bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        ActivityCartBinding binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        itemList = findViewById(R.id.itemsList);
        totalText = findViewById(R.id.total);

        Button checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(this);

        //Create a cart for the user's list of products
        cart = new Cart(cartList);
        total = cart.getTotal();

        if(!cart.checkCart(cartList)) {
            totalText.setText("Empty cart");
        } else {
            totalText.setText("Total Price: $" + form.format(total));
        }

        //onClickListener for when the user wants to remove an item from their cart
        setOnClickListener();

        // Initializing our adapter class and passing our arraylist to it.
        cartShopAdapter = new CartAdapter(this, cartList, listener);
        // Setting a layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemList.setLayoutManager(linearLayoutManager);
        itemList.setAdapter(cartShopAdapter);

        //Get bottom nav id so an item select listener can be set for switching between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_cart);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_shop: //If user goes to the shopping page
                    Intent shop = new Intent(CartActivity.this, ShopActivity.class);
                    startActivity(shop);
                    break;
                case R.id.navigation_profile: //If user goes to their profile page
                    Intent profile = new Intent(CartActivity.this, ProfileActivity.class);
                    startActivity(profile);
                    break;
            }
            return true;
        });
    }

    /**
     onClick method for the checkout button, where clicking it will create an intent to move to
     another screen (the checkout screen) and then initiate this intent
     @param view the current view component that has a onClickListener
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     onClickListener for the buttons on a card item in the recycler view. If any buttons in a
     specific card item is selected, then the designated peice of functionality will execute
     */
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    private void setOnClickListener() {
        listener = (view, position) -> {
            Product item = cartList.get(position);

            //Checks which button is being clicked on. If a specific is clicked on, then the switch
            //finds that case and once that case with the button's id is found, then that specific
            //piece of functionality executes
            switch (view.getId()) {
                case R.id.remove_item:
                    //If the user clicks the button to remove a specific item in their cart
                    cart.deleteProduct(item);
                    total -= item.getPrice();
                    if(!cart.checkCart(cartList)) {
                        totalText.setText("Empty cart");
                    } else {
                        totalText.setText("Total Price: $" + form.format(total));
                    }

                    Toast.makeText(getApplicationContext(), item.getName() + " has been removed from your cart",
                            Toast.LENGTH_LONG).show();
                    break;

                case R.id.increase:
                    //If the user clicks the button to increase the quantity of a product in their
                    //cart
                    cart.raiseQuantity(item);
                    total += item.getPrice();
                    totalText.setText("Total Price: $" + form.format(total));
                    break;

                case R.id.decrease:
                    //If the user clicks the button to decrease the quantity of a product in their
                    //cart

                    //If condition were set to 0, then the item will still appear in the cart.
                    //Once the user has the quantity at 0, the item should be removed from the cart
                    if (item.getQuantity() == 1) {
                        cart.deleteProduct(item);
                        Toast.makeText(getApplicationContext(), item.getName() + " has been removed from your cart",
                                Toast.LENGTH_LONG).show();
                    } else {
                        cart.lowerQuantity(item);
                    }
                    total -= item.getPrice();
                    totalText.setText("Total Price: $" + form.format(total));

                    if(!cart.checkCart(cartList)) {
                        totalText.setText("Empty cart");
                    }
                    break;
                default:
                    break;
            }

            //Instaniate cartAdapter to refresh the current recycler view
            cartShopAdapter = new CartAdapter(this, cartList, listener);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            itemList.setLayoutManager(linearLayoutManager);
            itemList.setAdapter(cartShopAdapter);
        };
    }
}

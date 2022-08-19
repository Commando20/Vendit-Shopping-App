package com.example.venditoop.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.venditoop.R;
import com.example.venditoop.data.LoginRepository;
import com.example.venditoop.databinding.ActivityProfileBinding;
import com.example.venditoop.ui.cart.CartActivity;
import com.example.venditoop.ui.login.LoginActivity;
import com.example.venditoop.ui.shop.ShopActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Activity that renders the profile screen. User information is present as well as the button 
 * to logout
 */
public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    /**
     Method that intializes the activity and renders all components and functionality on the screen
     @param savedInstanceState a saved instance of the current state so that if the activity needs
     to be recreated, no prior information is lost
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView email = binding.textView3;
        Button logout = binding.logout;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        email.setText(auth.getCurrentUser().getEmail());
        logout.setOnClickListener(view -> {
            auth.signOut();
            Toast.makeText(ProfileActivity.this, "Logging out",Toast.LENGTH_SHORT).show();
            Intent login = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        });

        //Get bottom nav id so an item select listener can be set for switching between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_shop: //If user goes to the shopping page
                    Intent shop = new Intent(ProfileActivity.this, ShopActivity.class);
                    startActivity(shop);
                    break;
                case R.id.navigation_cart: //If user goes to their cart page
                    Intent cart = new Intent(ProfileActivity.this, CartActivity.class);
                    startActivity(cart);
                    break;
            }
            return true;
        });
    }
}

package com.example.venditoop.ui.checkout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.venditoop.R;
import com.example.venditoop.data.model.data_stores.Product;
import com.example.venditoop.databinding.ActivityCheckoutBinding;
import com.example.venditoop.ui.cart.CartActivity;
import com.example.venditoop.ui.shop.ShopActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Activity that renders the checkout page. Here the user is able to input personal information
 * so they can purchase items they added to their cart. If the user is not yet satisfied, they
 * can return to the shopping screen or they can place their order
 */
public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {
    private final ArrayList<Product> cartList = ShopActivity.cartList;
    private EditText editTextAddress, editTextCity, editTextState, editTextZip, editTextCredit, editTextName;

    /**
     Method that intializes the activity and renders all components and functionality on the screen
     @param savedInstanceState a saved instance of the current state so that if the activity needs
     to be recreated, no prior information is lost
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get rid of pesky title bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        ActivityCheckoutBinding binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextAddress = findViewById(R.id.address);
        editTextCity = findViewById(R.id.city);
        editTextState = findViewById(R.id.state);
        editTextZip = findViewById(R.id.zipCode);
        editTextCredit = findViewById(R.id.cardNumber);
        editTextName = findViewById(R.id.nameOnCard);

        Button placeOrder = findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(this);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    /**
     onClick method for the place order and cancel button, if either of these buttons are clicked,
     then there respective case in the switch is executed
     @param view the current view component that has a onClickListener
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @android.annotation.SuppressLint({"NonConstantResourceId", "WrongViewCast"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.placeOrder:
                //If the user clicks place order, a call to Firebase is made to update the
                //quantities of the products the user bought and then the user's cart is cleared
                placeOrder();
                break;
            case R.id.cancel:
                //If the user clicks cancel, they will be redirected back to the cart screen
                Intent cancel = new Intent(CheckoutActivity.this, CartActivity.class);
                startActivity(cancel);
                break;
            default:
                break;
        }
    }

    /**
     Method makes a call to Firebase is made to update the quantities of the products the user
     bought
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void placeOrder() {
        Boolean hasError = checkEmpty();
        if(hasError)
            return;
        CollectionReference db = FirebaseFirestore.getInstance().collection("inventory");

        for (int i = 0; i < cartList.size(); i++) {
            Product item = cartList.get(i);

            db.document(item.getId()).update("quantity", FieldValue.increment(-item.getQuantity()))
            .addOnSuccessListener(aVoid -> {
                db.whereLessThanOrEqualTo("quantity", 0).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.document(document.getId()).delete();
                        }
                        Toast.makeText(getApplicationContext(), "Your order has been placed!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("CheckoutActivity", "Error getting documents: ", task.getException());
                        Toast.makeText(getApplicationContext(), "Your order could not be placed at this time. We are sorry!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            })
            .addOnFailureListener(e -> Log.e("CheckoutActivity", "An error has occurred", e)
            );

            Log.e("CheckoutActivity", "Inventory has been updated");
        }
         cartList.clear();

         Intent shop = new Intent(CheckoutActivity.this, ShopActivity.class);
         startActivity(shop);
    }

    /**
     Method that checks each individual input field in the checkout field and verifies that the
     correct input is inserted
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Boolean checkEmpty() {
        ArrayList<EditText> views = new ArrayList<>();
        HashMap<EditText, Boolean> err = new HashMap<>();
        Collections.addAll(views, editTextAddress, editTextName, editTextState, editTextZip, editTextCity, editTextCredit);
        AtomicReference<Boolean> formError = new AtomicReference<>(false);

        for(EditText view: views){
            if(view.getText().toString().isEmpty()){
                err.put(view, true);
            }else{
                err.put(view, false);
            }
        }

        err.forEach((view, hasError)->{
            if(hasError){
                view.setError("This field is required!");
                view.requestFocus();
                formError.set(true);
            }
        });

        return formError.get();
    }
}

package com.example.venditoop.ui.newItem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.venditoop.R;
import com.example.venditoop.databinding.ActivityNewItemBinding;
import com.example.venditoop.ui.shop.ShopActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Activity that renders the new item screen. Upon entering this page, the user can input information
 * for a new product to add to the inventory. Upon completing the information, the product is saved
 * to Firebase and the inventory is updated
 */
public class newItemActivity extends AppCompatActivity implements View.OnClickListener {
    private Uri path;
    private ImageView image;
    private EditText newItemName, newItemDescription, newItemPrice, newItemQuantity;
    protected FirebaseStorage storage;

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
        ActivityNewItemBinding binding = ActivityNewItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get instance of Firebase Storage Bucket
        storage = FirebaseStorage.getInstance();

        image = findViewById(R.id.newItemImage);
        newItemName = findViewById(R.id.newItemName);
        newItemDescription = findViewById(R.id.newItemDescription);
        newItemPrice = findViewById(R.id.newItemPrice);
        newItemQuantity = findViewById(R.id.newItemQuantity);

        Button uploadImage = findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(this);

        Button uploadItem = findViewById(R.id.uploadItem);
        uploadItem.setOnClickListener(this);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    /**
     onClickListener for the buttons in the new item screen. If a user clicks on any of these
     buttons then their respective case will a piece of functionality
     @param view the current view component that has a onClickListener
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadImage:
                //If the user clicks the upload image button, they will be sent to their local
                //device gallery and they can select an image to have uploaded
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;
                
            case R.id.uploadItem:
                //If the user clicks the upload button, the inputted information will be written
                //to firebase and saved in the inventory collection
                uploadToFirebase();
                finish();
                break;
                
            case R.id.cancel:
                //If the user clicks cancel, the new item activity screen closes and returns
                //back to the shopping screen
                finish();
                break;
            default:
                break;
        }
    }

    /**
     Method that handles when an image is selected from the browser and adds the following to
     the activity
     @param requestCode the code returned to identify which intent came back (when the user is able
     to select an image, then the request code returned is 1 - 1 stands for the the first returned
     activity with an onActivityResult)
     @param resultCode the code returned to determine if the user was able to complete their action
     successfully without any interrupts. If there is no interruption with viewing the gallery and
     selecting an image, then the result code will return RESULT_OK
     @param data the data that is returned when the user completes the activity (e.g. when the user
     finishes uploading an image, the data of the image such as its path is returned
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                path = data.getData();
                Log.e("newItemActivity", "Fail: " + path);
                Glide.with(this).load(path.toString()).into(image);
            }
        }
    }

    /**
     Method makes a call to Firebase Storage to store the uploaded image to Firebase. Once the
     uploaded image is stored in Firebase, we are able to retrieve a Firebase Storage link of that
     image to use for the image URL when rendering the image on the application
     */
    public void uploadToFirebase() {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        StorageReference ref = storageRef.child(path.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(path);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

            task.addOnSuccessListener(uri -> {
                String name = newItemName.getText().toString();
                String description = newItemDescription.getText().toString();
                final String downloadUrl = uri.toString();
                double price = Double.parseDouble(String.valueOf(newItemPrice.getText()));
                int quantity = Integer.parseInt(String.valueOf(newItemQuantity.getText()));
                long time = System.currentTimeMillis() / 1000;
                String currTime = Long.toString(time);

                // Add a new document with a generated id.
                Map<String, Object> data = new HashMap<>();
                data.put("name", name);
                data.put("description", description);
                data.put("price", price);
                data.put("quantity", quantity);
                data.put("imageURL", downloadUrl);
                data.put("timestamp", currTime);

                writeToFirestore(data);
            });
        }).addOnFailureListener(exception ->
            Log.e("newItemActivity", "Fail: " + exception)
        );
    }

    /**
     Method that makes a call to Firebase to write the new inputted product information from the
     user to Firebase.
     */
    public void writeToFirestore(Map<String, Object> data) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("inventory").add(data)
        .addOnSuccessListener(documentReference -> {
            Log.e("newItemActivity", "Success: " + documentReference);

            Toast.makeText(getApplicationContext(), "Your item has been added to the inventory",
                Toast.LENGTH_LONG).show();

            Intent shop = new Intent(newItemActivity.this, ShopActivity.class);
            startActivity(shop);
        }).addOnFailureListener(e -> Log.e("newItemActivity", "Error writing document", e) );
    }
}

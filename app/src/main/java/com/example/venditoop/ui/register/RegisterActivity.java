package com.example.venditoop.ui.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.venditoop.R;
import com.example.venditoop.data.model.user_models.User;
import com.example.venditoop.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Activity that renders the register screen. Here the user can input new credentials to create
 * an account for the application. Once the user is satisfied with their credentials, they can click
 * register to create a new account. They will be redirected back to the login page and they can
 * then sign in with those new credentials
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextUsername, editTextEmail, editTextPassword;
    private FirebaseAuth auth;

    /**
     Method that intializes the activity and renders all components and functionality on the screen
     @param savedInstanceState a saved instance of the current state so that if the activity needs
     to be recreated, no prior information is lost
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        TextView registerUser = findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
    }
    
    /**
     onClickListener for the buttons in the new item screen. If a user clicks on any of these
     buttons then their respective case will a piece of functionality
     @param v the current view component that has a onClickListener
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerUser:
                //If the user clicks the button register, a call to Firebase will be to create
                //a new user and it to the Firebase project
                registerUser();
                break;
            case R.id.cancel:
                //If the user clicks the cancel button, the process to register will cease and
                //the user will be redirected back to the login page
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
                break;
            default:
                break;
        }
    }

    /**
     Method that makes a call to Firebase to create a new user using the inputted credentials. If
     the call to Firebase is successful, a new account will be created and added to the Firebase
     project.
     */
    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

        if(username.isEmpty()){
            editTextUsername.setError("Username is required!");
            editTextUsername.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            return;
        }

        Toast.makeText(this,"Registering...", Toast.LENGTH_LONG).show();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){
                    User user = new User(username, email);
                    FirebaseDatabase.getInstance().getReference("users/"+ auth.getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(login);
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this,"Registration failed. Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

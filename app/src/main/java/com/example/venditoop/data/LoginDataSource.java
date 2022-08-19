package com.example.venditoop.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.venditoop.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    FirebaseAuth auth;

    /**
     * Method that calls Firebase to sign in a user using inputted username and password.
     * @param username the username passed in the login view (the username is usually an email)
     * @param password the password passed to the login view
     */
    public void login(String username, String password){
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    LoggedInUser user = new LoggedInUser(username);
                    Log.d("LOGIN","Success");
                }else{
                    Log.d("LOGIN","Failure");
                }
            }
        });
    }
    
    /**
     Method that will logout the user
     */
    public void logout() {
        auth.signOut();
    }
}
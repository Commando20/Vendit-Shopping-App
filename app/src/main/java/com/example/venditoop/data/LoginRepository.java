package com.example.venditoop.data;

import com.example.venditoop.data.model.LoggedInUser;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {
    private static volatile LoginRepository instance;
    private final LoginDataSource dataSource;

    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public void logout() {
        dataSource.logout();
    }
    
    /**
     *
     * @param username the String passed as the username in the login view
     * @param password the String passed as the password in the login view
     * @return the result
     */
    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result.Success result = null;
        dataSource.login(username, password);
        user = new LoggedInUser(username);
        if(user != null){
            result = new Result.Success<>(user);
        }

        if (result instanceof Result.Success) {
        }
        return result;
    }
}
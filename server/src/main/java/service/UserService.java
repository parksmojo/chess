package service;

import dataAccess.*;
import model.AuthData;

public class UserService {
    private final UserDAO userDAO = new MemoryUserDAO();

    public AuthData register(String username, String password, String email){
        if(userDAO.getUser(username) != null){
            return null;
        }

        userDAO.createUser(username,password,email);

        return userDAO.createAuth(username);
    }
}

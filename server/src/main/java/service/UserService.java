package service;

import dataAccess.*;
import model.*;
import java.util.Objects;

public class UserService {
    private final UserDAO userDAO = new MemoryUserDAO();

    public AuthData register(String username, String password, String email){
        if(userDAO.getUser(username) != null){
            return null;
        }

        userDAO.createUser(username,password,email);

        return userDAO.createAuth(username);
    }

    public AuthData login(String username, String password){
        UserData user = userDAO.getUser(username);
        if(user == null){
            return null;
        }

        if(Objects.equals(password, user.password())) {
            return userDAO.createAuth(username);
        } else {
            return null;
        }
    }

    public boolean logout(String authToken){
        return userDAO.delAuth(authToken);
    }
}

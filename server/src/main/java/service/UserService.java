package service;

import dataAccess.*;
import model.*;
import java.util.Objects;

public class UserService {
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

    public boolean validateAuth(String auth){
        return authDAO.validateAuth(auth) != null;
    }

    public AuthData register(String username, String password, String email){
        if(userDAO.getUser(username) != null){
            return null;
        }

        userDAO.createUser(username,password,email);

        return authDAO.createAuth(username);
    }

    public AuthData login(String username, String password){
        UserData user = userDAO.getUser(username);
        if(user == null){
            return null;
        }

        if(Objects.equals(password, user.password())) {
            return authDAO.createAuth(username);
        } else {
            return null;
        }
    }

    public boolean logout(String authToken){
        return authDAO.delAuth(authToken);
    }

    public void clear(){
        userDAO.clear();
        authDAO.clear();
    }
}

package service;

import dataAccess.*;
import model.*;
import java.util.Objects;

public class UserService {
    private final UserDAO userDAO = new DatabaseUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

    public boolean validateAuth(String auth){
        return authDAO.validateAuth(auth) != null;
    }

    public AuthData register(String username, String password, String email){
        try {
            if (userDAO.getUser(username) != null) {
                return null;
            }

            userDAO.createUser(username, password, email);

            return authDAO.createAuth(username);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public AuthData login(String username, String password){
        try {
            UserData user = userDAO.getUser(username);
            if (user == null) {
                return null;
            }

            if (Objects.equals(password, user.password())) {
                return authDAO.createAuth(username);
            } else {
                return null;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean logout(String authToken){
        try {
            return authDAO.delAuth(authToken);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void clear(){
        try {
            userDAO.clear();
            authDAO.clear();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

package service;

import dataAccess.*;
import model.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    private final UserDAO userDAO = new DatabaseUserDAO();
    private final AuthDAO authDAO = new DatabaseAuthDAO();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public boolean validateAuth(String auth){
        try {
            return authDAO.validateAuth(auth) != null;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public AuthData register(String username, String password, String email){
        try {
            if (userDAO.getUser(username) != null) {
                return null;
            }

            String hashedPassword = encoder.encode(password);
            userDAO.createUser(username, hashedPassword, email);

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


            if (encoder.matches(password, user.password())) {
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

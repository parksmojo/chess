package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO{
    private HashSet<UserData> users = new HashSet<>();
    private HashSet<AuthData> auths = new HashSet<>();

    public UserData getUser(String username) {
        for(UserData user : users){
            if(user.username().equals(username)){
                return user;
            }
        }
        return null;
    }

    public void createUser(String username, String password, String email) {
        users.add(new UserData(username, password, email));
    }

    public AuthData createAuth(String username) {
        AuthData auth = new AuthData(UUID.randomUUID().toString(), username);
        auths.add(auth);
        return auth;
    }

    public boolean validateAuth(String auth) {
        AuthData authToken = null;
        for(AuthData token : auths){
            if(token.authToken().equals(auth)){
                authToken = token;
                break;
            }
        }
        return authToken != null;
    }

    public boolean delAuth(String auth){
        if(validateAuth(auth)){
            for(AuthData token : auths){
                if(token.authToken().equals(auth)){
                    auths.remove(token);
                    return true;
                }
            }
        }
        return false;
    }
}

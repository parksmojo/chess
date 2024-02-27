package dataAccess;

import model.AuthData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private static HashSet<AuthData> authTokens = new HashSet<>();

    public AuthData createAuth(String username) {
        AuthData auth = new AuthData(UUID.randomUUID().toString(), username);
        authTokens.add(auth);
        return auth;
    }

    public boolean validateAuth(String auth) {
        AuthData authToken = null;
        for(AuthData token : authTokens){
            if(token.authToken().equals(auth)){
                authToken = token;
                break;
            }
        }
        return authToken != null;
    }

    public boolean delAuth(String auth){
        if(validateAuth(auth)){
            for(AuthData token : authTokens){
                if(token.authToken().equals(auth)){
                    authTokens.remove(token);
                    return true;
                }
            }
        }
        return false;
    }
}

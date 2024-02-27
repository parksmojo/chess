package dataAccess;

import model.AuthData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private static HashSet<AuthData> authTokens = new HashSet<>();

    @Override
    public AuthData createAuth(String username) {
        AuthData auth = new AuthData(UUID.randomUUID().toString(), username);
        authTokens.add(auth);
        return auth;
    }

    @Override
    public AuthData validateAuth(String auth) {
        AuthData authToken = null;
        for(AuthData token : authTokens){
            if(token.authToken().equals(auth)){
                authToken = token;
                break;
            }
        }
        return authToken;
    }



    @Override
    public boolean delAuth(String auth){
        AuthData authToken = validateAuth(auth);
        if(validateAuth(auth) != null){
            authTokens.remove(authToken);
            return true;
        }

        return false;
    }

    @Override
    public void clear() {
        authTokens.clear();
    }
}

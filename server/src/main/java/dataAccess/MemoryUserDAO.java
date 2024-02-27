package dataAccess;

import model.UserData;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{
     private static HashSet<UserData> users = new HashSet<>();

    @Override
    public UserData getUser(String username) {
        for(UserData user : users){
            if(user.username().equals(username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        users.add(new UserData(username, password, email));
    }

    @Override
    public void clear() {
        users.clear();
    }
}

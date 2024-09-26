package net.weesli.rfriend.database;

import net.weesli.rfriend.RFriend;
import net.weesli.rfriend.modal.User;
import net.weesli.rfriend.util.CacheManager;

public class Loader {

    public static void setupDatabase(){
        RFriend.getInstance().setDatabase(new SQLite());
    }

    public static void load(){
        for (User user : RFriend.getInstance().getDatabase().getAllUsers()){
            CacheManager.addUser(user.getUuid(), user);
        }
    }

    public static void save(){
        for (User user : CacheManager.getUsers().values()){
            if (RFriend.getInstance().getDatabase().hasUser(user.getUuid().toString())){
                RFriend.getInstance().getDatabase().updateUser(user);
            }else {
                RFriend.getInstance().getDatabase().insertUser(user);
            }
        }
    }
}

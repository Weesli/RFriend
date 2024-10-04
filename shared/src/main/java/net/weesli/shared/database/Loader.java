package net.weesli.shared.database;

import net.weesli.shared.RFriend;
import net.weesli.shared.model.User;
import net.weesli.shared.util.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Loader {



    public static void setupDatabase(){
        String driver = RFriend.getInstance().getConfig().getString("options.database.driver");
        if (driver.equalsIgnoreCase("MySQL")){
            RFriend.getInstance().setDatabase(new MySQL());
        }else if (driver.equalsIgnoreCase("SQLite")){
            RFriend.getInstance().setDatabase(new SQLite());
        }else {
            Bukkit.getServer().getPluginManager().disablePlugin(RFriend.getInstance());
            throw new IllegalArgumentException("Invalid database driver. Supported drivers: MySQL, SQLite");
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RFriend] Loading database " + driver);
    }

    public static void load(){
        for (User user : RFriend.getInstance().getDatabase().getAllUsers()){
            CacheManager.addUser(user.getUuid(), user);
        }
    }

    public static void save(){
        for (User user : CacheManager.getUsers().values()){
            if (RFriend.getInstance().getDatabase().hasUser(user.getUuid())){
                RFriend.getInstance().getDatabase().updateUser(user);
            }else {
                RFriend.getInstance().getDatabase().insertUser(user);
            }
        }
    }
}

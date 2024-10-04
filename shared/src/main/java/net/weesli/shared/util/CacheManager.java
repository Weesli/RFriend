package net.weesli.shared.util;

import lombok.Getter;
import lombok.Setter;
import net.weesli.shared.model.User;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Getter@Setter
public class CacheManager {

    @Getter private static Map<UUID, User> users = new HashMap<>();

    public static void addUser(UUID uuid, User user){
        users.put(uuid, user);
    }

    public static User getUser(UUID uuid){
        User user = users.getOrDefault(uuid,new User(
                uuid,
                Bukkit.getOfflinePlayer(uuid).getName(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        ));
        users.put(uuid,user);
        return user;
    }
}

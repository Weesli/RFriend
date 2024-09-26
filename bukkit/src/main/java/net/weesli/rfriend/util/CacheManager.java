package net.weesli.rfriend.util;

import lombok.Getter;
import lombok.Setter;
import net.weesli.rfriend.modal.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Getter@Setter
public class CacheManager {

    @Getter private static Map<UUID, User> users = new HashMap<>();

    public static void addUser(UUID uuid, User user){
        users.put(uuid, user);
    }

    public static void removeUser(UUID uuid){
        users.remove(uuid);
    }

    public static User getUser(UUID uuid){
        return users.get(uuid);
    }

    public static boolean hasUser(UUID uuid){
        return users.containsKey(uuid);
    }


}

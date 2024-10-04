package net.weesli.shared.api;

import net.weesli.shared.enums.FriendSetting;
import net.weesli.shared.model.User;
import net.weesli.shared.util.CacheManager;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class RFriendAPI {

    public static User getUser(UUID uuid){
        return CacheManager.getUser(uuid);
    }

    public static boolean isFriend(Player player, UUID target){
        User user = getUser(player.getUniqueId());
        return user.isFriend(target);
    }

    public static boolean isBlocked(Player player, UUID target){
        User user = getUser(player.getUniqueId());
        return user.isBlocked(target);
    }

    public static boolean isSetting(Player player, FriendSetting setting){
        User user = getUser(player.getUniqueId());
        return user.isSetting(setting);
    }

    public static Collection<User> getAllUsers(){
        return CacheManager.getUsers().values();
    }

}

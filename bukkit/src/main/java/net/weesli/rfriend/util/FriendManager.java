package net.weesli.rfriend.util;

import lombok.Getter;
import lombok.Setter;
import net.weesli.rfriend.modal.User;
import net.weesli.rfriend.modal.UserInvite;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter@Setter
public class FriendManager {

    private static Map<UUID, UserInvite> invites = new HashMap<>();

    private void invite(Player player, UUID target){
        UserInvite invite = invites.getOrDefault(target, new UserInvite(target,new ArrayList<>()));
        if (invite.getSenders().contains(player.getUniqueId())){
            // message already sent
            return;
        }
        invite.getSenders().add(player.getUniqueId());
        invites.put(target, invite);
        // message
    }

    private void accept(Player player, UUID target){
        UserInvite invite = invites.get(target);
        if (invite == null ||!invite.getSenders().contains(player.getUniqueId())){
            // no such invite or not sent by the player
            return;
        }
        invite.getSenders().remove(player.getUniqueId());
        CacheManager.getUser(target).addFriend(player.getUniqueId());
        CacheManager.getUser(player.getUniqueId()).addFriend(player.getUniqueId());

        // message
    }

    private void decline(Player player, UUID target){
        UserInvite invite = invites.get(target);
        if (invite == null ||!invite.getSenders().contains(player.getUniqueId())){
            // no such invite or not sent by the player
            return;
        }
        invite.getSenders().remove(player.getUniqueId());
        // message
    }

    private void block(Player player, UUID target){
        User user = CacheManager.getUser(player.getUniqueId());
        user.addBlock(target);
        // message
    }

    private void unblock(Player player, UUID target){
        User user = CacheManager.getUser(player.getUniqueId());
        user.removeBlock(target);
        // message
    }

    private void removeFriend(Player player, UUID target){
        User user = CacheManager.getUser(player.getUniqueId());
        user.removeFriend(target);
        // message
    }

    private void sendMessage(Player player, String message){
        // TODO: Send a private message to the player

    }


}

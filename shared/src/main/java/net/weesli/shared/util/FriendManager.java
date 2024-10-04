package net.weesli.shared.util;

import lombok.Getter;
import lombok.Setter;
import net.weesli.shared.RFriend;
import net.weesli.shared.enums.FriendSetting;
import net.weesli.shared.enums.Lang;
import net.weesli.shared.model.User;
import net.weesli.shared.model.UserInvite;
import net.weesli.rozsLib.color.ColorBuilder;
import net.weesli.shared.util.proxy.ChannelSender;
import net.weesli.shared.util.proxy.ProxyManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter@Setter
public class FriendManager {

    @Getter private static Map<UUID, UserInvite> invites = new HashMap<>();

    public static void invite(Player player, UUID target){
        UserInvite invite = invites.getOrDefault(target, new UserInvite(target,new ArrayList<>()));
        User user = CacheManager.getUser(player.getUniqueId());
        if (user.isFriend(target)){
            player.sendMessage(Lang.already_friend.getMessage());
            return;
        }
        User targetUser = CacheManager.getUser(target);
        if (targetUser == null || targetUser.isBlocked(player.getUniqueId()) || targetUser.isSetting(FriendSetting.DISABLE_INVITE)){
            player.sendMessage(Lang.blocked.getMessage());
            return;
        }
        if (invite.getSenders().contains(player.getUniqueId())){
            player.sendMessage(Lang.invite_already_sent.getMessage());
            return;
        }
        invite.getSenders().add(player.getUniqueId());
        invites.put(target, invite);
        player.sendMessage(Lang.invite_sent.getMessage().replaceAll("%player%", targetUser.getUsername()));
        if (RFriend.getInstance().isCrossServer()){
            ChannelSender.send_friend_request(player,targetUser.getUsername());
            ChannelSender.send_user_invites(player, invite);
        }
    }

    public static void accept(Player player, UUID target){
        UserInvite invite = invites.get(player.getUniqueId());
        if (invite == null || !invite.getSenders().contains(target)){
            player.sendMessage(Lang.no_invite.getMessage());
            return;
        }
        invite.getSenders().remove(player.getUniqueId());

        User target_user = CacheManager.getUser(target);
        User user = CacheManager.getUser(player.getUniqueId());

        target_user.addFriend(player.getUniqueId());
        user.addFriend(target);

        player.sendMessage(Lang.accept_invite.getMessage());
        if (RFriend.getInstance().isCrossServer()){
            ChannelSender.publish_user(player, target_user);
            ChannelSender.publish_user(player,user);
            ChannelSender.send_user_invites(player,invite);
        }
    }

    public static void decline(Player player, UUID target){
        UserInvite invite = invites.get(target);
        if (invite == null ||!invite.getSenders().contains(player.getUniqueId())){
            player.sendMessage(Lang.no_invite.getMessage());
            return;
        }
        invite.getSenders().remove(player.getUniqueId());
        player.sendMessage(Lang.decline_invite.getMessage());
        if (RFriend.getInstance().isCrossServer()){
            ChannelSender.send_user_invites(player,invite);
        }
    }

    public static void block(Player player, UUID target){
        User user = CacheManager.getUser(player.getUniqueId());
        if (user.isBlocked(target)){
            player.sendMessage(Lang.already_blocked.getMessage());
            return;
        }
        user.addBlock(target);
        player.sendMessage(Lang.added_block_player.getMessage());
        if (RFriend.getInstance().isCrossServer()){
            ChannelSender.publish_user(player,user);
        }
    }

    public static void unblock(Player player, UUID target){
        User user = CacheManager.getUser(player.getUniqueId());
        if (!user.isBlocked(target)){
            player.sendMessage(Lang.not_blocked.getMessage());
            return;
        }
        user.removeBlock(target);
        player.sendMessage(Lang.removed_block_player.getMessage());
        if (RFriend.getInstance().isCrossServer()){
            ChannelSender.publish_user(player,user);
        }
    }

    public static void removeFriend(Player player, UUID target){
        User user = CacheManager.getUser(player.getUniqueId());
        if (!user.isFriend(target)){
            player.sendMessage(Lang.not_friend.getMessage());
            return;
        }
        User target_user = CacheManager.getUser(target);
        user.removeFriend(target);
        target_user.removeFriend(player.getUniqueId());
        player.sendMessage(Lang.removed_friend.getMessage());
        if (RFriend.getInstance().isCrossServer()){
            ChannelSender.publish_user(player,user);
            ChannelSender.publish_user(player,target_user);
        }
    }

    public static void sendMessage(Player sender, UUID target, String message){
        User target_user = CacheManager.getUser(target);
        if (target_user == null){
            sender.sendMessage(Lang.player_not_found.getMessage());
            return;
        }
        if (!target_user.isFriend(sender.getUniqueId())){
            sender.sendMessage(Lang.not_friend.getMessage());
            return;
        }
        if (target_user.isSetting(FriendSetting.DISABLE_MESSAGES)){
            sender.sendMessage(Lang.setting_message_disabled.getMessage());
            return;
        }
        if (RFriend.getInstance().isCrossServer()){
            ChannelSender.sendMessage(sender,target,ColorBuilder.convertColors(RFriend.getInstance().getConfig().getString("options.friend-message-format").replaceAll("%sender%", sender.getName()).replaceAll("%message%", message)));
        }else {
            Player player = Bukkit.getPlayer(target);
            if (player!= null){
                player.sendMessage(ColorBuilder.convertColors(RFriend.getInstance().getConfig().getString("options.friend-message-format").replaceAll("%sender%", sender.getName()).replaceAll("%message%", message)));
            }else {
                player.sendMessage(Lang.player_not_found.getMessage());
            }
        }
    }

    public static String getServer(UUID uuid){
        return ProxyManager.getServers().getOrDefault(Bukkit.getOfflinePlayer(uuid).getName(), "");
    }

    public static String getStatus(boolean status){
        return (status ? ColorBuilder.convertColors(RFriend.getInstance().getConfig().getString("options.status.online")): ColorBuilder.convertColors(RFriend.getInstance().getConfig().getString("options.status.offline")));
    }

    public static boolean isOnline(String name){
        if (!RFriend.getInstance().isCrossServer()){
            return Bukkit.getPlayer(name)!= null;
        }else {
            return ProxyManager.getServers().containsKey(name);
        }
    }

    public static String getSettingStatus(boolean setting) {
        return (setting ? ColorBuilder.convertColors(RFriend.getInstance().getConfig().getString("options.settings-status.enabled")): ColorBuilder.convertColors(RFriend.getInstance().getConfig().getString("options.settings-status.disabled")));
    }
}

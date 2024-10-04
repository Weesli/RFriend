package net.weesli.shared.command;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Join;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import net.weesli.shared.RFriend;
import net.weesli.shared.enums.Lang;
import net.weesli.shared.model.User;
import net.weesli.shared.util.CacheManager;
import net.weesli.shared.util.FriendManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Command("friend")
public class FriendCommand extends BaseCommand {


    @Default
    public void execute(Player player){
        User user = CacheManager.getUser(player.getUniqueId());
        RFriend.getInstance().getUiManager().openInventory(player, user, "main");
    }

    @SubCommand("invite")
    public void invite(Player player, String name){
        if (name.equals(player.getName()))return;
        boolean isValid = Bukkit.getOfflinePlayer(name).hasPlayedBefore();
        if(!isValid){
            player.sendMessage(Lang.player_not_found.getMessage());
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        FriendManager.invite(player,offlinePlayer.getUniqueId());
    }

    @SubCommand("accept")
    public void accept(Player player, String name){
        if (name.equals(player.getName()))return;
        boolean isValid = Bukkit.getOfflinePlayer(name).hasPlayedBefore();
        if(!isValid){
            player.sendMessage(Lang.player_not_found.getMessage());
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        FriendManager.accept(player, offlinePlayer.getUniqueId());
    }

    @SubCommand("decline")
    public void decline(Player player, String name){
        if (name.equals(player.getName()))return;
        boolean isValid = Bukkit.getOfflinePlayer(name).hasPlayedBefore();
        if(!isValid){
            player.sendMessage(Lang.player_not_found.getMessage());
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        FriendManager.decline(player, offlinePlayer.getUniqueId());
    }

    @SubCommand("message")
    public void message(Player player, String name, @Join(" ") String message){
        if (name.equals(player.getName()))return;
        boolean isValid = Bukkit.getOfflinePlayer(name).hasPlayedBefore();
        if(!isValid){
            player.sendMessage(Lang.player_not_found.getMessage());
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        FriendManager.sendMessage(player, offlinePlayer.getUniqueId(), message);
    }
    @SubCommand("block")
    public void block(Player player, String name){
        if (name.equals(player.getName()))return;
        boolean isValid = Bukkit.getOfflinePlayer(name).hasPlayedBefore();
        if(!isValid){
            player.sendMessage(Lang.player_not_found.getMessage());
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        FriendManager.block(player, offlinePlayer.getUniqueId());
    }

    @SubCommand("unblock")
    public void unblock(Player player, String name){
        if (name.equals(player.getName())) return;
        boolean isValid = Bukkit.getOfflinePlayer(name).hasPlayedBefore();
        if(!isValid){
            player.sendMessage(Lang.player_not_found.getMessage());
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        FriendManager.unblock(player, offlinePlayer.getUniqueId());
    }
}

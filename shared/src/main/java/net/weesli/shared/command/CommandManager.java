package net.weesli.shared.command;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import lombok.Getter;
import lombok.Setter;
import net.weesli.shared.RFriend;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
@Getter@Setter
public class CommandManager {

    private BukkitCommandManager<CommandSender> manager;

    public CommandManager() {
        manager = BukkitCommandManager.create(RFriend.getInstance());
        registerCommands();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RFriend] Registered all commands");
    }

    private void registerCommands() {
        manager.registerCommand(new FriendCommand());
    }
}

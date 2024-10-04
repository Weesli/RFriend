package net.weesli.shared.ui;

import net.weesli.shared.RFriend;
import net.weesli.shared.model.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface FriendInventory {

    void openInventory(Player player, User user, FileConfiguration config);

    default ItemStack getItemstack(String path){
        return RFriend.getInstance().getMenus_builder().getItemStack(path);
    }
}

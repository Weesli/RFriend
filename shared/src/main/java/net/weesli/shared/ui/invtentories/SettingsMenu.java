package net.weesli.shared.ui.invtentories;

import net.weesli.shared.RFriend;
import net.weesli.shared.enums.FriendSetting;
import net.weesli.shared.model.User;
import net.weesli.shared.ui.FriendInventory;
import net.weesli.shared.util.FriendManager;
import net.weesli.rozsLib.inventory.lasest.InventoryBuilder;
import net.weesli.shared.util.proxy.ChannelSender;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsMenu implements FriendInventory {

    int[] glass_slots = {0, 1, 2, 3, 4, 5, 6, 7,8,9,17,18,19,20,21,22,23,24,25,26};

    @Override
    public void openInventory(Player player, User user, FileConfiguration config) {
        InventoryBuilder builder = new InventoryBuilder().title(config.getString("settings-menu.title")).size(config.getInt("settings-menu.size"));
        if (config.getBoolean("main-menu.glass-items")){
            for (int slot : glass_slots){
                builder.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }
        addItem(builder, player, user, config, "invite", FriendSetting.DISABLE_INVITE);
        addItem(builder, player, user, config, "message", FriendSetting.DISABLE_MESSAGES);
        builder.openInventory(player);
    }

    private void addItem(InventoryBuilder builder, Player player, User user, FileConfiguration config, String s, FriendSetting friendSetting) {
        ItemStack itemStack = getItemstack("settings-menu.children."  + s);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(meta.getLore().stream().map(line -> line.replaceAll("%status%", FriendManager.getSettingStatus(user.isSetting(friendSetting)))).toList());
        itemStack.setItemMeta(meta);
        builder.setItem(config.getInt("settings-menu.children." + s + ".slot"), itemStack, event -> {
            if (user.isSetting(friendSetting)){
                user.removeSetting(friendSetting);
            }else {
                user.addSetting(friendSetting);
            }
            if (RFriend.getInstance().isCrossServer()){
                ChannelSender.publish_user(player,user);
            }
            openInventory(player,user,config);
        });
    }
}

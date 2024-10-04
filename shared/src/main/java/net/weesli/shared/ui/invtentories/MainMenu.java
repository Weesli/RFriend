package net.weesli.shared.ui.invtentories;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import net.weesli.shared.RFriend;
import net.weesli.shared.model.User;
import net.weesli.shared.ui.FriendInventory;
import net.weesli.rozsLib.inventory.lasest.InventoryBuilder;
import net.weesli.shared.util.proxy.ChannelSender;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;

public class MainMenu implements FriendInventory {

    int[] glass_slots = {0, 1, 2, 3, 5, 6, 7,8,9,17,18,19,20,21,22,23,24,25,26};

    @Override
    public void openInventory(Player player, User user, FileConfiguration config) {
        InventoryBuilder builder = new InventoryBuilder().title(config.getString("main-menu.title")).size(config.getInt("main-menu.size"));
        if (config.getBoolean("main-menu.glass-items")){
            for (int slot : glass_slots){
                builder.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        builder.setItem(config.getInt("main-menu.children.profile.slot"), getProfileItem(player,user), event -> {
            openSign(player,user);
        });

        builder.setItem(config.getInt("main-menu.children.friends.slot"), getItemstack("main-menu.children.friends"), event -> {
            RFriend.getInstance().getUiManager().openInventory(player,user,"friends");
        });

        builder.setItem(config.getInt("main-menu.children.settings.slot"), getItemstack("main-menu.children.settings"), event -> {
            RFriend.getInstance().getUiManager().openInventory(player,user,"settings");
        });

        builder.setItem(config.getInt("main-menu.children.blocks.slot"), getItemstack("main-menu.children.blocks"), event -> {
            RFriend.getInstance().getUiManager().openInventory(player,user,"blocks");
        });

        builder.openInventory(player);
    }

    private ItemStack getProfileItem(Player player, User user){
        ItemStack itemStack = getItemstack("main-menu.children.profile");
        itemStack.setType(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwnerProfile(player.getPlayerProfile());
        meta.setLore(meta.getLore().stream().map(line -> line.replaceAll("%player%", player.getName()).replaceAll("%count%", String.valueOf(user.getFriends().size())).replaceAll("%status%", user.getStatus())).toList());
        itemStack.setItemMeta(meta);
        return itemStack;
    }


    private void openSign(Player player,User user){
        SignGUI signGUI = SignGUI.builder()
                .setLines("","▲▲▲▲▲▲▲▲","","")
                .setType(Material.DARK_OAK_SIGN)
                .setColor(DyeColor.WHITE)
                .setHandler((p,result) -> {
                    String status = result.getLine(0);
                    user.setStatus(status);
                    if (RFriend.getInstance().isCrossServer()){
                        ChannelSender.publish_user(player,user);
                    }
                    return Collections.singletonList(SignGUIAction.run(() -> RFriend.getInstance().getUiManager().openInventory(player, user, "main")));
                })
                .build();
        signGUI.open(player);
    }
}

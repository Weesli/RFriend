package net.weesli.shared.ui.invtentories;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import lombok.Getter;
import lombok.Setter;
import net.weesli.shared.RFriend;
import net.weesli.shared.enums.Lang;
import net.weesli.shared.model.User;
import net.weesli.shared.ui.FriendInventory;
import net.weesli.shared.util.FriendManager;
import net.weesli.rozsLib.inventory.lasest.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BlocksMenu implements FriendInventory {

    int[] glass_slots = {0, 1, 2, 3, 4, 5, 6, 7,8,9,17,18,19,20,21,23,24,25,26};
    int[] slots = {10,11,12,13,14,15,16};
    int blocksPerPage = slots.length;

    @Setter
    @Getter
    int page = 0;

    @Override
    public void openInventory(Player player, User user, FileConfiguration config) {
        InventoryBuilder builder = new InventoryBuilder().title(config.getString("blocks-menu.title")).size(config.getInt("blocks-menu.size"));

        if (config.getBoolean("main-menu.glass-items")) {
            for (int slot : glass_slots) {
                builder.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        builder.setItem(config.getInt("blocks-menu.children.add-block.slot"), getItemstack("blocks-menu.children.add-block"), event -> {
            openSign(player,user);
        });

        ItemStack itemStack = getItemstack("blocks-menu.children.item-settings");
        List<UUID> blocks = user.getBlocks();

        int startIndex = page * blocksPerPage;
        int endIndex = Math.min(startIndex + blocksPerPage, blocks.size());

        int i = 0;
        for (int index = startIndex; index < endIndex; index++) {
            UUID uuid = blocks.get(index);
            OfflinePlayer friend = Bukkit.getOfflinePlayer(uuid);

            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwnerProfile(friend.getPlayerProfile());
            skullMeta.setDisplayName(skullMeta.getDisplayName().replaceAll("%player%", friend.getName()));
            itemStack.setItemMeta(skullMeta);

            builder.setItem(slots[i], itemStack, event -> {
                FriendManager.unblock(player,uuid);
            });
            i++;
        }

        if (page > 0) {
            builder.setItem(config.getInt("blocks-menu.children.previous-page.slot"), getItemstack("blocks-menu.children.previous-page"), event -> {
                setPage(page - 1);
                openInventory(player, user, config);
            });
        }

        if (endIndex < blocks.size()) {
            builder.setItem(config.getInt("blocks-menu.children.next-page.slot"), getItemstack("blocks-menu.children.next-page"), event -> {
                setPage(page + 1);
                openInventory(player, user, config);
            });
        }

        builder.openInventory(player);
    }

    private void openSign(Player player,User user){
        SignGUI signGUI = SignGUI.builder()
                .setLines("","▲▲▲▲▲▲▲▲","","")
                .setType(Material.DARK_OAK_SIGN)
                .setColor(DyeColor.WHITE)
                .setHandler((p,result) -> {
                    String name = result.getLine(0);
                    if (name.isEmpty() && name.equals(player.getName()) && FriendManager.isOnline(name)){
                        player.sendMessage(Lang.player_not_found.getMessage());
                        return Collections.singletonList(SignGUIAction.run(() -> RFriend.getInstance().getUiManager().openInventory(player, user, "blocks")));
                    }else {
                        FriendManager.block(player,Bukkit.getOfflinePlayer(name).getUniqueId());
                        return Collections.singletonList(SignGUIAction.run(() -> RFriend.getInstance().getUiManager().openInventory(player, user, "blocks")));
                    }
                })
                .build();
        signGUI.open(player);
    }
}

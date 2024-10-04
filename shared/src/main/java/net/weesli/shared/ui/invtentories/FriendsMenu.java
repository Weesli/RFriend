package net.weesli.shared.ui.invtentories;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import lombok.Getter;
import lombok.Setter;
import net.weesli.shared.RFriend;
import net.weesli.shared.enums.Lang;
import net.weesli.shared.model.User;
import net.weesli.shared.ui.FriendInventory;
import net.weesli.shared.util.CacheManager;
import net.weesli.shared.util.FriendManager;
import net.weesli.rozsLib.inventory.lasest.InventoryBuilder;
import net.weesli.shared.util.proxy.ChannelSender;
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

public class FriendsMenu implements FriendInventory {

    int[] glass_slots = {0, 1, 2, 3, 4, 5, 6, 7,8,9,17,18,19,20,21,23,24,25,26};
    int[] slots = {10,11,12,13,14,15,16};
    int friendsPerPage = slots.length;

    @Setter@Getter
    int page = 0;

    @Override
    public void openInventory(Player player, User user, FileConfiguration config) {
        InventoryBuilder builder = new InventoryBuilder().title(config.getString("friends-menu.title")).size(config.getInt("friends-menu.size"));

        if (config.getBoolean("friends-menu.glass-items")) {
            for (int slot : glass_slots) {
                builder.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        builder.setItem(config.getInt("friends-menu.children.add-friend.slot"), getItemstack("friends-menu.children.add-friend"), event -> {
            openSign(player,user);
        });

        ItemStack itemStack = getItemstack("friends-menu.children.item-settings");
        List<UUID> friends = user.getFriends();

        int startIndex = page * friendsPerPage;
        int endIndex = Math.min(startIndex + friendsPerPage, friends.size());

        int i = 0;
        for (int index = startIndex; index < endIndex; index++) {
            UUID uuid = friends.get(index);
            OfflinePlayer friend = Bukkit.getOfflinePlayer(uuid);

            // get servers of friends if cross-server is enabled
            if (RFriend.getInstance().isCrossServer()){
                ChannelSender.sendRequestServer(player,friend.getName());
            }

            User user_meta = CacheManager.getUser(uuid);

            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwnerProfile(friend.getPlayerProfile());
            skullMeta.setDisplayName(skullMeta.getDisplayName().replaceAll("%player%", friend.getName()));
            skullMeta.setLore(skullMeta.getLore().stream().map(line ->
                    line.replaceAll("%status%", FriendManager.getStatus(FriendManager.isOnline(friend.getName())))
                            .replaceAll("%message%", (user_meta != null ? user_meta.getStatus() : ""))
                            .replaceAll("%server%", FriendManager.getServer(uuid))
            ).toList());
            itemStack.setItemMeta(skullMeta);

            builder.setItem(slots[i], itemStack, event -> {
                FriendManager.removeFriend(player,uuid);
            });
            i++;
        }

        if (page > 0) {
            builder.setItem(config.getInt("friends-menu.children.previous-page.slot"), getItemstack("friends-menu.children.previous-page"), event -> {
                setPage(page - 1);
                openInventory(player, user, config);
            });
        }

        if (endIndex < friends.size()) {
            builder.setItem(config.getInt("friends-menu.children.next-page.slot"), getItemstack("friends-menu.children.next-page"), event -> {
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
                        return Collections.singletonList(SignGUIAction.run(() -> RFriend.getInstance().getUiManager().openInventory(player, user, "friends")));
                    }else {
                        FriendManager.invite(player, Bukkit.getOfflinePlayer(name).getUniqueId());
                        return Collections.singletonList(SignGUIAction.run(() -> RFriend.getInstance().getUiManager().openInventory(player, user, "friends")));
                    }
                })
                .build();
        signGUI.open(player);
    }

}

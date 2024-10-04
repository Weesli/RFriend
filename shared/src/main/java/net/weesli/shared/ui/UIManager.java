package net.weesli.shared.ui;

import lombok.Getter;
import lombok.Setter;
import net.weesli.shared.RFriend;
import net.weesli.shared.model.User;
import net.weesli.shared.ui.invtentories.BlocksMenu;
import net.weesli.shared.ui.invtentories.FriendsMenu;
import net.weesli.shared.ui.invtentories.MainMenu;
import net.weesli.shared.ui.invtentories.SettingsMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class UIManager {

    private Map<String, FriendInventory> map = new HashMap<>();

    public UIManager() {
        map.put("main", new MainMenu());
        map.put("blocks", new BlocksMenu());
        map.put("friends", new FriendsMenu());
        map.put("settings", new SettingsMenu());
    }

    public void openInventory(Player player, User user, String value){
        map.get(value).openInventory(player,user, RFriend.getInstance().getMenus());
    }
}

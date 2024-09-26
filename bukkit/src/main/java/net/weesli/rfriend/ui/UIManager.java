package net.weesli.rfriend.ui;

import lombok.Getter;
import lombok.Setter;
import net.weesli.rfriend.modal.User;
import net.weesli.rfriend.ui.invtentories.MainMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class UIManager {

    private MainMenu mainMenu;
    private Map<String, FriendInventory> map = new HashMap<>();

    public UIManager() {
        map.put("main", new MainMenu());
    }

    public void openInventory(Player player, User user, String value){
        map.get("value").openInventory(player,user);
    }
}

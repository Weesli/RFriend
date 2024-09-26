package net.weesli.rfriend;

import lombok.Getter;
import lombok.Setter;
import net.weesli.rfriend.database.Database;
import net.weesli.rfriend.database.Loader;
import net.weesli.rfriend.ui.UIManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter@Setter
public final class RFriend extends JavaPlugin {

    @Getter private static RFriend instance;

    private Database database;
    private UIManager uiManager;

    @Override
    public void onEnable() {
        instance = this;
        Loader.setupDatabase();
        Loader.load();
        uiManager = new UIManager();
    }

    @Override
    public void onDisable() {
        Loader.save();
    }
}

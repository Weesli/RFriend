package net.weesli.shared;

import lombok.Getter;
import lombok.Setter;
import net.weesli.shared.command.CommandManager;
import net.weesli.shared.database.Database;
import net.weesli.shared.database.Loader;
import net.weesli.shared.ui.UIManager;
import net.weesli.rozsLib.configuration.YamlFileBuilder;
import net.weesli.shared.util.proxy.ProxyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter@Setter
public final class RFriend extends JavaPlugin {

    @Getter private static RFriend instance;
    private Database database;
    private UIManager uiManager;
    private CommandManager commandManager;
    private ProxyManager proxyManager;

    // files

    private YamlFileBuilder menus_builder = new YamlFileBuilder(this,"menus").setResource(true);
    private YamlFileBuilder lang_builder = new YamlFileBuilder(this,"lang").setResource(true);

    private FileConfiguration menus;
    private FileConfiguration lang;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RFriend] ------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RFriend] RFriend v" + getDescription().getVersion() + " Starting by Weesli!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RFriend] ------------------------------------------------");
        uiManager = new UIManager();
        commandManager = new CommandManager();
        proxyManager = new ProxyManager(this);
        loadFiles();
        Loader.setupDatabase();
        Loader.load();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RFriend] Enabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RFriend] ------------------------------------------------");
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Loader::save, 12000, 12000);
    }


    @Override
    public void onDisable() {
        Loader.save();
        if (isCrossServer()){
            getProxyManager().getRedisManager().closeConnections();
        }
    }
    private void loadFiles() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        menus = menus_builder.load();
        lang = lang_builder.load();
        menus.options().copyDefaults(true);
        lang.options().copyDefaults(true);
        menus_builder.save();
        lang_builder.save();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[RFriend] Configs loaded!");
    }


    public boolean isCrossServer(){
        return getConfig().getBoolean("options.cross-server.enabled");
    }
}

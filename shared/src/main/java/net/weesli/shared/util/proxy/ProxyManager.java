package net.weesli.shared.util.proxy;

import lombok.Getter;
import lombok.Setter;
import net.weesli.rozsLib.color.ColorBuilder;
import net.weesli.shared.RFriend;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class ProxyManager {

    @Getter
    public static final String CHANNEL = "rfriend:main";

    private RedisManager redisManager;
    @Getter@Setter
    private static Map<String,String> servers = new HashMap<>();

    public ProxyManager(Plugin plugin){
        if (RFriend.getInstance().isCrossServer()){
            redisManager = new RedisManager(plugin);
            plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin,CHANNEL);
            plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin,CHANNEL, new ChannelListener());
            Bukkit.getConsoleSender().sendMessage(ColorBuilder.convertColors("&a[RFriend] Cross-server mode enabled!"));
        }
    }

}

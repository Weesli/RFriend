package net.weesli.shared.util.proxy;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class ChannelListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (!s.equals(ProxyManager.CHANNEL)) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
        String subchannel = input.readUTF();
        if (subchannel.equals("getServer")){
            String playerName = input.readUTF();
            String server = input.readUTF();
            ProxyManager.getServers().put(playerName,server);
        }
    }
}

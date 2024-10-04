package net.weesli.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.Collection;

public final class Bungee extends Plugin implements Listener {

    @Override
    public void onEnable() {
        getProxy().registerChannel("rfriend:main");
        getProxy().getPluginManager().registerListener(this,this);
    }

    public void sendCustomData(ProxiedPlayer player, byte[] data) {
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        if ( networkPlayers == null || networkPlayers.isEmpty() ) {return;}
        player.getServer().getInfo().sendData( "rfriend:main", data );
    }

    @EventHandler
    public void on(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("rfriend:main"))return;
        ByteArrayDataInput in = ByteStreams.newDataInput( event.getData() );
        String subChannel = in.readUTF();
        if ( event.getReceiver() instanceof ProxiedPlayer receiver) {
            if (subChannel.equalsIgnoreCase("send_friend_request")){
                String senderName = in.readUTF();
                String targetName = in.readUTF();
                String message = in.readUTF();
                ProxiedPlayer target = getProxy().getPlayer(targetName);
                if (target!= null) {
                    target.sendMessage(new TextComponent(message));
                }
            }
            if (subChannel.equalsIgnoreCase("send_request_server")){
                String senderName = in.readUTF();
                String targetName = in.readUTF();
                ProxiedPlayer target = getProxy().getPlayer(targetName);
                if (target!= null) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("getServer");
                    out.writeUTF(targetName);
                    out.writeUTF(target.getServer().getInfo().getName());
                    sendCustomData(receiver, out.toByteArray());
                }
            }
            if (subChannel.equalsIgnoreCase("message")){
                String senderName = in.readUTF();
                String targetName = in.readUTF();
                String message = in.readUTF();
                ProxiedPlayer sender = getProxy().getPlayer(senderName);
                ProxiedPlayer target = getProxy().getPlayer(targetName);
                if (target != null) {
                    sender.sendMessage(new TextComponent(message));
                }
            }
        }
    }
}

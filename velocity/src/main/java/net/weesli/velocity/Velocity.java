package net.weesli.velocity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter@Setter
@Plugin(id = "rfriend", name = "RFriend", version = "1.0", authors = {"Weesli"})
public class Velocity{

    public static final LegacyChannelIdentifier IDENTIFIER = new LegacyChannelIdentifier("rfriend:main");

    @Inject
    private Logger logger;
    @Inject
    private ProxyServer server;


    @Inject
    public Velocity(ProxyServer server, Logger logger){
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        server.getChannelRegistrar().register(IDENTIFIER);
    }

    public void sendPluginMessageToBackendUsingPlayer(Player player, ChannelIdentifier identifier, byte[] data) {
        Optional<ServerConnection> connection = player.getCurrentServer();
        connection.map(serverConnection -> serverConnection.sendPluginMessage(identifier, data));
    }

    @Subscribe
    public void onPluginMessageFromPlayer(PluginMessageEvent event) {
        Player player = (Player) event.getTarget();
        if (!IDENTIFIER.equals(event.getIdentifier())) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();
        if (subChannel.equals("send_friend_request")) {
            String senderName = in.readUTF();
            String targetName = in.readUTF();
            Optional<Player> target = server.getPlayer(targetName);
            List<String> message = List.of(in.readUTF().replace("[", "").replaceAll("]", "").split(", "));
            if (target.isPresent()) {
                for (String line : message) {
                    target.get().sendMessage(Component.text(line));
                }
            }
        }
        if (subChannel.equals("send_request_server")){
            String senderName = in.readUTF();
            String targetName = in.readUTF();
            String serverName = "";
            Optional<Player> target = server.getPlayer(targetName);
            serverName = (target.isEmpty() ? "" : target.get().getCurrentServer().get().getServerInfo().getName());
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("getServer");
            out.writeUTF(targetName);
            out.writeUTF(serverName);
            sendPluginMessageToBackendUsingPlayer(player,IDENTIFIER,out.toByteArray());
        }
        if (subChannel.equals("message")){
            String senderName = in.readUTF();
            UUID targetUUID = UUID.fromString(in.readUTF());
            String message = in.readUTF();
            Optional<Player> target = server.getPlayer(targetUUID);
            target.ifPresent(value -> value.sendMessage(Component.text(message)));
        }
    }

}


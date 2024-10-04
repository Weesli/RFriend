package net.weesli.shared.util.proxy;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.weesli.rozsLib.color.ColorBuilder;
import net.weesli.shared.RFriend;
import net.weesli.shared.gson.GsonProvider;
import net.weesli.shared.model.User;
import net.weesli.shared.model.UserInvite;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelSender {

    public static void publish_user(Player player, User user){
        String message = GsonProvider.getGson().toJson(user);
        RFriend.getInstance().getProxyManager().getRedisManager().getAsyncCommands().publish("rfriend:users", message);
    }

    public static void send_friend_request(Player sender, String target) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("send_friend_request");
        out.writeUTF(sender.getName());
        out.writeUTF(target.toString());
        List<String> message = new ArrayList<>();
        for (String line : RFriend.getInstance().getLang().getStringList("invite-message")){
            message.add(ColorBuilder.convertColors(line.replaceAll("%sender%", sender
                    .getName())));
        }
        out.writeUTF(message.toString());
        sender.sendPluginMessage(RFriend.getInstance(), ProxyManager.CHANNEL, out.toByteArray());
    }


    public static void send_user_invites(Player sender, UserInvite userInvite){
        String message = GsonProvider.getGson().toJson(userInvite);
        RFriend.getInstance().getProxyManager().getRedisManager().getAsyncCommands().publish("rfriend:invites", message);
    }

    public static void sendRequestServer(Player sender, String target) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("send_request_server");
        out.writeUTF(sender.getName());
        out.writeUTF(target);
        sender.sendPluginMessage(RFriend.getInstance(), ProxyManager.CHANNEL, out.toByteArray());
    }


    public static void sendMessage(Player sender, UUID target, String message){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("message");
        out.writeUTF(sender.getName());
        out.writeUTF(target.toString());
        out.writeUTF(message);
        sender.sendPluginMessage(RFriend.getInstance(), ProxyManager.CHANNEL, out.toByteArray());
    }
}

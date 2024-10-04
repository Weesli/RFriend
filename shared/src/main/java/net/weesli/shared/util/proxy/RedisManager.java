package net.weesli.shared.util.proxy;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import lombok.Getter;
import lombok.Setter;
import net.weesli.shared.gson.GsonProvider;
import net.weesli.shared.model.User;
import net.weesli.shared.model.UserInvite;
import net.weesli.shared.util.CacheManager;
import net.weesli.shared.util.FriendManager;
import org.bukkit.plugin.Plugin;
@Getter@Setter
public class RedisManager {

    private RedisClient redisClient;
    private StatefulRedisPubSubConnection<String, String> connection;
    private StatefulRedisConnection<String, String> syncConnection;
    private Plugin plugin;
    private RedisPubSubAsyncCommands<String, String> asyncCommands;

    public RedisManager(Plugin plugin) {
        this.plugin = plugin;
        createConnection();
        registerChannels();
    }

    private void createConnection() {
        String redisUri = plugin.getConfig().getString("options.cross-server.redis-uri");
        redisClient = RedisClient.create(redisUri);
        syncConnection = redisClient.connect();
        connection = redisClient.connectPubSub();
        asyncCommands = connection.async();
    }


    private void registerChannels() {
        asyncCommands.subscribe("rfriend:users", "rfriend:invites");

        connection.addListener(new RedisPubSubListener<>() {
            @Override
            public void message(String channel, String message) {
                switch (channel) {
                    case "rfriend:users":
                        User user = GsonProvider.getGson().fromJson(message, User.class);
                        CacheManager.addUser(user.getUuid(), user);
                        break;
                    case "rfriend:invites":
                        UserInvite invite = GsonProvider.getGson().fromJson(message, UserInvite.class);
                        FriendManager.getInvites().put(invite.getReceiver(), invite);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void message(String string, String k1, String string2) { }

            @Override
            public void subscribed(String channel, long count) { }

            @Override
            public void psubscribed(String pattern, long count) { }

            @Override
            public void unsubscribed(String channel, long count) { }

            @Override
            public void punsubscribed(String pattern, long count) { }
        });
    }

    public void closeConnections() {
        syncConnection.close();
        connection.close();
        redisClient.shutdown();
    }
}

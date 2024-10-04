package net.weesli.shared.model;

import lombok.Getter;
import lombok.Setter;
import net.weesli.shared.enums.FriendSetting;

import java.util.ArrayList;
import java.util.UUID;

@Getter@Setter
public class User {

    private UUID uuid;
    private String username;
    private ArrayList<UUID> friends;
    private ArrayList<UUID> blocks;
    private String status;
    private ArrayList<FriendSetting> friendSettings;

    public User(UUID uuid, String username, ArrayList<UUID> friends, ArrayList<UUID> blocks, ArrayList<FriendSetting> friendSettings) {
        this.uuid = uuid;
        this.username = username;
        this.friends = friends;
        this.blocks = blocks;
        this.status = "";
        this.friendSettings = friendSettings;
    }

    public void addFriend(UUID friendUUID) {
        friends.add(friendUUID);
    }

    public void removeFriend(UUID friendUUID) {
        friends.remove(friendUUID);
    }

    public void addSetting(FriendSetting friendSetting) {
        friendSettings.add(friendSetting);
    }

    public void removeSetting(FriendSetting friendSetting) {
        friendSettings.remove(friendSetting);
    }

    public boolean isSetting(FriendSetting friendSetting) {
        return friendSettings.contains(friendSetting);
    }

    public boolean isFriend(UUID friendUUID) {
        return friends.contains(friendUUID);
    }

    public void addBlock(UUID blockUUID) {
        blocks.add(blockUUID);
    }

    public void removeBlock(UUID blockUUID) {
        blocks.remove(blockUUID);
    }

    public boolean isBlocked(UUID blockUUID) {
        return blocks.contains(blockUUID);
    }

}

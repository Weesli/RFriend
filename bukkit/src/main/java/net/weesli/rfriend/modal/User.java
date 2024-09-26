package net.weesli.rfriend.modal;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter@Setter
public class User {

    private UUID uuid;
    private String username;
    private ArrayList<UUID> friends;
    private ArrayList<UUID> blocks;

    public User(UUID uuid, String username, ArrayList<UUID> friends, ArrayList<UUID> blocks) {
        this.uuid = uuid;
        this.username = username;
        this.friends = friends;
        this.blocks = blocks;
    }

    public void addFriend(UUID friendUUID) {
        friends.add(friendUUID);
    }

    public void removeFriend(UUID friendUUID) {
        friends.remove(friendUUID);
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

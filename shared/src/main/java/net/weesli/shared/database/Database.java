package net.weesli.shared.database;

import net.weesli.shared.model.User;

import java.util.List;
import java.util.UUID;

public interface Database {
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(UUID uuid);
    User getUserByUuid(UUID uuid);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean hasUser(UUID uuid);
    String getName();
}

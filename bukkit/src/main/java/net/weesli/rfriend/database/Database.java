package net.weesli.rfriend.database;

import net.weesli.rfriend.modal.User;

import java.util.List;

public interface Database {
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(String uuid);
    User getUserByUuid(String uuid);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean hasUser(String uuid);
    String getName();
}

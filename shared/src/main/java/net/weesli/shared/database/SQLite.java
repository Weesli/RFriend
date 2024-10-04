package net.weesli.shared.database;

import lombok.SneakyThrows;
import net.weesli.shared.RFriend;
import net.weesli.shared.enums.FriendSetting;
import net.weesli.shared.model.User;
import net.weesli.rozsLib.database.mysql.*;
import net.weesli.rozsLib.database.sqlite.SQLiteBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SQLite implements Database{


    private SQLiteBuilder builder;
    private Connection connection;

    @SneakyThrows
    public SQLite(){
        builder = new SQLiteBuilder("rfriend").setPath(RFriend.getInstance().getDataFolder().getPath());
        connection = builder.build();
        createTable();
    }

    @SneakyThrows
    private  void createTable(){
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("uuid", "TEXT", 255));
        columns.add(new Column("username", "TEXT", 255));
        columns.add(new Column("friends", "TEXT", 65000));
        columns.add(new Column("blocks", "TEXT", 65000));
        columns.add(new Column("settings", "TEXT", 65000));
        builder.createTable("rfriend_friends", connection, columns);
    }


    @SneakyThrows
    @Override
    public void insertUser(User user) {
        Insert insert = new Insert("rfriend_friends", List.of("uuid", "username", "friends", "blocks", "settings"),
                List.of(user.getUuid(),user.getUsername(),user.getFriends(), user.getBlocks(), user.getFriendSettings().stream().map(FriendSetting::name).toList()));
        builder.insert(connection,insert);
    }

    @SneakyThrows
    @Override
    public void updateUser(User user) {
        Update update = new Update("rfriend_friends", List.of("friends", "blocks", "settings"), List.of(user.getFriends(), user.getBlocks(), user.getFriendSettings().stream().map(FriendSetting::name).toList()), Map.of("uuid", String.valueOf(user.getUuid())));
        builder.update(connection,update);
    }

    @SneakyThrows
    @Override
    public void deleteUser(UUID uuid) {
        builder.delete(new Delete(connection, "rfriend_friends", Map.of("uuid", String.valueOf(uuid))));
    }

    @Override
    public User getUserByUuid(UUID uuid) {
        String sql = "SELECT * FROM rfriend_friends WHERE uuid = " + uuid;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, String.valueOf(uuid));
            Result result = new Result(statement.executeQuery());
            if(result.next()){
                List<FriendSetting> settings = result.getStringList("settings").stream().filter(key -> {
                    try {
                        FriendSetting.valueOf(key);
                        return true;
                    }catch (Exception e){
                        return false;
                    }
                }).map(FriendSetting::valueOf).toList();
                return new User(result.getUUID("uuid"), result.getString("username"), new ArrayList<>(result.getUUIDList("friends")), new ArrayList<>(result.getUUIDList("blocks")),new ArrayList<>(settings));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM rfriend_friends WHERE username = " + username;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, username);
            Result result = new Result(statement.executeQuery());
            if(result.next()){
                List<FriendSetting> settings = result.getStringList("settings").stream().filter(key -> {
                    try {
                        FriendSetting.valueOf(key);
                        return true;
                    }catch (Exception e){
                        return false;
                    }
                }).map(FriendSetting::valueOf).toList();
                return new User(result.getUUID("uuid"), result.getString("username"), new ArrayList<>(result.getUUIDList("friends")), new ArrayList<>(result.getUUIDList("blocks")),new ArrayList<>(settings));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM rfriend_friends";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            Result result = new Result(statement.executeQuery());
            while(result.next()){
                List<FriendSetting> settings = result.getStringList("settings").stream().filter(key -> {
                    try {
                        FriendSetting.valueOf(key);
                        return true;
                    }catch (Exception e){
                        return false;
                    }
                }).map(FriendSetting::valueOf).toList();
                users.add(new User(result.getUUID("uuid"), result.getString("username"), new ArrayList<>(result.getUUIDList("friends")), new ArrayList<>(result.getUUIDList("blocks")), new ArrayList<>(settings)));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean hasUser(UUID uuid) {
        String sql = "SELECT COUNT(*) FROM rfriend_friends WHERE uuid=?";
        try(PreparedStatement statement = connection.prepareStatement(
                sql)){
            statement.setString(1, String.valueOf(uuid));
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt(1) > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getName() {
        return "SQLite";
    }
}

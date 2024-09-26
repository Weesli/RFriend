package net.weesli.rfriend.database;

import lombok.SneakyThrows;
import net.weesli.rfriend.modal.User;
import net.weesli.rozsLib.database.mysql.*;
import net.weesli.rozsLib.database.sqlite.SQLiteBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLite implements Database{


    private SQLiteBuilder builder;
    private Connection connection;

    @SneakyThrows
    public SQLite(){
        builder = new SQLiteBuilder("rfriend");
        connection = builder.build();
        createTable();
    }

    @SneakyThrows
    private  void createTable(){
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("uuid", "TEXT PRIMARY KEY", 255));
        columns.add(new Column("username", "TEXT", 255));
        columns.add(new Column("friends", "TEXT", 65000));
        columns.add(new Column("blocks", "TEXT", 65000));
        builder.createTable("rfriend_friends", connection, columns);
    }


    @SneakyThrows
    @Override
    public void insertUser(User user) {
        Insert insert = new Insert("rfriend_friends", List.of("uuid", "username", "friends", "blocks"),
                List.of(user.getUuid(),user.getUsername(),user.getFriends(), user.getBlocks()));
        builder.insert(connection,insert);
    }

    @SneakyThrows
    @Override
    public void updateUser(User user) {
        Update update = new Update("rfriend_friends", List.of("friends", "blocks"), List.of(user.getFriends(), user.getBlocks()), Map.of("uuid", String.valueOf(user.getUuid())));
        builder.update(connection,update);
    }

    @SneakyThrows
    @Override
    public void deleteUser(String uuid) {
        builder.delete(new Delete(connection, "rfriend_friends", Map.of("uuid", uuid)));
    }

    @Override
    public User getUserByUuid(String uuid) {
        String sql = "SELECT * FROM friend_friends WHERE uuid = " + uuid;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, uuid);
            Result result = new Result(statement.executeQuery());
            if(result.next()){
                return new User(result.getUUID("uuid"), result.getString("username"), new ArrayList<>(result.getUUIDList("friends")), new ArrayList<>(result.getUUIDList("blocks")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM friend_friends WHERE username = " + username;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, username);
            Result result = new Result(statement.executeQuery());
            if(result.next()){
                return new User(result.getUUID("uuid"), result.getString("username"), new ArrayList<>(result.getUUIDList("friends")), new ArrayList<>(result.getUUIDList("blocks")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM friend_friends";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            Result result = new Result(statement.executeQuery());
            while(result.next()){
                users.add(new User(result.getUUID("uuid"), result.getString("username"), new ArrayList<>(result.getUUIDList("friends")), new ArrayList<>(result.getUUIDList("blocks"))));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean hasUser(String uuid) {
        String sql = "SELECT COUNT(*) FROM friend_friends WHERE uuid = " + uuid;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
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

package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT* FROM users";
        return jdbcTemplate.query(sqlQuery, userMapper);
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into USERS(USER_NAME, EMAIL, LOGIN, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET user_name=?, login=?, email=?, birthday=? WHERE user_id=? ";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUser(int id) {
        String sqlQuery = "SELECT user_id, user_name, login, email, birthday FROM users WHERE user_id=?";
        return jdbcTemplate.queryForObject(sqlQuery, userMapper, id);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sqlQuery = "INSERT INTO friends(user_id, friend_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        String sqlQuery = "SELECT*\n" +
                "FROM users\n" +
                "WHERE user_id IN (SELECT friend_id\n" +
                "FROM friends\n" +
                "WHERE user_id =?)";
        return jdbcTemplate.query(sqlQuery, userMapper, id);
    }

    @Override
    public List<User> getMutualFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT u.user_id, email, login, user_name, birthday\n" +
                "FROM friends as f1\n" +
                "JOIN friends as f2 ON  f2.friend_id = f1.friend_id AND f2.user_id = ?\n" +
                "JOIN users u on u.user_id = f1.friend_id\n" +
                "WHERE f1.user_id = ?";
        List<User> resultList = jdbcTemplate.query(sqlQuery, userMapper, id, otherId);
        if (resultList.isEmpty()) {
            return Collections.emptyList();
        }
        return resultList;
    }

    @Override
    public Boolean isExistUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", id);
        return userRows.next();

    }

}

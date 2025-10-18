package com.collabchat.dao;

import com.collabchat.models.User;
import com.collabchat.util.DBUtil;

import java.sql.*;

public class UserDAO {

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT user_id, username FROM users WHERE username = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("username"));
                }
            }
        }
        return null;
    }

    public int createUser(String username, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public String getPasswordHashByUsername(String username) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("password_hash");
            }
        }
        return null;
    }

    public User findById(int userId) throws SQLException {
        String sql = "SELECT user_id, username FROM users WHERE user_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new User(rs.getInt("user_id"), rs.getString("username"));
            }
        }
        return null;
    }
}

package com.collabchat.dao;

import com.collabchat.models.Message;
import com.collabchat.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDAO {

    public int saveMessage(int groupId, int userId, String message) throws SQLException {
        String sql = "INSERT INTO messages (group_id, user_id, message) VALUES (?, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ps.setString(3, message);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public List<Message> fetchRecentForGroup(int groupId, int limit) throws SQLException {
    String sql = "SELECT m.message_id, m.group_id, m.user_id, m.message, m.timestamp, u.username FROM messages m JOIN users u ON m.user_id = u.user_id WHERE m.group_id = ? ORDER BY m.timestamp ASC LIMIT ?";
        List<Message> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Message m = new Message();
                    m.setMessageId(rs.getInt("message_id"));
                    m.setGroupId(rs.getInt("group_id"));
                    m.setUserId(rs.getInt("user_id"));
                    m.setMessage(rs.getString("message"));
                    m.setTimestamp(rs.getTimestamp("timestamp"));
                    m.setUsername(rs.getString("username"));
                    out.add(m);
                }
            }
        }
        return out;
    }
}

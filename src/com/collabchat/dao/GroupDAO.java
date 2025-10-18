package com.collabchat.dao;

import com.collabchat.models.Group;
import com.collabchat.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {

    public int createGroup(String name, int createdBy) throws SQLException {
        String sql = "INSERT INTO `groups` (name, created_by) VALUES (?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setInt(2, createdBy);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public Group findById(int groupId) throws SQLException {
        String sql = "SELECT group_id, name, created_by FROM `groups` WHERE group_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Group g = new Group();
                    g.setGroupId(rs.getInt("group_id"));
                    g.setName(rs.getString("name"));
                    g.setCreatedBy(rs.getInt("created_by"));
                    return g;
                }
            }
        }
        return null;
    }

    public List<Group> findGroupsForUser(int userId) throws SQLException {
        String sql = "SELECT g.group_id, g.name, g.created_by FROM `groups` g JOIN group_members gm ON g.group_id = gm.group_id WHERE gm.user_id = ?";
        List<Group> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Group g = new Group();
                    g.setGroupId(rs.getInt("group_id"));
                    g.setName(rs.getString("name"));
                    g.setCreatedBy(rs.getInt("created_by"));
                    out.add(g);
                }
            }
        }
        return out;
    }

    public boolean addMember(int groupId, int userId) throws SQLException {
        String sql = "INSERT IGNORE INTO group_members (user_id, group_id) VALUES (?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, groupId);
            return ps.executeUpdate() > 0;
        }
    }
}

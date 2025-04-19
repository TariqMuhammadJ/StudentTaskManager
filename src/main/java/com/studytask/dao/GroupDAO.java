package com.studytask.dao;

import com.studytask.models.Group;
import com.studytask.models.User;
import com.studytask.exceptions.DAOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupDAO extends GenericDAO<Group> {

    @Override
    public List<Group> findAll() throws DAOException {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM Groups ORDER BY name";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("Executing findAll() query for Groups");
            
            while (rs.next()) {
                Group group = mapResultSetToGroup(rs);
                group.setMembers(findGroupMembers(conn, group.getId()));
                groups.add(group);
                System.out.println("Found group: " + group.getName() + " with " + 
                                 group.getMembers().size() + " members");
            }
            
            return groups;
        } catch (SQLException e) {
            System.err.println("Error in findAll(): " + e.getMessage());
            e.printStackTrace();
            throw new DAOException("Error retrieving all groups", e);
        }
    }

    @Override
    public Optional<Group> findById(int id) throws DAOException {
        String sql = "SELECT * FROM Groups WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Group group = mapResultSetToGroup(rs);
                    group.setMembers(findGroupMembers(conn, group.getId()));
                    return Optional.of(group);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Error finding group by ID", e);
        }
    }

    @Override
    public void save(Group group) throws DAOException {
        System.out.println("============ SAVE GROUP START ============");
        try {
            executeTransaction(conn -> {
                String sql = "INSERT INTO Groups (name) VALUES (?)";
                System.out.println("Attempting to save group: " + group.getName());
                System.out.println("Using SQL: " + sql);
                
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, group.getName());
                    int affectedRows = stmt.executeUpdate();
                    System.out.println("Affected rows: " + affectedRows);

                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            group.setId(generatedId);
                            System.out.println("Generated group ID: " + generatedId);
                        } else {
                            System.err.println("No ID obtained for saved group");
                        }
                    }

                    // Save members
                    if (group.getMembers() != null && !group.getMembers().isEmpty()) {
                        System.out.println("Saving " + group.getMembers().size() + " members");
                        saveGroupMembers(conn, group);
                    } else {
                        System.out.println("No members to save");
                    }
                }
            });
            System.out.println("Group saved successfully");
        } catch (Exception e) {
            System.err.println("============ SAVE GROUP ERROR ============");
            System.err.println("Error type: " + e.getClass().getName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            throw new DAOException("Failed to save group: " + e.getMessage(), e);
        }
        System.out.println("============ SAVE GROUP END ============");
    }

    @Override
    public void update(Group group) throws DAOException {
        try {
            executeTransaction(conn -> {
                String sql = "UPDATE Groups SET name = ? WHERE id = ?";
                
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, group.getName());
                    stmt.setInt(2, group.getId());
                    stmt.executeUpdate();

                    // Update members
                    // First remove all existing members
                    removeAllMembers(conn, group.getId());
                    // Then add the current members
                    if (group.getMembers() != null && !group.getMembers().isEmpty()) {
                        saveGroupMembers(conn, group);
                    }
                }
            });
        } catch (Exception e) {
            throw new DAOException("Failed to update group", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        try {
            executeTransaction(conn -> {
                String sql = "DELETE FROM Groups WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
            });
        } catch (Exception e) {
            throw new DAOException("Failed to delete group", e);
        }
    }

    public List<Group> findByUserId(int userId) throws DAOException {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT g.* FROM Groups g " +
                    "JOIN GroupMembers gm ON g.id = gm.group_id " +
                    "WHERE gm.user_id = ? " +
                    "ORDER BY g.name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Group group = mapResultSetToGroup(rs);
                    group.setMembers(findGroupMembers(conn, group.getId()));
                    groups.add(group);
                }
            }
            return groups;
        } catch (SQLException e) {
            throw new DAOException("Error finding groups by user ID", e);
        }
    }

    public void addMember(int groupId, int userId) throws DAOException {
        try {
            executeTransaction(conn -> {
                String sql = "INSERT INTO GroupMembers (group_id, user_id) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, groupId);
                    stmt.setInt(2, userId);
                    stmt.executeUpdate();
                }
            });
        } catch (Exception e) {
            throw new DAOException("Failed to add member to group", e);
        }
    }

    public void removeMember(int groupId, int userId) throws DAOException {
        try {
            executeTransaction(conn -> {
                String sql = "DELETE FROM GroupMembers WHERE group_id = ? AND user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, groupId);
                    stmt.setInt(2, userId);
                    stmt.executeUpdate();
                }
            });
        } catch (Exception e) {
            throw new DAOException("Failed to remove member from group", e);
        }
    }

    private void removeAllMembers(Connection conn, int groupId) throws SQLException {
        String sql = "DELETE FROM GroupMembers WHERE group_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.executeUpdate();
        }
    }

    private Group mapResultSetToGroup(ResultSet rs) throws SQLException {
        Group group = new Group(rs.getString("name"));
        group.setId(rs.getInt("id"));
        return group;
    }

    private List<User> findGroupMembers(Connection conn, int groupId) throws SQLException {
        List<User> members = new ArrayList<>();
        String sql = "SELECT u.* FROM Users u " +
                    "JOIN GroupMembers gm ON u.id = gm.user_id " +
                    "WHERE gm.group_id = ? " +
                    "ORDER BY u.login";
                    
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setLogin(rs.getString("login"));
                    members.add(user);
                }
            }
        }
        System.out.println("Found " + members.size() + " members for group " + groupId);
        return members;
    }

    private void saveGroupMembers(Connection conn, Group group) throws SQLException {
        String sql = "INSERT INTO GroupMembers (group_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (User member : group.getMembers()) {
                stmt.setInt(1, group.getId());
                stmt.setInt(2, member.getId());
                stmt.executeUpdate();
                System.out.println("Added member " + member.getLogin() + 
                                 " to group " + group.getName());
            }
        }
    }
    
    public void checkTables() throws DAOException {
        try (Connection conn = getConnection()) {
            System.out.println("============ CHECKING TABLES ============");
            
            // Check Groups table
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "GROUPS", null)) {
                System.out.println("Groups table exists: " + rs.next());
            }
            
            // Check GroupMembers table
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "GROUPMEMBERS", null)) {
                System.out.println("GroupMembers table exists: " + rs.next());
            }
            
            // Check table structure
            try (ResultSet rs = conn.getMetaData().getColumns(null, null, "GROUPS", null)) {
                System.out.println("Groups table columns:");
                while (rs.next()) {
                    System.out.println("- " + rs.getString("COLUMN_NAME") + 
                                     " (" + rs.getString("TYPE_NAME") + ")");
                }
            }
            
            System.out.println("============ CHECK COMPLETE ============");
        } catch (SQLException e) {
            throw new DAOException("Failed to check tables", e);
        }
    }
}
package com.studytask.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.studytask.exceptions.DAOException;
import com.studytask.models.Chat;
import com.studytask.util.ConnectionPool;

public class ChatDAO {

    public static void sendMessage(String message, int sender_id, int sent_to_id) throws DAOException {
        String sql = "INSERT INTO Chats (chat_text, sender_id, sent_to_id) VALUES(?, ?, ?)";
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, message);
            stmt.setInt(2, sender_id);
            stmt.setInt(3, sent_to_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error sending message", e);
        }
        
    }

    public List<Chat> getMessagesBetweenUsers(int userId1, int userId2) throws DAOException {
        List<Chat> chats = new ArrayList<>();
        String sql = """
            SELECT * FROM Chats 
            WHERE (sender_id = ? AND sent_to_id = ?)
               OR (sender_id = ? AND sent_to_id = ?)
            ORDER BY sent_at ASC
        """;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId1);
            stmt.setInt(2, userId2);
            stmt.setInt(3, userId2);
            stmt.setInt(4, userId1);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Chat chat = new Chat(
                    rs.getInt("chat_id"),
                    rs.getString("chat_text"), // Should be getString not getInt
                    rs.getInt("sender_id"),
                    rs.getInt("sent_to_id"),
                    rs.getTimestamp("sent_at").toLocalDateTime() // Fix for timestamp
                );
                chats.add(chat);
            }

        } catch (SQLException e) {
            throw new DAOException("Error retrieving messages", e);
        }

        return chats;
    }
}

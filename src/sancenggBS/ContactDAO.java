package sancenggBS;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ContactDAO {
    private String url = "jdbc:mysql://localhost:3306/test";
    private String user = "root";
    private String password = "fanpeiya123";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void addContact(String name, String address, String phone) throws SQLException {
        String query = "INSERT INTO contacts2 (name, address, phone) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
        }
    }

    public void updateContact(int id, String name, String address, String phone) throws SQLException {
        String query = "UPDATE contacts2 SET name = ?, address = ?, phone = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, phone);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteContact(int id) throws SQLException {
        String query = "DELETE FROM contacts2 WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void refreshTable(DefaultTableModel model) throws SQLException {
        String query = "SELECT * FROM contacts2";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {rs.getInt("id"), rs.getString("name"), rs.getString("address"), rs.getString("phone")};
                model.addRow(row);
            }
        }
    }
}

package liangcengBS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ContactManager extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;

    public ContactManager() {
        setTitle("个人通讯录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Object[][] data = {};
        String[] columns = {"ID", "姓名", "住址", "电话"};
        model = new DefaultTableModel(data, columns);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 创建输入面板
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2)); // 修改为3行2列

        nameField = new JTextField();
        addressField = new JTextField();
        phoneField = new JTextField();

        inputPanel.add(new JLabel("姓名："));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("住址："));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("电话："));
        inputPanel.add(phoneField);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加");
        JButton updateButton = new JButton("更新");
        JButton deleteButton = new JButton("删除");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // 添加按钮事件
        addButton.addActionListener(e -> addContact());
        updateButton.addActionListener(e -> updateContact());
        deleteButton.addActionListener(e -> deleteContact());

        // 将输入面板和按钮面板添加到 SOUTH 区域
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel, BorderLayout.SOUTH);
        updateTable(); // 初始化表格数据
    }

    private void addContact() {
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有字段都是必填的！");
            return;
        }
        String query = "INSERT INTO contacts (name, address, phone) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "fanpeiya123");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, phone);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "联系人添加成功！");
                nameField.setText("");
                addressField.setText("");
                phoneField.setText("");
                updateTable();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库错误：" + ex.getMessage());
        }
    }

    private void updateContact() {
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有字段都是必填的！");
            return;
        }
        String query = "UPDATE contacts SET name = ?, address = ?, phone = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "fanpeiya123");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请选择要更新的联系人！");
                return;
            }
            int id = (int) model.getValueAt(selectedRow, 0);
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, phone);
            pstmt.setInt(4, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "联系人更新成功！");
                updateTable();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库错误：" + ex.getMessage());
        }
    }

    private void deleteContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的联系人！");
            return;
        }
        int id = (int) model.getValueAt(selectedRow, 0);
        String query = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "fanpeiya123");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "联系人删除成功！");
                updateTable();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库错误：" + ex.getMessage());
        }
    }

    private void updateTable() {
        String query = "SELECT * FROM contacts";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "fanpeiya123");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {rs.getInt("id"), rs.getString("name"), rs.getString("address"), rs.getString("phone")};
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库错误：" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ContactManager().setVisible(true);
        });
    }
}
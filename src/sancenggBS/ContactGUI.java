package sancenggBS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private ContactManager contactManager = new ContactManager();

    public ContactGUI() {
        setTitle("个人通讯录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Object[][] data = {};
        String[] columns = {"ID", "姓名", "住址", "电话"};
        model = new DefaultTableModel(data, columns);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 创建输入面板，并设置为3行2列的布局
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel, BorderLayout.SOUTH);
        refreshTable();
    }

    private void addContact() {
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有字段都是必填的！");
            return;
        }
        contactManager.addContact(name, address, phone);
        JOptionPane.showMessageDialog(this, "联系人添加成功！");
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
        contactManager.refreshTable(model);
    }

    private void updateContact() {
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有字段都是必填的！");
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要更新的联系人！");
            return;
        }
        // 获取ID值时，需要先转换为String，然后解析为Integer
        int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        contactManager.updateContact(id, name, address, phone);
        JOptionPane.showMessageDialog(this, "联系人更新成功！");
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
        refreshTable();
    }

    private void deleteContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的联系人！");
            return;
        }
        try {
            // 获取ID值时，需要先转换为String，然后解析为Integer
            int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            contactManager.deleteContact(id);
            JOptionPane.showMessageDialog(this, "联系人删除成功！");
            refreshTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID格式不正确，请输入数字。");
        }
    }
    private void refreshTable() {
        contactManager.refreshTable(model);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ContactGUI().setVisible(true);
        });
    }
}
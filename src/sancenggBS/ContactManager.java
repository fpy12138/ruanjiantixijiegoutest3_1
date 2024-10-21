package sancenggBS;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class ContactManager {
    private ContactDAO contactDAO = new ContactDAO();

    public void addContact(String name, String address, String phone) {
        try {
            contactDAO.addContact(name, address, phone);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateContact(int id, String name, String address, String phone) {
        try {
            contactDAO.updateContact(id, name, address, phone);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContact(int id) {
        try {
            contactDAO.deleteContact(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable(DefaultTableModel model) {
        try {
            contactDAO.refreshTable(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package vsu.se22.team1.gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import vsu.se22.team1.Employee;

public class EmployeeContextMenu extends JPopupMenu {
    public EmployeeContextMenu(ManagerFrame parent) {
        if(parent.employeesTable.getSelectedRowCount() == 0) {
            return;
        }
        Employee selected = parent.manager.employees.get(parent.employeesTable.getSelectedRow());
        JMenuItem manage = new JMenuItem("Manage Access");
        manage.addActionListener(e -> {
            new EmployeeAccessFrame(parent, selected).setVisible(true);
        });
        add(manage);
        JMenuItem edit = new JMenuItem("Edit");
        edit.addActionListener(e -> {
            new EmployeeEditFrame(parent, selected) {
                @Override
                protected void submit(Object obj) {
                    Employee edited = (Employee) obj;
                    selected.name = edited.name;
                    selected.id = edited.id;
                    parent.updateTables();
                }
            }.setVisible(true);
        });
        add(edit);
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(e -> {
            parent.manager.removeEmployee(selected);
            parent.updateTables();
        });
        add(delete);
    }
}

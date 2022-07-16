package vsu.se22.team1.gui;

import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vsu.se22.team1.Employee;

public abstract class EmployeeEditFrame extends AbstractEditFrame {

    EmployeeEditFrame(JFrame parent, Employee existing) {
        super(parent, existing == null ? "Add New Employee" : "Edit Employee", "Employee Name", "Employee ID");
        if(existing != null) {
            this.setNameValue(existing.name);
            this.setCodeValue(existing.id);
        }
    }

    @Override
    protected boolean validateInput() {
        try {
            commitCode();
        } catch(ParseException e) {
            JOptionPane.showMessageDialog(null, "An exception occurred while parsing the employee ID.", "Exception",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(getNameValue().isBlank()) {
            JOptionPane.showMessageDialog(null, "Name field may not be blank.", "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected Object getOutput(String name, int code) {
        return new Employee(name, code);
    }
}

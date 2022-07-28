package vsu.se22.team1.gui;

import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vsu.se22.team1.Employee;
import vsu.se22.team1.I18n;

public abstract class EmployeeEditFrame extends AbstractEditFrame {

    EmployeeEditFrame(JFrame parent, Employee existing) {
        super(parent,
                existing == null ? I18n.get("edit-frame.employee.title.new")
                        : I18n.get("edit-frame.employee.title.existing"),
                I18n.get("edit-frame.employee.name"), I18n.get("edit-frame.employee.id"));
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
            JOptionPane.showMessageDialog(null, I18n.get("edit-frame.employee.exception.id"),
                    I18n.get("application.error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(getNameValue().isBlank()) {
            JOptionPane.showMessageDialog(null, I18n.get("edit-frame.employee.exception.name"),
                    I18n.get("application.invalid-input"), JOptionPane.ERROR_MESSAGE);
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

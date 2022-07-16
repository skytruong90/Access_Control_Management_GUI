package vsu.se22.team1.gui;

import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vsu.se22.team1.Building;

public abstract class BuildingEditFrame extends AbstractEditFrame {

    BuildingEditFrame(JFrame parent, Building existing) {
        super(parent, existing == null ? "Add New Building" : "Edit Building", "Building Name", "Building Code");
        if(existing != null) {
            this.setNameValue(existing.name);
            this.setCodeValue(existing.code);
        }
    }

    @Override
    protected boolean validateInput() {
        try {
            commitCode();
        } catch(ParseException e) {
            JOptionPane.showMessageDialog(null, "An exception occurred while parsing the building code.", "Exception",
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
        return new Building(name, code);
    }
}

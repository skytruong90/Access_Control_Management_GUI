package vsu.se22.team1.gui;

import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vsu.se22.team1.Room;

public abstract class RoomEditFrame extends AbstractEditFrame {

    RoomEditFrame(JFrame parent, Room existing) {
        super(parent, existing == null ? "Add New Room" : "Edit Room", "Room Name", "Room Number");
        if(existing != null) {
            this.setCodeValue(existing.code);
        }
        this.nameField.setEnabled(false);

    }

    @Override
    protected boolean validateInput() {
        try {
            commitCode();
        } catch(ParseException e) {
            JOptionPane.showMessageDialog(null, "An exception occurred while parsing the room number.", "Exception",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    protected Object getOutput(String name, int code) {
        return new Room(code);
    }
}

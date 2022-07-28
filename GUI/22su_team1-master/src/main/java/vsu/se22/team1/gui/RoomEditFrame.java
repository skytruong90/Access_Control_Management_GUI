package vsu.se22.team1.gui;

import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vsu.se22.team1.I18n;
import vsu.se22.team1.Room;

public abstract class RoomEditFrame extends AbstractEditFrame {

    RoomEditFrame(JFrame parent, Room existing) {
        super(parent,
                existing == null ? I18n.get("edit-frame.room.title.new") : I18n.get("edit-frame.room.title.existing"),
                "", I18n.get("edit-frame.room.number"));
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
            JOptionPane.showMessageDialog(null, I18n.get("edit-frame.room.exception.number"),
                    I18n.get("application.error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    protected Object getOutput(String name, int code) {
        return new Room(code);
    }
}

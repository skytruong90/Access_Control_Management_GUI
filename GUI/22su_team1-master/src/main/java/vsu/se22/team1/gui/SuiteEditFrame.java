package vsu.se22.team1.gui;

import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vsu.se22.team1.I18n;
import vsu.se22.team1.Suite;

public abstract class SuiteEditFrame extends AbstractEditFrame {

    SuiteEditFrame(JFrame parent, Suite existing) {
        super(parent,
                existing == null ? I18n.get("edit-frame.suite.title.new") : I18n.get("edit-frame.suite.title.existing"),
                I18n.get("edit-frame.suite.name"), I18n.get("edit-frame.suite.code"));
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
            JOptionPane.showMessageDialog(null, I18n.get("edit-frame.suite.exception.code"),
                    I18n.get("application.error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(getNameValue().isBlank()) {
            JOptionPane.showMessageDialog(null, I18n.get("edit-frame.suite.exception.name"),
                    I18n.get("application.invalid-input"), JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected Object getOutput(String name, int code) {
        return new Suite(name, code);
    }
}

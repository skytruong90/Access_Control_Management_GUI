package vsu.se22.team1.gui;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import vsu.se22.team1.I18n;

public abstract class AbstractEditFrame extends JFrame {
    private SpringLayout layout = new SpringLayout();
    private JLabel nameLabel = new JLabel();
    JTextField nameField = new JTextField();
    private JLabel codeLabel = new JLabel();
    private JSpinner codeSpinner = new JSpinner();
    private JButton submitButton = new JButton(I18n.get("application.submit"));
    private JButton cancelButton = new JButton(I18n.get("application.cancel"));

    AbstractEditFrame(JFrame parent, String title, String nameHeader, String codeHeader) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(layout);
        nameLabel.setText(nameHeader);
        codeLabel.setText(codeHeader);
        addComponents();
        layout.putConstraint(SpringLayout.EAST, getContentPane(), 5, SpringLayout.EAST, cancelButton);
        layout.putConstraint(SpringLayout.SOUTH, getContentPane(), 5, SpringLayout.SOUTH, cancelButton);
        pack();
        setLocationRelativeTo(parent);
    }

    private void addComponents() {
        nameLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, nameLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, nameLabel, 5, SpringLayout.NORTH, this);
        add(nameLabel);
        nameField.setPreferredSize(new Dimension(200, 30));
        layout.putConstraint(SpringLayout.WEST, nameField, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, nameField, 5, SpringLayout.SOUTH, nameLabel);
        add(nameField);

        codeLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, codeLabel, 5, SpringLayout.EAST, nameLabel);
        layout.putConstraint(SpringLayout.NORTH, codeLabel, 5, SpringLayout.NORTH, this);
        add(codeLabel);
        codeSpinner.setPreferredSize(new Dimension(200, 30));
        layout.putConstraint(SpringLayout.WEST, codeSpinner, 5, SpringLayout.EAST, nameField);
        layout.putConstraint(SpringLayout.NORTH, codeSpinner, 5, SpringLayout.SOUTH, codeLabel);
        add(codeSpinner);

        submitButton.setPreferredSize(new Dimension(200, 20));
        submitButton.addActionListener(e -> {
            if(validateInput()) {
                submit(getOutput(getNameValue(), getCodeValue()));
                close();
            }
        });
        layout.putConstraint(SpringLayout.WEST, submitButton, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, submitButton, 5, SpringLayout.SOUTH, nameField);
        add(submitButton);
        cancelButton.setPreferredSize(new Dimension(200, 20));
        cancelButton.addActionListener(e -> {
            close();
        });
        layout.putConstraint(SpringLayout.WEST, cancelButton, 5, SpringLayout.EAST, submitButton);
        layout.putConstraint(SpringLayout.NORTH, cancelButton, 5, SpringLayout.SOUTH, codeSpinner);
        add(cancelButton);
    }

    protected String getNameValue() {
        return nameField.getText();
    }

    protected void setNameValue(String value) {
        nameField.setText(value);
    }

    protected int getCodeValue() {
        return (int) codeSpinner.getValue();
    }

    protected void setCodeValue(int value) {
        codeSpinner.setValue(value);
    }

    protected void commitCode() throws ParseException {
        codeSpinner.commitEdit();
    }

    private void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    protected abstract boolean validateInput();

    protected abstract Object getOutput(String name, int code);

    protected abstract void submit(Object obj);
}

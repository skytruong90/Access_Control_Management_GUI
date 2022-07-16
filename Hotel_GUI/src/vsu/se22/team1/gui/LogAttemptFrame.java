package vsu.se22.team1.gui;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpringLayout;

import vsu.se22.team1.Area;
import vsu.se22.team1.AttemptFlag;
import vsu.se22.team1.Building;
import vsu.se22.team1.Room;
import vsu.se22.team1.Suite;

public class LogAttemptFrame extends JFrame {
    private ManagerFrame parent;
    private SpringLayout layout = new SpringLayout();
    private JLabel areaLabel = new JLabel("Area");
    private JComboBox<Area> area;
    private JLabel employeeLabel = new JLabel("Employee ID");
    private JSpinner employee = new JSpinner();
    private JButton submitButton = new JButton("Submit");
    private JButton cancelButton = new JButton("Cancel");

    LogAttemptFrame(ManagerFrame parent) {
        this.parent = parent;
        setTitle("New");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(layout);
        addComponents();
        layout.putConstraint(SpringLayout.EAST, getContentPane(), 5, SpringLayout.EAST, cancelButton);
        layout.putConstraint(SpringLayout.SOUTH, getContentPane(), 5, SpringLayout.SOUTH, cancelButton);
        pack();
        setLocationRelativeTo(parent);
    }

    private void addComponents() {
        areaLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, areaLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, areaLabel, 5, SpringLayout.NORTH, this);
        add(areaLabel);
        List<Area> areas = new ArrayList<>();
        for(Building b : parent.manager.buildings) {
            areas.add(b);
            for(Suite s : b.getSuites()) {
                areas.add(s);
                for(Room r : s.getRooms()) {
                    areas.add(r);
                }
            }
        }
        Area[] areaArray = new Area[areas.size()];
        area = new JComboBox<>(areas.toArray(areaArray));
        area.setSelectedItem(parent.logFilter.area);
        area.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, area, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, area, 5, SpringLayout.SOUTH, areaLabel);
        add(area);

        employeeLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, employeeLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, employeeLabel, 10, SpringLayout.SOUTH, area);
        add(employeeLabel);
        employee.setPreferredSize(new Dimension(200, 20));
        employee.setValue(parent.logFilter.employeeId);
        layout.putConstraint(SpringLayout.WEST, employee, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, employee, 5, SpringLayout.SOUTH, employeeLabel);
        add(employee);

        submitButton.setPreferredSize(new Dimension(200, 20));
        submitButton.addActionListener(e -> {
            Area a = (Area) area.getSelectedItem();
            if(a == null) {
                JOptionPane.showMessageDialog(LogAttemptFrame.this, "An area must be selected.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) employee.getValue();
            AttemptFlag flag = parent.manager.accessManager.logAttempt(id, a);
            parent.updateTables();
            close();
            if(flag == AttemptFlag.SECURITY_ALERT) {
                JOptionPane.showMessageDialog(parent, "SECURITY ALERT: No employee exists with the ID " + id,
                        "Security Alert", JOptionPane.WARNING_MESSAGE);
            }
        });
        layout.putConstraint(SpringLayout.WEST, submitButton, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, submitButton, 20, SpringLayout.SOUTH, employee);
        add(submitButton);
        cancelButton.setPreferredSize(new Dimension(200, 20));
        cancelButton.addActionListener(e -> {
            close();
        });
        layout.putConstraint(SpringLayout.WEST, cancelButton, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, cancelButton, 5, SpringLayout.SOUTH, submitButton);
        add(cancelButton);
    }

    private void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}

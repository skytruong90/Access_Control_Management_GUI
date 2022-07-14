package vsu.se22.team1.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AccessControlSystem extends JFrame implements ActionListener {
    private JButton addBuildingButton, addSuiteButton, addRoomButton, addEmployeeButton, testAccessButton,
            generateReportButton;
    private JPanel buttonPanel;

    public AccessControlSystem() {
        setTitle("Access Control System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3));

        addBuildingButton = new JButton("Add Building");
        addSuiteButton = new JButton("Add Suite");
        addRoomButton = new JButton("Add Room");
        addEmployeeButton = new JButton("Add Employee");
        testAccessButton = new JButton("Test Access");
        generateReportButton = new JButton("Generate Report");

        buttonPanel.add(addBuildingButton);
        buttonPanel.add(addSuiteButton);
        buttonPanel.add(addRoomButton);
        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(testAccessButton);
        buttonPanel.add(generateReportButton);

        add(buttonPanel, BorderLayout.CENTER);

        addBuildingButton.addActionListener(this);
        addSuiteButton.addActionListener(this);
        addRoomButton.addActionListener(this);
        addEmployeeButton.addActionListener(this);
        testAccessButton.addActionListener(this);
        generateReportButton.addActionListener(this);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addBuildingButton) {
            // Add Building
        } else if(e.getSource() == addSuiteButton) {
            // Add Suite
        } else if(e.getSource() == addRoomButton) {
            // Add Room
        } else if(e.getSource() == addEmployeeButton) {
            // Add Employee
        } else if(e.getSource() == testAccessButton) {
            // Test Access
        } else if(e.getSource() == generateReportButton) {
            // Generate Report
        }
    }
}

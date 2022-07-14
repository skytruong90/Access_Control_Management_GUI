package com.codebind;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame{
    private JButton buttonOne;
    private JPanel mainPanel;
    private JTextField tfEmployeeID;
    private JTextField tfBuldingCode;
    private JTextField tfRoomCode;
    private JButton btnCHECK;
    private JButton btnCLEAR;
    private JLabel lbWelcome;


    public App() {
        setContentPane(mainPanel);
        setTitle(" Welcome To Alexandria Hotel ");
        setSize(650, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        btnCHECK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkEmployees();
                String employeeID = tfEmployeeID.getText();
               // String buildingCode = tfBuldingCode.getText();
               // String roomCode = tfRoomCode.getText();
                lbWelcome.setText("Welcome! \n" + employeeID);

            }
        });
        btnCLEAR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfEmployeeID.setText("");
                tfBuldingCode.setText("");
                tfRoomCode.setText("");
            }
        });
    }

    private void checkEmployees() {
        String employeeID = tfEmployeeID.getText();
        String buildingCode = tfBuldingCode.getText();
        String roomCode = tfRoomCode.getText();

        if (employeeID.isEmpty() || buildingCode.isEmpty() || roomCode.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields", "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    public static void main(String[] args) {
        App myFrame = new App();

    }
}

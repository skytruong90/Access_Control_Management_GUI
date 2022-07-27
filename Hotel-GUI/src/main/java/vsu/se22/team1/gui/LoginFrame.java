package vsu.se22.team1.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import vsu.se22.team1.Config;
import vsu.se22.team1.I18n;
import vsu.se22.team1.Main;
import vsu.se22.team1.Utils;

public class LoginFrame extends JFrame {
    private static final long LOCKOUT_PERIOD = 1000 * 60 * 60; // one hour

    private static int attempts = 3;
    private static long lockoutTime;

    private SpringLayout layout = new SpringLayout();
    private JPanel panel = new JPanel();
    private JLabel logo = new JLabel();
    private JLabel usernameLabel = new JLabel(I18n.get("login.username"));
    private JTextField usernameField = new JTextField();
    private JLabel passwordLabel = new JLabel(I18n.get("login.password"));
    private JTextField passwordField = new JPasswordField();
    private JLabel attemptsLabel = new JLabel();
    private JButton newAccountButton = new JButton(I18n.get("login.create-account"));
    private JButton submitButton = new JButton(I18n.get("login.submit"));
    private Font font = new Font("Verdana", Font.PLAIN, 16);
    private int fontHeight = getFontMetrics(font).getHeight() + getFontMetrics(font).getMaxAscent();
    private Font fontSmall = font.deriveFont(12.0f);
    private int fontSmallHeight = getFontMetrics(fontSmall).getHeight() + getFontMetrics(fontSmall).getMaxAscent();
    private Font fontSmaller = font.deriveFont(10.0f);
    private int fontSmallerHeight = getFontMetrics(fontSmaller).getHeight()
            + getFontMetrics(fontSmaller).getMaxAscent();

    public LoginFrame() {
        String lockoutTimeString = Config.get("lockout.timestamp");
        lockoutTime = lockoutTimeString == null ? 0L : Long.valueOf(lockoutTimeString, Character.MAX_RADIX);
        setTitle(Config.get("login.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(Integer.valueOf(Config.get("login.background-color"), 16)));
        addComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void addComponents() {
        panel.setLayout(layout);

        logo.setOpaque(false);
        BufferedImage icon;
        try {
            icon = ImageIO.read(LoginFrame.class.getResource("/icon.png"));
            logo.setIcon(new ImageIcon(icon.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            logo.setPreferredSize(new Dimension(200, 100));
        } catch(IOException | IllegalArgumentException e) {
            logo.setPreferredSize(new Dimension(0, 0));
        }
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        layout.putConstraint(SpringLayout.NORTH, logo, 20, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, logo, 0, SpringLayout.WEST, submitButton);
        layout.putConstraint(SpringLayout.EAST, logo, 0, SpringLayout.EAST, submitButton);
        panel.add(logo);

        usernameLabel.setPreferredSize(new Dimension(400, fontHeight));
        usernameLabel.setFont(font);
        layout.putConstraint(SpringLayout.WEST, usernameLabel, 20, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, usernameLabel, 20, SpringLayout.SOUTH, logo);
        panel.add(usernameLabel);
        usernameField.setPreferredSize(new Dimension(400, fontHeight));
        usernameField.addActionListener(e -> {
            login();
        });
        usernameField.setFont(font);
        layout.putConstraint(SpringLayout.WEST, usernameField, 20, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, usernameField, 0, SpringLayout.EAST, submitButton);
        layout.putConstraint(SpringLayout.NORTH, usernameField, 0, SpringLayout.SOUTH, usernameLabel);
        panel.add(usernameField);

        passwordLabel.setFont(font);
        passwordLabel.setPreferredSize(usernameLabel.getPreferredSize());
        layout.putConstraint(SpringLayout.WEST, passwordLabel, 20, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, passwordLabel, 10, SpringLayout.SOUTH, usernameField);
        panel.add(passwordLabel);
        passwordField.setPreferredSize(usernameField.getPreferredSize());
        passwordField.setFont(font);
        passwordField.addActionListener(e -> {
            login();
        });
        layout.putConstraint(SpringLayout.WEST, passwordField, 20, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, submitButton);
        layout.putConstraint(SpringLayout.NORTH, passwordField, 0, SpringLayout.SOUTH, passwordLabel);
        panel.add(passwordField);

        attemptsLabel.setPreferredSize(new Dimension(400, 0));
        attemptsLabel.setForeground(new Color(0xB71C1C));
        attemptsLabel.setFont(fontSmaller);
        layout.putConstraint(SpringLayout.WEST, attemptsLabel, 0, SpringLayout.WEST, submitButton);
        layout.putConstraint(SpringLayout.NORTH, attemptsLabel, 0, SpringLayout.SOUTH, passwordField);
        panel.add(attemptsLabel);

        Color buttonColor = new Color(Integer.valueOf(Config.get("login.button-color"), 16));
        submitButton.setPreferredSize(new Dimension(200, fontHeight));
        submitButton.setBackground(buttonColor);
        submitButton.setBorderPainted(false);
        submitButton.setForeground(
                Utils.isDark(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue()) ? Color.WHITE
                        : Color.BLACK);
        submitButton.setFont(font.deriveFont(Font.BOLD));
        submitButton.addActionListener(e -> {
            login();
        });
        layout.putConstraint(SpringLayout.WEST, submitButton, 20, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, submitButton, 40, SpringLayout.SOUTH, attemptsLabel);
        panel.add(submitButton);
        newAccountButton.setPreferredSize(new Dimension(submitButton.getPreferredSize().width, fontSmallHeight));
        newAccountButton.setOpaque(false);
        newAccountButton.setContentAreaFilled(false);
        newAccountButton.setBorderPainted(false);
        newAccountButton.setFont(fontSmall);
        newAccountButton.addActionListener(e -> {
            try {
                newUser();
            } catch(IOException e1) {
                JOptionPane.showMessageDialog(LoginFrame.this, I18n.get("login.error.create-account"),
                        I18n.get("application.error"), JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        layout.putConstraint(SpringLayout.WEST, newAccountButton, 0, SpringLayout.WEST, submitButton);
        layout.putConstraint(SpringLayout.NORTH, newAccountButton, 20, SpringLayout.SOUTH, submitButton);
        panel.add(newAccountButton);

        layout.putConstraint(SpringLayout.EAST, panel, 20, SpringLayout.EAST, submitButton);
        layout.putConstraint(SpringLayout.SOUTH, panel, 20, SpringLayout.SOUTH, newAccountButton);

        panel.setBorder(BorderFactory.createMatteBorder(80, 160, 80, 160, getContentPane().getBackground()));
        panel.setBackground(Color.WHITE);

        add(panel);
    }

    private void login() {
        if(isLockedOut()) {
            long time = (lockoutTime + LOCKOUT_PERIOD) - System.currentTimeMillis();
            JOptionPane.showMessageDialog(this,
                    I18n.get("login.lockout.wait-time", TimeUnit.MILLISECONDS.toMinutes(time),
                            TimeUnit.MILLISECONDS.toSeconds(time)
                                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))),
                    I18n.get("login.lockout.title"), JOptionPane.ERROR_MESSAGE);
            return;
        } else if(attempts == 0) {
            attempts = 3;
            attemptsLabel.setPreferredSize(new Dimension(attemptsLabel.getPreferredSize().width, 0));
            attemptsLabel.revalidate();
            attemptsLabel.repaint();
        }
        if(usernameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, I18n.get("login.error.missing.username"), I18n.get("application.error"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, I18n.get("login.error.missing.password"), I18n.get("application.error"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean success = false;
        try {
            success = Config.checkPassword(usernameField.getText(),
                    Utils.sha512(usernameField.getText().getBytes(), passwordField.getText().getBytes()));
        } catch(IOException e1) {
            JOptionPane.showMessageDialog(LoginFrame.this, I18n.get("login.error.submit"),
                    I18n.get("application.error"), JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
        if(success) {
            Main.launchManager();
            this.dispose();
        } else {
            attempts--;
            attemptsLabel.setText(I18n.get("login.lockout.attempts", attempts));
            attemptsLabel.setPreferredSize(new Dimension(attemptsLabel.getPreferredSize().width, fontSmallerHeight));
            attemptsLabel.revalidate();
            attemptsLabel.repaint();
            if(attempts == 0) {
                lockoutTime = System.currentTimeMillis();
                JOptionPane.showMessageDialog(this, I18n.get("login.lockout.notice"), I18n.get("login.lockout.title"),
                        JOptionPane.ERROR_MESSAGE);
                Config.put("lockout.timestamp", Long.toString(lockoutTime, Character.MAX_RADIX).toUpperCase());
                try {
                    Config.saveConfig();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, I18n.get("login.error.incorrect"), I18n.get("application.error"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void newUser() throws IOException {
        if(usernameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, I18n.get("login.error.missing.username"), I18n.get("application.error"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(passwordField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, I18n.get("login.error.missing.password"), I18n.get("application.error"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(Config.userExists(usernameField.getText())) {
            JOptionPane.showMessageDialog(this, I18n.get("login.error.user-exists", usernameField.getText()),
                    I18n.get("login.error.user-exists.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        Config.writePassword(usernameField.getText(),
                Utils.sha512(usernameField.getText().getBytes(), passwordField.getText().getBytes()));
        usernameField.setText("");
        passwordField.setText("");
        JOptionPane.showMessageDialog(this, I18n.get("login.account-created"), I18n.get("login.account-created.title"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static boolean isLockedOut() {
        return System.currentTimeMillis() < lockoutTime + LOCKOUT_PERIOD;
    }
}

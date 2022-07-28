package vsu.se22.team1.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;

import vsu.se22.team1.Area;
import vsu.se22.team1.Building;
import vsu.se22.team1.Employee;
import vsu.se22.team1.I18n;
import vsu.se22.team1.Room;
import vsu.se22.team1.Suite;

public class EmployeeAccessFrame extends JFrame {
    private ManagerFrame parent;
    private Employee employee;
    private SpringLayout layout = new SpringLayout();
    private JPanel areaPanel;
    private JScrollPane areaScroll = new JScrollPane(areaPanel);
    private JLabel areaLabel = new JLabel(I18n.get("access-frame.area"));
    private JLabel allowLabel = new JLabel(I18n.get("access-frame.allow"));
    private JLabel denyLabel = new JLabel(I18n.get("access-frame.deny"));
    private JLabel inheritLabel = new JLabel(I18n.get("access-frame.inherit"));
    private JButton closeButton = new JButton(I18n.get("application.close"));

    EmployeeAccessFrame(ManagerFrame parent, Employee employee) {
        this.parent = parent;
        this.employee = employee;
        setTitle(I18n.get("access-frame.title", employee.toString()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(layout);
        addComponents();
        layout.putConstraint(SpringLayout.EAST, getContentPane(), 5, SpringLayout.EAST, closeButton);
        layout.putConstraint(SpringLayout.SOUTH, getContentPane(), 5, SpringLayout.SOUTH, closeButton);
        pack();
        setLocationRelativeTo(parent);
    }

    private void addComponents() {
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
        SpringLayout l = new SpringLayout();
        areaPanel = new JPanel(l);
        areaPanel.setLayout(l);
        areaLabel.setPreferredSize(new Dimension(230, 20));
        areaLabel.setBackground(Color.GRAY);
        l.putConstraint(SpringLayout.WEST, areaLabel, 5, SpringLayout.WEST, areaPanel);
        l.putConstraint(SpringLayout.NORTH, areaLabel, 5, SpringLayout.NORTH, areaPanel);
        areaPanel.add(areaLabel);
        allowLabel.setPreferredSize(new Dimension(40, 20));
        l.putConstraint(SpringLayout.WEST, allowLabel, 5, SpringLayout.EAST, areaLabel);
        l.putConstraint(SpringLayout.NORTH, allowLabel, 5, SpringLayout.NORTH, areaPanel);
        areaPanel.add(allowLabel);
        denyLabel.setPreferredSize(new Dimension(40, 20));
        l.putConstraint(SpringLayout.WEST, denyLabel, 5, SpringLayout.EAST, allowLabel);
        l.putConstraint(SpringLayout.NORTH, denyLabel, 5, SpringLayout.NORTH, areaPanel);
        areaPanel.add(denyLabel);
        inheritLabel.setPreferredSize(new Dimension(40, 20));
        l.putConstraint(SpringLayout.WEST, inheritLabel, 5, SpringLayout.EAST, denyLabel);
        l.putConstraint(SpringLayout.NORTH, inheritLabel, 5, SpringLayout.NORTH, areaPanel);
        areaPanel.add(inheritLabel);
        JLabel prev = areaLabel;
        Font font = areaLabel.getFont().deriveFont(Font.PLAIN);
        int minHeight = 0;
        for(Area a : areas) {
            JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
            l.putConstraint(SpringLayout.WEST, sep, 0, SpringLayout.WEST, areaPanel);
            l.putConstraint(SpringLayout.EAST, sep, 0, SpringLayout.EAST, areaPanel);
            l.putConstraint(SpringLayout.NORTH, sep, 2, SpringLayout.SOUTH, prev);
            areaPanel.add(sep);

            JLabel label = new JLabel(a.toString());
            label.setPreferredSize(new Dimension(230, 20));
            label.setFont(font);
            l.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, areaPanel);
            l.putConstraint(SpringLayout.NORTH, label, 2, SpringLayout.SOUTH, sep);
            areaPanel.add(label);

            ButtonGroup bg = new ButtonGroup();

            JRadioButton allow = new JRadioButton();
            allow.setPreferredSize(new Dimension(40, 20));
            allow.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    employee.setAccessState(a, true);
                }
            });
            bg.add(allow);
            l.putConstraint(SpringLayout.WEST, allow, 5, SpringLayout.EAST, label);
            l.putConstraint(SpringLayout.NORTH, allow, 2, SpringLayout.SOUTH, sep);
            areaPanel.add(allow);

            JRadioButton deny = new JRadioButton();
            deny.setPreferredSize(allow.getPreferredSize());
            deny.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    employee.setAccessState(a, false);
                }
            });
            bg.add(deny);
            l.putConstraint(SpringLayout.WEST, deny, 5, SpringLayout.EAST, allow);
            l.putConstraint(SpringLayout.NORTH, deny, 2, SpringLayout.SOUTH, sep);
            areaPanel.add(deny);

            JRadioButton inherit = new JRadioButton();
            inherit.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    employee.clearAccessState(a);
                }
            });
            inherit.setPreferredSize(allow.getPreferredSize());
            bg.add(inherit);
            l.putConstraint(SpringLayout.WEST, inherit, 5, SpringLayout.EAST, deny);
            l.putConstraint(SpringLayout.NORTH, inherit, 2, SpringLayout.SOUTH, sep);
            areaPanel.add(inherit);

            if(employee.inheritsAccess(a)) {
                inherit.setSelected(true);
            } else if(employee.hasAccess(a)) {
                allow.setSelected(true);
            } else {
                deny.setSelected(true);
            }
            minHeight += sep.getPreferredSize().getHeight() + label.getPreferredSize().getHeight() + 5;

            prev = label;
        }
        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
        l.putConstraint(SpringLayout.WEST, sep, 0, SpringLayout.WEST, areaPanel);
        l.putConstraint(SpringLayout.EAST, sep, 0, SpringLayout.EAST, areaPanel);
        l.putConstraint(SpringLayout.NORTH, sep, 2, SpringLayout.SOUTH, prev);
        areaPanel.add(sep);
        JSeparator sep0 = new JSeparator(JSeparator.VERTICAL);
        l.putConstraint(SpringLayout.WEST, sep0, 0, SpringLayout.EAST, areaLabel);
        l.putConstraint(SpringLayout.NORTH, sep0, 0, SpringLayout.NORTH, areaPanel);
        l.putConstraint(SpringLayout.SOUTH, sep0, 0, SpringLayout.SOUTH, prev);
        areaPanel.add(sep0);
        JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
        l.putConstraint(SpringLayout.WEST, sep1, 0, SpringLayout.EAST, allowLabel);
        l.putConstraint(SpringLayout.NORTH, sep1, 0, SpringLayout.NORTH, areaPanel);
        l.putConstraint(SpringLayout.SOUTH, sep1, 0, SpringLayout.SOUTH, prev);
        areaPanel.add(sep1);
        JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
        l.putConstraint(SpringLayout.WEST, sep2, 0, SpringLayout.EAST, denyLabel);
        l.putConstraint(SpringLayout.NORTH, sep2, 0, SpringLayout.NORTH, areaPanel);
        l.putConstraint(SpringLayout.SOUTH, sep2, 0, SpringLayout.SOUTH, prev);
        areaPanel.add(sep2);

        areaPanel.setPreferredSize(new Dimension(380, minHeight));

        areaScroll.setPreferredSize(new Dimension(400, 600));
        areaScroll.add(areaPanel);
        areaScroll.setViewportView(areaPanel);
        areaScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        areaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        layout.putConstraint(SpringLayout.WEST, areaScroll, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, areaScroll, 5, SpringLayout.NORTH, this);
        add(areaScroll);

        closeButton.setPreferredSize(new Dimension(400, 20));
        closeButton.addActionListener(e -> {
            parent.updateTables();
            close();
        });
        layout.putConstraint(SpringLayout.WEST, closeButton, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, closeButton, 20, SpringLayout.SOUTH, areaScroll);
        add(closeButton);
    }

    private void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}

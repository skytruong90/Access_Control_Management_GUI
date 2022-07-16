package vsu.se22.team1.gui;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpringLayout;

import vsu.se22.team1.Area;
import vsu.se22.team1.AttemptFlag;
import vsu.se22.team1.Building;
import vsu.se22.team1.Room;
import vsu.se22.team1.Suite;

public class LogFilterFrame extends JFrame {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private ManagerFrame parent;
    private SpringLayout layout = new SpringLayout();
    private JLabel filterByLabel = new JLabel("Filter By");
    private JComboBox<LogFilter.FilterKey> filterBy;
    private JLabel sortByLabel = new JLabel("Sort By");
    private JComboBox<LogFilter.FilterKey> sortBy;
    private JLabel dateStartLabel = new JLabel("Start Date (YYYY-MM-DD)");
    private JFormattedTextField dateStart = new JFormattedTextField(DATE_FORMAT);
    private JLabel dateEndLabel = new JLabel("End Date (YYYY-MM-DD)");
    private JFormattedTextField dateEnd = new JFormattedTextField(DATE_FORMAT);
    private JLabel areaLabel = new JLabel("Area");
    private JComboBox<Area> area;
    private JLabel employeeLabel = new JLabel("Employee ID");
    private JSpinner employee = new JSpinner();
    private JLabel flagLabel = new JLabel("Attempt Flag");
    private JComboBox<AttemptFlag> flag;
    private ButtonGroup sortDirection = new ButtonGroup();
    private JRadioButton sortDirectionAscending = new JRadioButton("Sort Ascending");
    private JRadioButton sortDirectionDescending = new JRadioButton("Sort Descending");
    private JButton submitButton = new JButton("Submit");
    private JButton cancelButton = new JButton("Cancel");

    LogFilterFrame(ManagerFrame parent) {
        this.parent = parent;
        setTitle("Access Log Filter");
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
        filterByLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, filterByLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, filterByLabel, 5, SpringLayout.NORTH, this);
        add(filterByLabel);
        filterBy = new JComboBox<>(LogFilter.FilterKey.values());
        filterBy.setSelectedItem(parent.logFilter.filterBy);
        filterBy.setPreferredSize(new Dimension(200, 30));
        layout.putConstraint(SpringLayout.WEST, filterBy, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, filterBy, 5, SpringLayout.SOUTH, filterByLabel);
        add(filterBy);

        sortByLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, sortByLabel, 5, SpringLayout.EAST, filterByLabel);
        layout.putConstraint(SpringLayout.NORTH, sortByLabel, 5, SpringLayout.NORTH, this);
        add(sortByLabel);
        sortBy = new JComboBox<>(LogFilter.FilterKey.valuesExcludingNone());
        sortBy.setSelectedItem(parent.logFilter.sortBy);
        sortBy.setPreferredSize(new Dimension(200, 30));
        layout.putConstraint(SpringLayout.WEST, sortBy, 5, SpringLayout.EAST, filterBy);
        layout.putConstraint(SpringLayout.NORTH, sortBy, 5, SpringLayout.SOUTH, sortByLabel);
        add(sortBy);

        dateStartLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, dateStartLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, dateStartLabel, 20, SpringLayout.SOUTH, filterBy);
        add(dateStartLabel);
        dateStart.setPreferredSize(new Dimension(200, 20));
        dateStart.setValue(new Date(parent.logFilter.startTimestamp == -1 ? 0 : parent.logFilter.startTimestamp));
        layout.putConstraint(SpringLayout.WEST, dateStart, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, dateStart, 5, SpringLayout.SOUTH, dateStartLabel);
        add(dateStart);

        dateEndLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, dateEndLabel, 5, SpringLayout.EAST, dateStartLabel);
        layout.putConstraint(SpringLayout.NORTH, dateEndLabel, 20, SpringLayout.SOUTH, sortBy);
        add(dateEndLabel);
        dateEnd.setPreferredSize(new Dimension(200, 20));
        dateEnd.setValue(new Date(
                parent.logFilter.endTimestamp == -1 ? System.currentTimeMillis() : parent.logFilter.endTimestamp));
        layout.putConstraint(SpringLayout.WEST, dateEnd, 5, SpringLayout.EAST, dateStart);
        layout.putConstraint(SpringLayout.NORTH, dateEnd, 5, SpringLayout.SOUTH, dateEndLabel);
        add(dateEnd);

        areaLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, areaLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, areaLabel, 10, SpringLayout.SOUTH, dateStart);
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
        layout.putConstraint(SpringLayout.WEST, employeeLabel, 5, SpringLayout.EAST, areaLabel);
        layout.putConstraint(SpringLayout.NORTH, employeeLabel, 10, SpringLayout.SOUTH, dateEnd);
        add(employeeLabel);
        employee.setPreferredSize(new Dimension(200, 20));
        employee.setValue(parent.logFilter.employeeId);
        layout.putConstraint(SpringLayout.WEST, employee, 5, SpringLayout.EAST, area);
        layout.putConstraint(SpringLayout.NORTH, employee, 5, SpringLayout.SOUTH, employeeLabel);
        add(employee);

        flagLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, flagLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, flagLabel, 10, SpringLayout.SOUTH, area);
        add(flagLabel);
        flag = new JComboBox<>(AttemptFlag.values());
        flag.setSelectedItem(parent.logFilter.flag);
        flag.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, flag, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, flag, 5, SpringLayout.SOUTH, flagLabel);
        add(flag);

        sortDirectionAscending.setPreferredSize(new Dimension(150, 20));
        sortDirection.add(sortDirectionAscending);
        layout.putConstraint(SpringLayout.WEST, sortDirectionAscending, 35, SpringLayout.EAST, flagLabel);
        layout.putConstraint(SpringLayout.NORTH, sortDirectionAscending, 10, SpringLayout.SOUTH, area);
        add(sortDirectionAscending);

        sortDirectionDescending.setPreferredSize(new Dimension(150, 20));
        if(parent.logFilter.sortDescending) {
            sortDirectionAscending.setSelected(false);
            sortDirectionDescending.setSelected(true);
        } else {
            sortDirectionAscending.setSelected(true);
            sortDirectionDescending.setSelected(false);
        }
        sortDirection.add(sortDirectionDescending);
        layout.putConstraint(SpringLayout.WEST, sortDirectionDescending, 35, SpringLayout.EAST, flag);
        layout.putConstraint(SpringLayout.NORTH, sortDirectionDescending, 10, SpringLayout.SOUTH,
                sortDirectionAscending);
        add(sortDirectionDescending);

        submitButton.setPreferredSize(new Dimension(200, 20));
        submitButton.addActionListener(e -> {
            parent.logFilter.filterBy = (LogFilter.FilterKey) filterBy.getSelectedItem();
            parent.logFilter.sortBy = (LogFilter.FilterKey) sortBy.getSelectedItem();
            parent.logFilter.startTimestamp = ((Date) dateStart.getValue()).getTime();
            parent.logFilter.endTimestamp = ((Date) dateEnd.getValue()).getTime();
            parent.logFilter.area = (Area) area.getSelectedItem();
            parent.logFilter.employeeId = (int) employee.getValue();
            parent.logFilter.flag = (AttemptFlag) flag.getSelectedItem();
            parent.logFilter.sortDescending = sortDirectionDescending.isSelected();
            parent.updateTables();
            close();
        });
        layout.putConstraint(SpringLayout.WEST, submitButton, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, submitButton, 20, SpringLayout.SOUTH, sortDirectionDescending);
        add(submitButton);
        cancelButton.setPreferredSize(new Dimension(200, 20));
        cancelButton.addActionListener(e -> {
            close();
        });
        layout.putConstraint(SpringLayout.WEST, cancelButton, 5, SpringLayout.EAST, submitButton);
        layout.putConstraint(SpringLayout.NORTH, cancelButton, 20, SpringLayout.SOUTH, sortDirectionDescending);
        add(cancelButton);
    }

    private void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}

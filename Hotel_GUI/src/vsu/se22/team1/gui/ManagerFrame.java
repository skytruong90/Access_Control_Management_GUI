package vsu.se22.team1.gui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;

import vsu.se22.team1.AccessAttempt;
import vsu.se22.team1.Building;
import vsu.se22.team1.Employee;
import vsu.se22.team1.Manager;
import vsu.se22.team1.Persistence;
import vsu.se22.team1.Utils;

public class ManagerFrame extends JFrame {
    private static final FileFilter BLDG_FILTER = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().endsWith(".bldg");
        }

        @Override
        public String getDescription() {
            return "Access System Data (.bldg)";
        }
    };
    private static final JFileChooser FILE_CHOOSER;
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        FILE_CHOOSER = new JFileChooser();
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        FILE_CHOOSER.setFileFilter(BLDG_FILTER);
        FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    private SpringLayout layout = new SpringLayout();
    TreeTable areasTable = new TreeTable(this);
    private JScrollPane areasScroll = new JScrollPane(areasTable);
    private JButton addBuilding = new JButton("Add Building");
    JTable employeesTable = new JTable();
    private JScrollPane employeesScroll = new JScrollPane(employeesTable);
    private JButton addEmployee = new JButton("Add Employee");
    JTable accessHistoryTable = new JTable();
    private JScrollPane accessHistoryScroll = new JScrollPane(accessHistoryTable);
    private JButton addAccessAttempt = new JButton("Log New Access Attempt");
    private JButton filterAccessHistory = new JButton("Filter Log");
    private JButton filterAccessHistoryReset = new JButton("Reset Filter");
    private JMenu fileMenu = new JMenu("File");

    Manager manager;
    private File managerFile;
    LogFilter logFilter = new LogFilter();

    public ManagerFrame() {
        manager = new Manager();
        FILE_CHOOSER.setCurrentDirectory(new File("."));
        setTitle("Access System Manager");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!promptSave("Save before exiting?")) return;
                ManagerFrame.this.setVisible(false);
                ManagerFrame.this.dispose();
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.getContentPane().setLayout(layout);
        addMenuBar();
        addComponents();
        updateTables();
        layout.putConstraint(SpringLayout.EAST, this.getContentPane(), 10, SpringLayout.EAST, accessHistoryScroll);
        layout.putConstraint(SpringLayout.SOUTH, this.getContentPane(), 10, SpringLayout.SOUTH, accessHistoryScroll);
        this.pack();
        setLocationRelativeTo(null);
    }

    public ManagerFrame(File toOpen) {
        this();
        try {
            managerFile = toOpen;
            manager = Persistence.read(managerFile);
            updateTables();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void addComponents() {
        addBuilding.setPreferredSize(new Dimension(400, 30));
        addBuilding.addActionListener(e -> {
            new BuildingEditFrame(ManagerFrame.this, null) {
                @Override
                protected void submit(Object obj) {
                    manager.addBuilding((Building) obj);
                    updateTables();
                }
            }.setVisible(true);
        });
        layout.putConstraint(SpringLayout.WEST, addBuilding, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, addBuilding, 10, SpringLayout.NORTH, this);
        add(addBuilding);
        areasScroll.setPreferredSize(new Dimension(400, 600));
        areasScroll.add(areasTable);
        areasScroll.setViewportView(areasTable);
        areasScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        areasScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areasTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    new AreaContextMenu(ManagerFrame.this, areasTable).show(e.getComponent(), e.getX(), e.getY());
                } else if(e.getClickCount() == 2) {
                    if(areasTable.getSelectedRowCount() == 0) return;
                    areasTable.expand();
                    updateTables();
                }
            }
        });
        layout.putConstraint(SpringLayout.WEST, areasScroll, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, areasScroll, 10, SpringLayout.SOUTH, addBuilding);
        add(areasScroll);

        addEmployee.setPreferredSize(new Dimension(400, 30));
        addEmployee.addActionListener(e -> {
            new EmployeeEditFrame(ManagerFrame.this, null) {
                @Override
                protected void submit(Object obj) {
                    manager.addEmployee((Employee) obj);
                    updateTables();
                }
            }.setVisible(true);
        });
        layout.putConstraint(SpringLayout.WEST, addEmployee, 10, SpringLayout.EAST, addBuilding);
        layout.putConstraint(SpringLayout.NORTH, addEmployee, 10, SpringLayout.NORTH, this);
        add(addEmployee);
        employeesScroll.setPreferredSize(new Dimension(400, 600));
        employeesScroll.add(employeesTable);
        employeesScroll.setViewportView(employeesTable);
        employeesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        employeesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        employeesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    new EmployeeContextMenu(ManagerFrame.this).show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        layout.putConstraint(SpringLayout.WEST, employeesScroll, 10, SpringLayout.EAST, areasScroll);
        layout.putConstraint(SpringLayout.NORTH, employeesScroll, 10, SpringLayout.SOUTH, addEmployee);
        add(employeesScroll);

        addAccessAttempt.setPreferredSize(new Dimension(390, 30));
        addAccessAttempt.addActionListener(e -> {
            new LogAttemptFrame(ManagerFrame.this).setVisible(true);
        });
        layout.putConstraint(SpringLayout.WEST, addAccessAttempt, 10, SpringLayout.EAST, addEmployee);
        layout.putConstraint(SpringLayout.NORTH, addAccessAttempt, 10, SpringLayout.NORTH, this);
        add(addAccessAttempt);
        filterAccessHistory.setPreferredSize(new Dimension(90, 30));
        filterAccessHistory.addActionListener(e -> {
            new LogFilterFrame(ManagerFrame.this).setVisible(true);
        });
        layout.putConstraint(SpringLayout.WEST, filterAccessHistory, 10, SpringLayout.EAST, addAccessAttempt);
        layout.putConstraint(SpringLayout.NORTH, filterAccessHistory, 10, SpringLayout.NORTH, this);
        add(filterAccessHistory);
        filterAccessHistoryReset.setPreferredSize(new Dimension(100, 30));
        filterAccessHistoryReset.addActionListener(e -> {
            logFilter = new LogFilter();
            updateTables();
        });
        layout.putConstraint(SpringLayout.WEST, filterAccessHistoryReset, 10, SpringLayout.EAST, filterAccessHistory);
        layout.putConstraint(SpringLayout.NORTH, filterAccessHistoryReset, 10, SpringLayout.NORTH, this);
        add(filterAccessHistoryReset);
        accessHistoryScroll.setPreferredSize(new Dimension(600, 600));
        accessHistoryScroll.add(accessHistoryTable);
        accessHistoryScroll.setViewportView(accessHistoryTable);
        accessHistoryScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        accessHistoryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        layout.putConstraint(SpringLayout.WEST, accessHistoryScroll, 10, SpringLayout.EAST, employeesScroll);
        layout.putConstraint(SpringLayout.NORTH, accessHistoryScroll, 10, SpringLayout.SOUTH, addAccessAttempt);
        add(accessHistoryScroll);
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(e -> {
            if(promptSave("Save changes before creating a new Access Manager?")) {
                manager = new Manager();
                managerFile = null;
                updateTables();
            }
        });
        fileMenu.add(menuItem);
        menuItem = new JMenuItem("Open");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.addActionListener(e -> {
            if(!promptSave("Save changes before opening another file?")) return;
            try {
                open();
            } catch(IOException e1) {
                JOptionPane.showMessageDialog(null, "An error occurred while opening the file", "Error",
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        fileMenu.add(menuItem);
        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.addActionListener(e -> {
            save();
        });
        fileMenu.add(menuItem);
        fileMenu.addSeparator();
        menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.addActionListener(e -> {
            ManagerFrame.this.dispatchEvent(new WindowEvent(ManagerFrame.this, WindowEvent.WINDOW_CLOSING));
        });
        fileMenu.add(menuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void open() throws IOException {
        int f = FILE_CHOOSER.showOpenDialog(this);
        if(f == JFileChooser.APPROVE_OPTION) {
            managerFile = FILE_CHOOSER.getSelectedFile();
            manager = Persistence.read(managerFile);
            updateTables();
        }
    }

    /**
     * @return True if the operation blocked by the prompt should continue, otherwise false.
     */
    private boolean promptSave(String message) {
        if(manager != null && manager.hasChangedSinceLastIO()) {
            int r = JOptionPane.showConfirmDialog(this, message, "Warning: Unsaved Changed",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if(r == JOptionPane.YES_OPTION) {
                if(!save()) return false;
            } else if(r == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }

    private boolean save() {
        if(managerFile != null) {
            return save(managerFile);
        }
        int f = FILE_CHOOSER.showSaveDialog(this);
        if(f == JFileChooser.APPROVE_OPTION) {
            if(save(FILE_CHOOSER.getSelectedFile())) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to save file", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean save(File out) {
        if(!out.getAbsolutePath().endsWith(".bldg")) {
            out = new File(out.getAbsolutePath() + ".bldg");
        }
        if(out.isDirectory()) return false;
        managerFile = out;
        try {
            Persistence.write(manager, out);
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    void updateTables() {
        List<AccessAttempt> history = new ArrayList<>();
        switch(logFilter.filterBy) {
        case AREA:
            history.addAll(manager.accessManager.getHistory(logFilter.area));
            break;
        case EMPLOYEE:
            history.addAll(manager.accessManager.getHistory(logFilter.employeeId));
            break;
        case FLAG:
            history.addAll(manager.accessManager.getHistory(logFilter.flag));
            break;
        case TIME:
            history.addAll(manager.accessManager.getHistoryBetween(logFilter.getStartTimestamp(),
                    logFilter.getEndTimestamp()));
            break;
        default:
            history.addAll(manager.accessManager.getHistory());
            break;
        }
        switch(logFilter.sortBy) {
        case AREA:
            history.sort(new Comparator<AccessAttempt>() {
                @Override
                public int compare(AccessAttempt o1, AccessAttempt o2) {
                    return o1.area.toString().compareTo(o2.area.toString());
                }
            });
            break;
        case EMPLOYEE:
            history.sort(new Comparator<AccessAttempt>() {
                @Override
                public int compare(AccessAttempt o1, AccessAttempt o2) {
                    return Integer.valueOf(o1.employeeId).compareTo(o2.employeeId);
                }
            });
            break;
        case FLAG:
            history.sort(new Comparator<AccessAttempt>() {
                @Override
                public int compare(AccessAttempt o1, AccessAttempt o2) {
                    return Integer.valueOf(o1.flag.code).compareTo(o2.flag.code);
                }
            });
            break;
        case NONE:
            history.sort(new Comparator<AccessAttempt>() {
                @Override
                public int compare(AccessAttempt o1, AccessAttempt o2) {
                    return Long.valueOf(o1.timestamp).compareTo(o2.timestamp);
                }
            });
            break;
        default:
            history.sort(new Comparator<AccessAttempt>() {
                @Override
                public int compare(AccessAttempt o1, AccessAttempt o2) {
                    return Long.valueOf(o1.timestamp).compareTo(o2.timestamp);
                }
            });
        }
        if(logFilter.sortDescending) {
            Collections.reverse(history);
        }
        areasTable.updateModel();
        employeesTable.setModel(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return manager == null ? 0 : manager.employees.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if(manager == null) return null;
                switch(columnIndex) {
                case 0:
                    return manager.employees.get(rowIndex).name;
                case 1:
                    return manager.employees.get(rowIndex).id;
                default:
                    return "?";
                }
            }
        });
        employeesTable.getColumnModel().getColumn(0).setHeaderValue("Name");
        employeesTable.getColumnModel().getColumn(1).setHeaderValue("ID");
        accessHistoryTable.setModel(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                if(manager == null) {
                    return 0;
                } else {
                    return history.size();
                }
            }

            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if(manager == null) return null;
                switch(columnIndex) {
                case 0:
                    return Utils.DATE_FORMAT.format(history.get(rowIndex).timestamp);
                case 1:
                    return history.get(rowIndex).area.toString();
                case 2:
                    return history.get(rowIndex).employeeId;
                case 3:
                    return history.get(rowIndex).flag;
                default:
                    return "?";
                }
            }
        });
        accessHistoryTable.getColumnModel().getColumn(0).setHeaderValue("Timestamp");
        accessHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(130);
        accessHistoryTable.getColumnModel().getColumn(1).setHeaderValue("Area");
        accessHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        accessHistoryTable.getColumnModel().getColumn(2).setHeaderValue("Employee ID");
        accessHistoryTable.getColumnModel().getColumn(3).setHeaderValue("Flag");
    }
}

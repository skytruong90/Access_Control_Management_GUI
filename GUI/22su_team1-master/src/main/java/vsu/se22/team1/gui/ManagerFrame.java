package vsu.se22.team1.gui;

import java.awt.Desktop;
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
import vsu.se22.team1.Config;
import vsu.se22.team1.Employee;
import vsu.se22.team1.I18n;
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
            return I18n.get("filechooser.filter.bldg");
        }
    };
    private static final FileFilter HTML_FILTER = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().endsWith(".html");
        }

        @Override
        public String getDescription() {
            return I18n.get("filechooser.filter.html");
        }
    };
    private static final FileFilter TXT_FILTER = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().endsWith(".txt");
        }

        @Override
        public String getDescription() {
            return I18n.get("filechooser.filter.txt");
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
        FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    private SpringLayout layout = new SpringLayout();
    TreeTable areasTable = new TreeTable(this);
    private JScrollPane areasScroll = new JScrollPane(areasTable);
    private JButton addBuilding = new JButton();
    JTable employeesTable = new JTable();
    private JScrollPane employeesScroll = new JScrollPane(employeesTable);
    private JButton addEmployee = new JButton();
    JTable accessHistoryTable = new JTable();
    private JScrollPane accessHistoryScroll = new JScrollPane(accessHistoryTable);
    private JButton addAccessAttempt = new JButton();
    private JButton filterAccessHistory = new JButton();
    private JButton filterAccessHistoryReset = new JButton();
    private JMenu fileMenu = new JMenu();
    private JMenuItem menuItemNew = new JMenuItem();
    private JMenuItem menuItemOpen = new JMenuItem();
    private JMenuItem menuItemSave = new JMenuItem();
    private JMenuItem menuItemExit = new JMenuItem();
    private JMenu reportsMenu = new JMenu();
    private JMenu accessHistoryMenu = new JMenu();
    private JMenuItem menuItemLogPlainText = new JMenuItem();
    private JMenuItem menuItemLogHTML = new JMenuItem();
    private JMenu employeeMenu = new JMenu();
    private JMenuItem menuItemEmployeePlainText = new JMenuItem();
    private JMenuItem menuItemEmployeeHTML = new JMenuItem();
    private JMenu settingsMenu = new JMenu();
    private JMenu languageMenu = new JMenu();
    private JMenuItem menuItemDefaultLocale = new JMenuItem();
    private JMenuItem menuItemOpenConfigFile = new JMenuItem();

    Manager manager;
    private File managerFile;
    LogFilter logFilter = new LogFilter();

    public ManagerFrame() {
        manager = new Manager();
        FILE_CHOOSER.setCurrentDirectory(new File("."));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!promptSave(I18n.get("optionpane.save.prompt.exit"))) return;
                ManagerFrame.this.setVisible(false);
                ManagerFrame.this.dispose();
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.getContentPane().setLayout(layout);
        addMenuBar();
        addComponents();
        updateStrings();
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
        menuItemNew.setMnemonic(KeyEvent.VK_N);
        menuItemNew.addActionListener(e -> {
            if(promptSave(I18n.get("optionpane.save.prompt.new"))) {
                manager = new Manager();
                managerFile = null;
                updateTables();
            }
        });
        fileMenu.add(menuItemNew);
        menuItemOpen.setMnemonic(KeyEvent.VK_O);
        menuItemOpen.addActionListener(e -> {
            if(!promptSave(I18n.get("optionpane.save.prompt.open"))) return;
            try {
                open();
            } catch(IOException e1) {
                JOptionPane.showMessageDialog(null, I18n.get("optionpane.open.failed"), I18n.get("application.error"),
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        fileMenu.add(menuItemOpen);
        menuItemSave.setMnemonic(KeyEvent.VK_S);
        menuItemSave.addActionListener(e -> {
            save();
        });
        fileMenu.add(menuItemSave);
        fileMenu.addSeparator();
        menuItemExit.setMnemonic(KeyEvent.VK_X);
        menuItemExit.addActionListener(e -> {
            ManagerFrame.this.dispatchEvent(new WindowEvent(ManagerFrame.this, WindowEvent.WINDOW_CLOSING));
        });
        fileMenu.add(menuItemExit);
        menuBar.add(fileMenu);

        reportsMenu.setMnemonic(KeyEvent.VK_R);
        accessHistoryMenu.setMnemonic(KeyEvent.VK_H);
        menuItemLogPlainText.setMnemonic(KeyEvent.VK_T);
        menuItemLogPlainText.addActionListener(e -> {
            FILE_CHOOSER.setFileFilter(TXT_FILTER);
            if(FILE_CHOOSER.showSaveDialog(ManagerFrame.this) != JFileChooser.APPROVE_OPTION) return;
            File out = FILE_CHOOSER.getSelectedFile();
            if(!out.getAbsolutePath().endsWith(".txt")) {
                out = new File(out.getAbsolutePath() + ".txt");
            }
            if(out.exists()) {
                if(JOptionPane.showConfirmDialog(ManagerFrame.this, I18n.get("optionpane.overwrite"),
                        I18n.get("optionpane.overwrite.title"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
                    return;
            }
            try {
                Report.accessPlain(out, getAccessLog());
            } catch(IOException ex) {
                JOptionPane.showMessageDialog(null, I18n.get("optionpane.save.failed"), I18n.get("application.error"),
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        accessHistoryMenu.add(menuItemLogPlainText);
        menuItemLogHTML.setMnemonic(KeyEvent.VK_H);
        menuItemLogHTML.addActionListener(e -> {
            FILE_CHOOSER.setFileFilter(HTML_FILTER);
            if(FILE_CHOOSER.showSaveDialog(ManagerFrame.this) != JFileChooser.APPROVE_OPTION) return;
            File out = FILE_CHOOSER.getSelectedFile();
            if(!out.getAbsolutePath().endsWith(".html")) {
                out = new File(out.getAbsolutePath() + ".html");
            }
            if(out.exists()) {
                if(JOptionPane.showConfirmDialog(ManagerFrame.this, I18n.get("optionpane.overwrite"),
                        I18n.get("optionpane.overwrite.title"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
                    return;
            }
            try {
                Report.accessHTML(out, getAccessLog());
            } catch(IOException ex) {
                JOptionPane.showMessageDialog(null, I18n.get("optionpane.save.failed"), I18n.get("application.error"),
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        accessHistoryMenu.add(menuItemLogHTML);
        reportsMenu.add(accessHistoryMenu);
        employeeMenu.setMnemonic(KeyEvent.VK_E);
        menuItemEmployeePlainText.setMnemonic(KeyEvent.VK_T);
        menuItemEmployeePlainText.addActionListener(e -> {
            FILE_CHOOSER.setFileFilter(TXT_FILTER);
            if(FILE_CHOOSER.showSaveDialog(ManagerFrame.this) != JFileChooser.APPROVE_OPTION) return;
            File out = FILE_CHOOSER.getSelectedFile();
            if(!out.getAbsolutePath().endsWith(".txt")) {
                out = new File(out.getAbsolutePath() + ".txt");
            }
            if(out.exists()) {
                if(JOptionPane.showConfirmDialog(ManagerFrame.this, I18n.get("optionpane.overwrite"),
                        I18n.get("optionpane.overwrite.title"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
                    return;
            }
            try {
                Report.employeesPlain(out, manager.employees);
            } catch(IOException ex) {
                JOptionPane.showMessageDialog(null, I18n.get("optionpane.save.failed"), I18n.get("application.error"),
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        employeeMenu.add(menuItemEmployeePlainText);
        menuItemEmployeeHTML.setMnemonic(KeyEvent.VK_H);
        menuItemEmployeeHTML.addActionListener(e -> {
            FILE_CHOOSER.setFileFilter(HTML_FILTER);
            if(FILE_CHOOSER.showSaveDialog(ManagerFrame.this) != JFileChooser.APPROVE_OPTION) return;
            File out = FILE_CHOOSER.getSelectedFile();
            if(!out.getAbsolutePath().endsWith(".html")) {
                out = new File(out.getAbsolutePath() + ".html");
            }
            if(out.exists()) {
                if(JOptionPane.showConfirmDialog(ManagerFrame.this, I18n.get("optionpane.overwrite"),
                        I18n.get("optionpane.overwrite.title"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
                    return;
            }
            try {
                Report.employeesHTML(out, manager.employees);
            } catch(IOException ex) {
                JOptionPane.showMessageDialog(null, I18n.get("optionpane.save.faild"), I18n.get("application.error"),
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        employeeMenu.add(menuItemEmployeeHTML);
        reportsMenu.add(employeeMenu);
        menuBar.add(reportsMenu);

        settingsMenu.setMnemonic(KeyEvent.VK_S);
        menuItemDefaultLocale.setMnemonic(KeyEvent.VK_D);
        menuItemDefaultLocale.addActionListener(e -> {
            I18n.useSystemLocale();
        });
        languageMenu.add(menuItemDefaultLocale);
        for(final String locale : I18n.getSupportedLocales()) {
            JMenuItem menuItem = new JMenuItem(I18n.getLocalName(locale));
            menuItem.addActionListener(e -> {
                I18n.setCurrentLocale(locale);
            });
            languageMenu.add(menuItem);
        }
        settingsMenu.add(languageMenu);
        menuItemOpenConfigFile.setMnemonic(KeyEvent.VK_C);
        menuItemOpenConfigFile.addActionListener(e -> {
            try {
                Desktop.getDesktop().edit(Config.CONFIG_FILE);
            } catch(IOException e1) {
                e1.printStackTrace();
            }
        });
        settingsMenu.add(menuItemOpenConfigFile);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);
    }

    private void open() throws IOException {
        FILE_CHOOSER.setFileFilter(BLDG_FILTER);
        if(FILE_CHOOSER.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
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
            int r = JOptionPane.showConfirmDialog(this, message, I18n.get("optionpane.warn.unsaved"),
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
        if(FILE_CHOOSER.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if(save(FILE_CHOOSER.getSelectedFile())) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, I18n.get("optionpane.save.failed"), I18n.get("application.error"),
                        JOptionPane.ERROR_MESSAGE);
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
        employeesTable.getColumnModel().getColumn(0).setHeaderValue(I18n.get("table.employees.name"));
        employeesTable.getColumnModel().getColumn(1).setHeaderValue(I18n.get("table.employees.id"));
        List<AccessAttempt> history = getAccessLog();
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
        accessHistoryTable.getColumnModel().getColumn(0).setHeaderValue(I18n.get("table.access-history.timestamp"));
        accessHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(130);
        accessHistoryTable.getColumnModel().getColumn(1).setHeaderValue(I18n.get("table.access-history.area"));
        accessHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        accessHistoryTable.getColumnModel().getColumn(2).setHeaderValue(I18n.get("table.access-history.employee-id"));
        accessHistoryTable.getColumnModel().getColumn(3).setHeaderValue(I18n.get("table.access-history.flag"));
    }

    public void updateStrings() {
        setTitle(I18n.get("application.name"));
        addBuilding.setText(I18n.get("button.add-building"));
        addEmployee.setText(I18n.get("button.add-employee"));
        addAccessAttempt.setText(I18n.get("button.add-access-attempt"));
        filterAccessHistory.setText(I18n.get("button.filter-log"));
        filterAccessHistoryReset.setText(I18n.get("button.reset-filter"));
        fileMenu.setText(I18n.get("menu.file"));
        reportsMenu.setText(I18n.get("menu.reports"));
        settingsMenu.setText(I18n.get("menu.settings"));
        menuItemNew.setText(I18n.get("menu.file.new"));
        menuItemOpen.setText(I18n.get("menu.file.open"));
        menuItemSave.setText(I18n.get("menu.file.save"));
        menuItemExit.setText(I18n.get("menu.file.exit"));
        accessHistoryMenu.setText(I18n.get("menu.reports.access-history"));
        menuItemLogPlainText.setText(I18n.get("menu.reports.plain-text"));
        menuItemLogHTML.setText(I18n.get("menu.reports.html"));
        employeeMenu.setText(I18n.get("menu.reports.employee"));
        menuItemEmployeePlainText.setText(I18n.get("menu.reports.plain-text"));
        menuItemEmployeeHTML.setText(I18n.get("menu.reports.html"));
        languageMenu.setText(I18n.get("menu.settings.language"));
        menuItemDefaultLocale.setText(I18n.get("menu.settings.language.default"));
        menuItemOpenConfigFile.setText(I18n.get("menu.settings.open-config-file"));
        updateTables();
    }

    private List<AccessAttempt> getAccessLog() {
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
        return history;
    }
}

package vsu.se22.team1.gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import vsu.se22.team1.Area;
import vsu.se22.team1.Building;
import vsu.se22.team1.Room;
import vsu.se22.team1.Suite;

public class AreaContextMenu extends JPopupMenu {
    public AreaContextMenu(ManagerFrame parent, TreeTable table) {
        if(parent.areasTable.getSelectedRowCount() == 0) {
            return;
        }
        Area selected = table.areas.get(parent.areasTable.getSelectedRow());
        boolean expanded = table.isExpanded(selected);
        JMenuItem expand = new JMenuItem(expanded ? "Collapse" : "Expand");
        expand.addActionListener(e -> {
            table.expand(selected);
        });
        add(expand);
        add(new JSeparator());
        JMenuItem edit = new JMenuItem("Edit");
        edit.addActionListener(e -> {
            if(selected instanceof Building) {
                new BuildingEditFrame(parent, (Building) selected) {
                    @Override
                    protected void submit(Object obj) {
                        Building edited = (Building) obj;
                        selected.name = edited.name;
                        selected.code = edited.code;
                        parent.updateTables();
                    }
                }.setVisible(true);
            } else if(selected instanceof Suite) {
                new SuiteEditFrame(parent, (Suite) selected) {
                    @Override
                    protected void submit(Object obj) {
                        Suite edited = (Suite) obj;
                        selected.name = edited.name;
                        selected.code = edited.code;
                        parent.updateTables();
                    }
                }.setVisible(true);
            } else if(selected instanceof Room) {
                new RoomEditFrame(parent, (Room) selected) {
                    @Override
                    protected void submit(Object obj) {
                        selected.code = ((Room) obj).code;
                        parent.updateTables();
                    }
                }.setVisible(true);
            }
        });
        add(edit);
        if(selected instanceof Building || selected instanceof Suite) {
            JMenuItem add = new JMenuItem();
            if(selected instanceof Building) {
                add.setText("Add Suite");
                add.addActionListener(e -> {
                    new SuiteEditFrame(parent, null) {
                        @Override
                        protected void submit(Object obj) {
                            ((Building) selected).addSuite((Suite) obj);
                            parent.updateTables();
                        }
                    }.setVisible(true);
                });
            } else if(selected instanceof Suite) {
                add.setText("Add Room");
                add.addActionListener(e -> {
                    new RoomEditFrame(parent, null) {
                        @Override
                        protected void submit(Object obj) {
                            ((Suite) selected).addRoom((Room) obj);
                            parent.updateTables();
                        }
                    }.setVisible(true);
                });
            }
            add(add);
        }
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(e -> {
            parent.manager.removeArea(selected);
            parent.updateTables();
        });
        add(delete);
    }
}

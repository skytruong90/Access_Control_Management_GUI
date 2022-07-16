package vsu.se22.team1.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import vsu.se22.team1.Area;
import vsu.se22.team1.Building;
import vsu.se22.team1.Room;
import vsu.se22.team1.Suite;

public class TreeTable extends JTable {
    private ManagerFrame parent;
    private Map<Area, Boolean> expanded = new HashMap<>();
    List<Area> areas = new ArrayList<>();

    public TreeTable(ManagerFrame parent) {
        this.parent = parent;
    }

    public void expand(Area a) {
        expanded.put(a, !expanded.getOrDefault(a, false));
        updateModel();
    }

    public void expand() {
        expand(areas.get(getSelectedRow()));
    }

    public boolean isExpanded(Area area) {
        return expanded.getOrDefault(area, false);
    }

    public void updateModel() {
        int rowCount = 0;
        areas.clear();
        for(Building b : parent.manager.buildings) {
            areas.add(b);
            rowCount++;
            if(expanded.getOrDefault(b, false)) {
                rowCount += b.getSuites().size();
                for(Suite s : b.getSuites()) {
                    areas.add(s);
                    if(expanded.getOrDefault(s, false)) {
                        rowCount += s.getRooms().size();
                        areas.addAll(s.getRooms());
                    }
                }
            }
        }
        final int finalRowCount = rowCount;
        setModel(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return finalRowCount;
            }

            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if(areas.size() == 0) return "?";
                Area a = areas.get(rowIndex);
                switch(columnIndex) {
                case 0:
                    if(a instanceof Building) {
                        return "B";
                    } else {
                        return "";
                    }
                case 1:
                    if(a instanceof Building) {
                        if(((Building) a).getSuites().size() == 0) {
                            return "";
                        } else {
                            return expanded.getOrDefault(a, false) ? "\u2796" : "\u2795";
                        }
                    } else if(a instanceof Suite) {
                        return "S";
                    } else {
                        return "";
                    }
                case 2:
                    if(a instanceof Suite) {
                        if(((Suite) a).getRooms().size() == 0) {
                            return "";
                        } else {
                            return expanded.getOrDefault(a, false) ? "\u2796" : "\u2795";
                        }
                    } else if(a instanceof Room) {
                        return "R";
                    } else {
                        return "";
                    }
                case 3:
                    return a.name;
                case 4:
                    return a.code;
                default:
                    return "?";
                }
            }
        });
        getColumnModel().getColumn(0).setMaxWidth(20);
        getColumnModel().getColumn(0).setHeaderValue("");
        getColumnModel().getColumn(1).setMaxWidth(20);
        getColumnModel().getColumn(1).setHeaderValue("");
        getColumnModel().getColumn(2).setMaxWidth(20);
        getColumnModel().getColumn(2).setHeaderValue("");
        getColumnModel().getColumn(3).setHeaderValue("Name");
        getColumnModel().getColumn(4).setHeaderValue("Code");
    }
}

package vsu.se22.team1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Persistence {
    public static byte[] serialize(Manager manager) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buildings = getBuildingBytes(manager);
        byte[] employees = getEmployeeBytes(manager);
        byte[] access = getAccessBytes(manager);
        out.writeBytes(Utils.intToBytes(buildings.length));
        out.writeBytes(Utils.intToBytes(employees.length));
        out.writeBytes(Utils.intToBytes(access.length));
        out.writeBytes(buildings);
        out.writeBytes(employees);
        out.writeBytes(access);
        return out.toByteArray();
    }

    private static byte[] getBuildingBytes(Manager m) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(Utils.intToBytes(m.buildings.size()));
        for(Building b : m.buildings) {
            out.writeBytes(Utils.uuidToBytes(b.uuid));
            out.writeBytes(Utils.intToBytes(b.code));
            byte[] name = b.name.getBytes();
            out.writeBytes(Utils.intToBytes(name.length));
            out.writeBytes(name);
            out.writeBytes(Utils.intToBytes(b.getSuites().size()));
            for(Suite s : b.getSuites()) {
                out.writeBytes(Utils.uuidToBytes(s.uuid));
                out.writeBytes(Utils.intToBytes(s.code));
                byte[] suiteName = s.name.getBytes();
                out.writeBytes(Utils.intToBytes(suiteName.length));
                out.writeBytes(suiteName);
                out.writeBytes(Utils.intToBytes(s.getRooms().size()));
                for(Room r : s.getRooms()) {
                    out.writeBytes(Utils.uuidToBytes(r.uuid));
                    out.writeBytes(Utils.intToBytes(r.code));
                }
            }
        }
        return out.toByteArray();
    }

    private static byte[] getEmployeeBytes(Manager m) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(Utils.intToBytes(m.employees.size()));
        for(Employee e : m.employees) {
            out.writeBytes(Utils.uuidToBytes(e.uuid));
            out.writeBytes(Utils.intToBytes(e.id));
            byte[] name = e.name.getBytes();
            out.writeBytes(Utils.intToBytes(name.length));
            out.writeBytes(name);
            out.writeBytes(Utils.intToBytes(e.access.size()));
            for(Area a : e.access.keySet()) {
                out.writeBytes(Utils.uuidToBytes(a.uuid));
                out.writeBytes(new byte[] { (byte) (e.access.get(a) ? 0x01 : 0x00) });
            }
        }
        return out.toByteArray();
    }

    private static byte[] getAccessBytes(Manager m) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(Utils.intToBytes(m.accessManager.getHistory().size()));
        for(AccessAttempt a : m.accessManager.getHistory()) {
            out.writeBytes(Utils.longToBytes(a.timestamp));
            out.writeBytes(Utils.uuidToBytes(a.area.uuid));
            out.writeBytes(Utils.intToBytes(a.employeeId));
            out.writeBytes(Utils.intToBytes(a.flag.code));
        }
        return out.toByteArray();
    }

    public static Manager deserialize(File file) throws IOException {
        return deserialize(Files.readAllBytes(file.toPath()));
    }

    public static Manager deserialize(byte[] bytes) {
        byte[] buildingsBytes = new byte[Utils.bytesToInt(Arrays.copyOfRange(bytes, 0, Integer.BYTES))];
        System.arraycopy(bytes, Integer.BYTES * 3, buildingsBytes, 0, buildingsBytes.length);
        int buildingCount = Utils.bytesToInt(Arrays.copyOfRange(buildingsBytes, 0, Integer.BYTES));
        List<Building> buildings = new ArrayList<>(buildingCount);
        int o = Integer.BYTES;
        for(int i = 0; i < buildingCount; i++) {
            UUID uuid = Utils.uuidFromBytes(Arrays.copyOfRange(buildingsBytes, o, o += Utils.UUID_BYTES));
            int code = Utils.bytesToInt(Arrays.copyOfRange(buildingsBytes, o, o += Integer.BYTES));
            int nameLength = Utils.bytesToInt(Arrays.copyOfRange(buildingsBytes, o, o += Integer.BYTES));
            String name = new String(Arrays.copyOfRange(buildingsBytes, o, o += nameLength));
            Building b = new Building(uuid, name, code);
            buildings.add(b);
            int suiteCount = Utils.bytesToInt(Arrays.copyOfRange(buildingsBytes, o, o += Integer.BYTES));
            for(int j = 0; j < suiteCount; j++) {
                UUID suiteUuid = Utils.uuidFromBytes(Arrays.copyOfRange(buildingsBytes, o, o += Utils.UUID_BYTES));
                int suiteCode = Utils.bytesToInt(Arrays.copyOfRange(buildingsBytes, o, o += Integer.BYTES));
                int suiteNameLength = Utils.bytesToInt(Arrays.copyOfRange(buildingsBytes, o, o += Integer.BYTES));
                String suiteName = new String(Arrays.copyOfRange(buildingsBytes, o, o += suiteNameLength));
                Suite s = new Suite(suiteUuid, suiteName, suiteCode);
                b.addSuite(s);
                int roomCount = Utils.bytesToInt(Arrays.copyOfRange(buildingsBytes, o, o += Integer.BYTES));
                for(int k = 0; k < roomCount; k++) {
                    UUID roomUuid = Utils.uuidFromBytes(Arrays.copyOfRange(buildingsBytes, o, o += Utils.UUID_BYTES));
                    int roomNumber = Utils.bytesToInt(Arrays.copyOfRange(buildingsBytes, o, o += Integer.BYTES));
                    s.addRoom(new Room(roomUuid, roomNumber));
                }
            }
        }

        byte[] employeesBytes = new byte[Utils.bytesToInt(Arrays.copyOfRange(bytes, Integer.BYTES, Integer.BYTES * 2))];
        System.arraycopy(bytes, Integer.BYTES * 3 + buildingsBytes.length, employeesBytes, 0, employeesBytes.length);
        int employeeCount = Utils.bytesToInt(Arrays.copyOfRange(employeesBytes, 0, Integer.BYTES));
        List<Employee> employees = new ArrayList<>(employeeCount);
        o = Integer.BYTES;
        for(int i = 0; i < employeeCount; i++) {
            UUID uuid = Utils.uuidFromBytes(Arrays.copyOfRange(employeesBytes, o, o += Utils.UUID_BYTES));
            int id = Utils.bytesToInt(Arrays.copyOfRange(employeesBytes, o, o += Integer.BYTES));
            int nameLength = Utils.bytesToInt(Arrays.copyOfRange(employeesBytes, o, o += Integer.BYTES));
            String name = new String(Arrays.copyOfRange(employeesBytes, o, o += nameLength));
            Employee e = new Employee(uuid, name, id);
            employees.add(e);
            int accessCount = Utils.bytesToInt(Arrays.copyOfRange(employeesBytes, o, o += Integer.BYTES));
            for(int j = 0; j < accessCount; j++) {
                UUID areaUuid = Utils.uuidFromBytes(Arrays.copyOfRange(employeesBytes, o, o += Utils.UUID_BYTES));
                boolean state = employeesBytes[o++] == 0x01 ? true : false;
                Area area = null;
                for(Building b : buildings) {
                    if(b.uuid.equals(areaUuid)) {
                        area = b;
                        break;
                    }
                    for(Suite s : b.getSuites()) {
                        if(s.uuid.equals(areaUuid)) {
                            area = s;
                            break;
                        }
                        for(Room r : b.getRooms()) {
                            if(r.uuid.equals(areaUuid)) {
                                area = r;
                                break;
                            }
                        }
                    }
                }
                e.setAccessState(area, state);
            }
        }

        byte[] accessBytes = new byte[Utils
                .bytesToInt(Arrays.copyOfRange(bytes, Integer.BYTES * 2, Integer.BYTES * 3))];
        System.arraycopy(bytes, Integer.BYTES * 3 + buildingsBytes.length + employeesBytes.length, accessBytes, 0,
                accessBytes.length);
        int attemptCount = Utils.bytesToInt(Arrays.copyOfRange(accessBytes, 0, Integer.BYTES));
        List<AccessAttempt> accessHistory = new ArrayList<>();
        o = Integer.BYTES;
        for(int i = 0; i < attemptCount; i++) {
            long timestamp = Utils.bytesToLong(Arrays.copyOfRange(accessBytes, o, o += Long.BYTES));
            UUID areaUuid = Utils.uuidFromBytes(Arrays.copyOfRange(accessBytes, o, o += Utils.UUID_BYTES));
            int employeeId = Utils.bytesToInt(Arrays.copyOfRange(accessBytes, o, o += Integer.BYTES));
            int flag = Utils.bytesToInt(Arrays.copyOfRange(accessBytes, o, o += Integer.BYTES));
            Area area = null;
            for(Building b : buildings) {
                if(b.uuid.equals(areaUuid)) {
                    area = b;
                    break;
                }
                for(Suite s : b.getSuites()) {
                    if(s.uuid.equals(areaUuid)) {
                        area = s;
                        break;
                    }
                    for(Room r : b.getRooms()) {
                        if(r.uuid.equals(areaUuid)) {
                            area = r;
                            break;
                        }
                    }
                }
            }
            accessHistory.add(new AccessAttempt(timestamp, area, employeeId, AttemptFlag.fromFlag(flag)));
        }
        Manager m = new Manager(buildings, employees);
        m.accessManager.addAll(accessHistory);
        return m;
    }
}

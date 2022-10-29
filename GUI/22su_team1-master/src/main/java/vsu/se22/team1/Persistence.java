package vsu.se22.team1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This class contains methods for reading and writing Manager instances.
 * @author David Tan
 */
public final class Persistence {
    /**
     * Arbitrary magic number, used to determine if a given file was
     * created by {@link #serialize(Manager)}.
     */
    private static final int MAGIC_NUMBER = 0xFEBABEFA;
    private static final byte[] NULL_BYTE = { (byte) 0x00 };
    private static final byte[] TRUE_BYTE = { (byte) 0x01 };

    public static byte[] serialize(Manager manager) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(Utils.intToBytes(MAGIC_NUMBER));
        byte[] buildings = getBuildingBytes(manager);
        byte[] employees = getEmployeeBytes(manager);
        byte[] access = getAccessBytes(manager);
        out.writeBytes(Utils.intToBytes(buildings.length));
        out.writeBytes(buildings);
        out.writeBytes(Utils.intToBytes(employees.length));
        out.writeBytes(employees);
        out.writeBytes(Utils.intToBytes(access.length));
        out.writeBytes(access);
        return out.toByteArray();
    }

    private static byte[] getBuildingBytes(Manager m) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(Utils.intToBytes(m.buildings.size()));
        for(Building b : m.buildings) {
            out.writeBytes(Utils.uuidToBytes(b.uuid));
            out.writeBytes(Utils.intToBytes(b.code));
            out.writeBytes(b.name.getBytes());
            out.writeBytes(NULL_BYTE);
            out.writeBytes(Utils.intToBytes(b.getSuites().size()));
            for(Suite s : b.getSuites()) {
                out.writeBytes(Utils.uuidToBytes(s.uuid));
                out.writeBytes(Utils.intToBytes(s.code));
                out.writeBytes(s.name.getBytes());
                out.writeBytes(NULL_BYTE);
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
            out.writeBytes(e.name.getBytes());
            out.writeBytes(NULL_BYTE);
            out.writeBytes(Utils.intToBytes(e.access.size()));
            for(Area a : e.access.keySet()) {
                out.writeBytes(Utils.uuidToBytes(a.uuid));
                if(e.hasAccess(a)) {
                    out.writeBytes(TRUE_BYTE);
                } else {
                    out.writeBytes(NULL_BYTE);
                }
            }
        }
        return out.toByteArray();
    }

    private static byte[] getAccessBytes(Manager m) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(Utils.intToBytes(m.accessManager.getHistory().size()));
        for(AccessAttempt a : m.accessManager.getHistory()) {
            out.writeBytes(Utils.longToBytes(a.timestamp));
            out.writeBytes(Utils.intToBytes(a.employeeId));
            out.writeBytes(Utils.intToBytes(a.flag.code));
            out.writeBytes(Utils.uuidToBytes(a.area.uuid));
        }
        return out.toByteArray();
    }

    public static Manager deserialize(File file) throws IOException {
        return deserialize(Files.readAllBytes(file.toPath()));
    }

    public static Manager deserialize(byte[] bytes) throws IOException {
        // Check if the first four bytes of the file are the file format's magic number,
        // and throw an exception if not
        if(bytes.length <= 4 || Utils.bytesToInt(bytes, 0, Integer.BYTES) != MAGIC_NUMBER) {
            throw new IOException("Invalid input file");
        }
        List<Building> buildings = new ArrayList<Building>();
        byte[] buildingBytes = new byte[Utils.bytesToInt(bytes, Integer.BYTES, Integer.BYTES * 2) + Integer.BYTES];
        System.arraycopy(bytes, Integer.BYTES, buildingBytes, 0, buildingBytes.length);
        int o = Integer.BYTES;
        int buildingCount = Utils.bytesToInt(buildingBytes, o, o += Integer.BYTES);
        for(int b = 0; b < buildingCount; b++) {
            UUID buildingUuid = Utils.uuidFromBytes(buildingBytes, o, o += Utils.UUID_BYTES);
            int buildingCode = Utils.bytesToInt(buildingBytes, o, o += Integer.BYTES);
            int stringOffset = 0;
            // Strings are zero-terminated
            while(buildingBytes[o + stringOffset] != NULL_BYTE[0]) {
                stringOffset++;
            }
            String buildingName = new String(Arrays.copyOfRange(buildingBytes, o, o += stringOffset));
            o++;
            Building building = new Building(buildingUuid, buildingName, buildingCode);
            buildings.add(building);
            int suiteCount = Utils.bytesToInt(buildingBytes, o, o += Integer.BYTES);
            for(int s = 0; s < suiteCount; s++) {
                UUID suiteUuid = Utils.uuidFromBytes(buildingBytes, o, o += Utils.UUID_BYTES);
                int suiteCode = Utils.bytesToInt(buildingBytes, o, o += Integer.BYTES);
                stringOffset = 0;
                while(buildingBytes[o + stringOffset] != NULL_BYTE[0]) {
                    stringOffset++;
                }
                String suiteName = new String(Arrays.copyOfRange(buildingBytes, o, o += stringOffset));
                o++;
                Suite suite = new Suite(suiteUuid, suiteName, suiteCode);
                building.addSuite(suite);
                int roomCount = Utils.bytesToInt(buildingBytes, o, o += Integer.BYTES);
                for(int r = 0; r < roomCount; r++) {
                    UUID roomUuid = Utils.uuidFromBytes(buildingBytes, o, o += Utils.UUID_BYTES);
                    int roomCode = Utils.bytesToInt(buildingBytes, o, o += Integer.BYTES);
                    suite.addRoom(new Room(roomUuid, roomCode));
                }
            }
        }

        List<Employee> employees = new ArrayList<>();
        byte[] employeeBytes = new byte[Utils.bytesToInt(bytes, Integer.BYTES + buildingBytes.length,
                Integer.BYTES * 2 + buildingBytes.length) + Integer.BYTES];
        System.arraycopy(bytes, Integer.BYTES + buildingBytes.length, employeeBytes, 0, employeeBytes.length);
        o = Integer.BYTES;
        int employeeCount = Utils.bytesToInt(employeeBytes, o, o += Integer.BYTES);
        for(int e = 0; e < employeeCount; e++) {
            UUID employeeUuid = Utils.uuidFromBytes(employeeBytes, o, o += Utils.UUID_BYTES);
            int employeeId = Utils.bytesToInt(employeeBytes, o, o += Integer.BYTES);
            int stringOffset = 0;
            while(employeeBytes[o + stringOffset] != NULL_BYTE[0]) {
                stringOffset++;
            }
            String employeeName = new String(Arrays.copyOfRange(employeeBytes, o, o += stringOffset));
            o++;
            Employee employee = new Employee(employeeUuid, employeeName, employeeId);
            employees.add(employee);
            int accessSize = Utils.bytesToInt(employeeBytes, o, o += Integer.BYTES);
            for(int a = 0; a < accessSize; a++) {
                UUID areaUuid = Utils.uuidFromBytes(employeeBytes, o, o += Utils.UUID_BYTES);
                boolean accessState = employeeBytes[o] == TRUE_BYTE[0];
                o++;
                employee.setAccessState(getArea(buildings, areaUuid), accessState);
            }
        }

        List<AccessAttempt> history = new ArrayList<>();
        int startOffset = Integer.BYTES + buildingBytes.length + employeeBytes.length;
        int endOffset = Integer.BYTES * 2 + buildingBytes.length + employeeBytes.length;
        byte[] accessBytes = new byte[Utils.bytesToInt(bytes, startOffset, endOffset) + Integer.BYTES];
        System.arraycopy(bytes, Integer.BYTES + buildingBytes.length + employeeBytes.length, accessBytes, 0,
                accessBytes.length);
        o = Integer.BYTES;
        int accessCount = Utils.bytesToInt(accessBytes, o, o += Integer.BYTES);
        for(int a = 0; a < accessCount; a++) {
            long timestamp = Utils.bytesToLong(accessBytes, o, o += Long.BYTES);
            int employeeId = Utils.bytesToInt(accessBytes, o, o += Integer.BYTES);
            AttemptFlag flag = AttemptFlag.fromCode(Utils.bytesToInt(accessBytes, o, o += Integer.BYTES));
            UUID areaUuid = Utils.uuidFromBytes(accessBytes, o, o += Utils.UUID_BYTES);
            history.add(new AccessAttempt(timestamp, getArea(buildings, areaUuid), employeeId, flag));
        }

        Manager m = new Manager(buildings, employees);
        m.accessManager.addAll(history);
        return m;
    }

    /**
     * Utility function for finding an Area within a list of buildings, given its UUID
     */
    private static Area getArea(List<Building> buildings, UUID uuid) {
        for(Building b : buildings) {
            if(b.uuid.equals(uuid)) return b;
            for(Suite s : b.getSuites()) {
                if(s.uuid.equals(uuid)) return s;
                for(Room r : s.getRooms()) {
                    if(r.uuid.equals(uuid)) return r;
                }
            }
        }
        return null;
    }

    public static Manager read(File file) throws IOException {
        Manager m = deserialize(Files.readAllBytes(file.toPath()));
        m.lastIOHash = Utils.sha1(m);
        return m;
    }

    public static void write(Manager m, File file) throws IOException {
        Files.write(file.toPath(), serialize(m));
        m.lastIOHash = Utils.sha1(m);
    }
}

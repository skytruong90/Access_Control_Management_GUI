package vsu.se22.team1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class PersistenceTest {
    /**
     * The bytes of an empty Manager should be as follows:
     * FE BA BE FA 00 00 00 04 00 00 00 00 00 00 00 04 00 00 00 00 00 00 00 04 00 00 00 00
     */
    @Test
    void test_Serialize_Empty() {
        Manager m = new Manager();
        // This is equivalent to string of hex numbers above
        byte[] expected = new byte[] { -2, -70, -66, -6, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 4, 0,
                0, 0, 0 };
        assertTrue(Arrays.equals(Persistence.serialize(m), expected));
    }

    /**
     * A newly instantiated Manager should be 28 bytes: one integer representing the
     * file type's magic number, three integers representing the length of each section,
     * and one integer in each section representing the number of elements in the section.
     * 
     * The actual bytes should be as follows:
     * FE BA BE FA 00 00 00 04 00 00 00 04 00 00 00 04 00 00 00 00 00 00 00 00 00 00 00 00
     */
    @Test
    void test_Serialize_Empty_Length() {
        Manager m = new Manager();
        assertEquals(Persistence.serialize(m).length, 28);
    }

    @Test
    void test_Deserialize_Byte_Array() {
        Manager original = new Manager();
        Building a = new Building("Admin", 1);
        original.addBuilding(a);
        Suite office = new Suite("Office", 10);
        for(int i = 1; i <= 8; i++) {
            office.addRoom(new Room(i));
        }
        Suite marketing = new Suite("Marketing", 20);
        for(int i = 1; i <= 6; i++) {
            marketing.addRoom(new Room(i));
        }
        a.addSuite(office);
        a.addSuite(marketing);
        Building c = new Building("Customers", 7);
        Suite basic = new Suite("Basic", 74);
        for(int i = 1; i <= 40; i++) {
            basic.addRoom(new Room(i));
        }
        Suite special = new Suite("Special", 78);
        for(int i = 1; i <= 10; i++) {
            special.addRoom(new Room(i));
        }
        c.addSuite(basic);
        c.addSuite(special);
        office.addRoom(new Room(106));
        original.addBuilding(c);
        Employee e1 = new Employee("John Doe", 456);
        e1.setAccessState(a, true);
        e1.setAccessState(marketing, false);
        e1.setAccessState(c, true);
        e1.setAccessState(basic, true);
        e1.setAccessState(special, true);
        original.accessManager.logAttempt(e1.id, basic);
        Employee e2 = new Employee("Jane Smith", 123);
        e2.setAccessState(a, true);
        original.accessManager.logAttempt(e2.id, a);
        original.accessManager.logAttempt(e2.id, office);
        original.accessManager.logAttempt(e2.id, marketing);
        original.accessManager.logAttempt(e2.id, special);
        original.accessManager.logAttempt(789, office);
        original.addEmployee(e1);
        original.addEmployee(e2);
        byte[] bytes = Persistence.serialize(original);
        Manager deserialized = null;
        try {
            deserialized = Persistence.deserialize(bytes);
        } catch(IOException ex) {
            fail("IOException during deserialzation");
        }

        assertEquals(original.buildings.size(), deserialized.buildings.size());
        assertEquals(original.employees.size(), deserialized.employees.size());
        assertEquals(original.accessManager.getHistory().size(), deserialized.accessManager.getHistory().size());
        for(Building b1 : original.buildings) {
            boolean containsBuilding = false;
            for(Building b2 : deserialized.buildings) {
                if(sameBuilding(b1, b2)) {
                    containsBuilding = true;
                    break;
                }
            }
            assertTrue(containsBuilding);
        }
        for(Employee e : original.employees) {
            assertNotNull(deserialized.getEmployee(e.id));
        }
        for(AccessAttempt a1 : original.accessManager.getHistory()) {
            boolean containsAttempt = false;
            for(AccessAttempt a2 : deserialized.accessManager.getHistory()) {
                if(sameAccessAttempt(a1, a2)) {
                    containsAttempt = true;
                    break;
                }
            }
            assertTrue(containsAttempt);
        }
    }

    private static boolean sameBuilding(Building a, Building b) {
        if(a == b) return true;
        if(a == null || b == null) return false;
        return a.uuid.equals(b.uuid);
    }

    private static boolean sameAccessAttempt(AccessAttempt a, AccessAttempt b) {
        if(a == b) return true;
        if(a == null || b == null) return false;
        return a.timestamp == b.timestamp && a.area.uuid.equals(b.area.uuid) && a.employeeId == b.employeeId
                && a.flag == b.flag;
    }
}

package vsu.se22.team1.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import vsu.se22.team1.Building;
import vsu.se22.team1.Employee;
import vsu.se22.team1.Manager;

class ManagerTest {



    @Test
    void test_Add_Building() {
        Manager m = new Manager();
        Building b = new Building("Guille", 0);
        Employee e = new Employee("Mike", 1457);
        Employee ee = new Employee(UUID.randomUUID(), "Tim", 8163);

        m.addBuilding(b);
        assertTrue(m.buildings.contains(b));
    }

    @Test
    void test_Remove_Building() {
        Manager m = new Manager();
        Building b = new Building("Guille", 0);
        Employee e = new Employee("Mike", 1457);
        Employee ee = new Employee(UUID.randomUUID(), "Tim", 8163);

        m.addBuilding(b);
        m.removeBuilding(b);
        assertFalse(m.buildings.contains(b));
    }

    @Test
    void test_Add_Employee() {
        Manager m = new Manager();
        Building b = new Building("Guille", 0);
        Employee e = new Employee("Mike", 1457);
        Employee ee = new Employee(UUID.randomUUID(), "Tim", 8163);

        m.addEmployee(e);
        assertTrue(m.employees.contains(e));
    }

    @Test
    void test_Add_Employee_With_UUID() {
        Manager m = new Manager();
        Building b = new Building("Guille", 0);
        Employee e = new Employee("Mike", 1457);
        Employee ee = new Employee(UUID.randomUUID(), "Tim", 8163);

        m.addEmployee(ee);
        assertTrue(m.employees.contains(ee));
    }

    @Test
    void test_Get_Employee_By_Id_Fail() {
        Manager m = new Manager();
        Building b = new Building("Guille", 0);
        Employee e = new Employee("Mike", 1457);
        Employee ee = new Employee(UUID.randomUUID(), "Tim", 8163);

        m.addEmployee(e);
        String expected = "Mike (ID 1457)";
        assertNotEquals(m.getEmployee(1457), expected);
    }

    @Test
    void test_Get_Employee_By_Id() {
        Manager m = new Manager();
        Building b = new Building("Guille", 0);
        Employee e = new Employee("Mike", 1457);
        Employee ee = new Employee(UUID.randomUUID(), "Tim", 8163);

        m.addEmployee(e);
        Employee expected = e;
        assertEquals(m.getEmployee(1457), expected);
    }

    @Test
    void test_Remove_Employee() {
        Manager m = new Manager();
        Building b = new Building("Guille", 0);
        Employee e = new Employee("Mike", 1457);
        Employee ee = new Employee(UUID.randomUUID(), "Tim", 8163);

        m.addEmployee(e);
        m.removeEmployee(e);
        assertFalse(m.employees.contains(e));
    }

    @Test
    void test_Remove_Employee_By_ID() {
        Manager m = new Manager();
        Building b = new Building("Guille", 0);
        Employee e = new Employee("Mike", 1457);
        Employee ee = new Employee(UUID.randomUUID(), "Tim", 8163);

        m.addEmployee(ee);
        m.removeEmployee(8163);
        assertFalse(m.employees.contains(ee));
    }
}

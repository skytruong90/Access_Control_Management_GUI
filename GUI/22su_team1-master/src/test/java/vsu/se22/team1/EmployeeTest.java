package vsu.se22.team1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void test_Has_Access() {
        Employee e = new Employee("Laura", 4590);
        Area a = new Building("Admin", 1);

        e.setAccessState(a, true);
        assertTrue(e.hasAccess(a));
    }

    @Test
    void test_Has_Access_Fail() {
        Employee e = new Employee("Laura", 4590);
        Area a = new Building("Admin", 1);

        e.setAccessState(a, false);
        assertFalse(e.hasAccess(a));
    }

    @Test
    void test_Get_Accessible_Areas() {
        Employee e = new Employee("Laura", 4590);
        Area a = new Building("Admin", 1);

        e.setAccessState(a, true);
        assertTrue(e.getAccessibleAreas().contains(a));

    }

    // Employee cannot access the accessible area without a TRUE access state
    @Test
    void test_Get_Accessible_Areas_Fail() {
        Employee e = new Employee("Laura", 4590);
        Area a = new Building("Admin", 1);

        e.setAccessState(a, false);
        assertFalse(e.getAccessibleAreas().contains(a));
    }
}

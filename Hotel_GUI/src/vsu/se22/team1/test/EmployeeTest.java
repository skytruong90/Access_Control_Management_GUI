package vsu.se22.team1.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import vsu.se22.team1.Employee;
import vsu.se22.team1.Area;

import org.junit.jupiter.api.Test;

class EmployeeTest {
	
	Employee e = new Employee("Laura", 4590);
	Area a;
	Set<Area> accessible = new HashSet<>();

	@Test
	void test_Has_Access() {
		e.setAccessState(a, true);
		assertTrue(e.hasAccess(a));
	}
	
	@Test
	void test_Has_Access_Fail() {
		e.setAccessState(a, false);
		assertFalse(e.hasAccess(a));
	}
	
	@Test
	void test_Get_Accessible_Areas() {
		accessible.add(a);
		e.setAccessState(a, true);
		assertTrue(e.getAccessibleAreas().contains(a));
		
	}
	
	//Employee cannot access the accessible area without a TRUE access state
	@Test
	void test_Get_Accessible_Areas_Fail() {
		accessible.add(a);
		e.setAccessState(a, false);
		assertFalse(e.getAccessibleAreas().contains(a));
	}

}

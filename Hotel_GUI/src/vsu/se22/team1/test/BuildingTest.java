package vsu.se22.team1.test;

import org.junit.jupiter.api.Test;
import vsu.se22.team1.Building;
import vsu.se22.team1.Suite;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {


	
	@Test
	void test_Contains() {
		Building b = new Building("Guille", 1);
		Suite s = new Suite("Express", 1);

		b.addSuite(s);
		assertTrue(b.contains(s));
		
	}

	@Test
	void test_Add_Suite() {
		Building b = new Building("Guille", 1);
		Suite s = new Suite("Express", 1);

		b.addSuite(s);
		assertTrue(b.contains(s));
	}
	
	@Test
	void test_Remove_Suite() {
		Building b = new Building("Guille", 1);
		Suite s = new Suite("Express", 1);
		
		b.addSuite(s);
		b.removeSuite(s);
		assertFalse(b.contains(s));
		
	}

}

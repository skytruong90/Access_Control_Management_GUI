package vsu.se22.team1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import vsu.se22.team1.Room;
import vsu.se22.team1.Suite;


class SuiteTest {
	
	Suite s = new Suite("Express", 1);
	Room r = new Room(0);
	
	@Test
	void test_Contains() {
		s.addRoom(r);
		assertTrue(s.contains(r));
	}

	@Test
	void test_Add_Room() {
		s.addRoom(r);
		assertTrue(s.contains(r));
	}

	@Test
	void test_Remove_Room() {
		s.addRoom(r);
		s.removeRoom(r);
		assertFalse(s.contains(r));
	}
	
	@Test
	void test_Contains_Fail() {
		assertFalse(s.contains(r));
	}

}

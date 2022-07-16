package vsu.se22.team1.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import vsu.se22.team1.Room;
import vsu.se22.team1.Suite;


class SuiteTest {
	


	private Suite createSuite(){
		Suite s = new Suite("Express", 1);
		return s;
	}
	private Room createRoom(){
		Room r = new Room(0);
		return r;
	}
	
	@Test
	void test_Contains() {
		s.addRoom(createRoom());
		assertTrue(createSuite().contains(r));
	}

	@Test
	void test_Add_Room() {
		s.addRoom(createRoom());
		assertTrue(createSuite().contains(r));
	}

	@Test
	void test_Remove_Room() {
		Room r = createRoom();
		s.addRoom(r);
		s.removeRoom(r);
		assertFalse(s.contains(r));
	}
	
	@Test
	void test_Contains_Fail() {
		Room r = createRoom();
		assertFalse(s.contains(r));
	}

}

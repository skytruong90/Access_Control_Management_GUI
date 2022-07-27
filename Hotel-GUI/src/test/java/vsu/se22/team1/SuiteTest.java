package vsu.se22.team1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SuiteTest {

    private static Suite createSuite() {
        return new Suite("Express", 1);
    }

    private static Room createRoom() {
        return new Room(0);
    }

    @Test
    void test_Contains() {
        Suite s = createSuite();
        Room r = createRoom();
        s.addRoom(r);
        assertTrue(s.contains(r));
    }

    @Test
    void test_Add_Room() {
        Suite s = createSuite();
        Room r = createRoom();
        s.addRoom(r);
        assertTrue(s.contains(r));
    }

    @Test
    void test_Remove_Room() {
        Suite s = createSuite();
        Room r = createRoom();
        s.addRoom(r);
        s.removeRoom(r);
        assertFalse(s.contains(r));
    }

    @Test
    void test_Contains_Fail() {
        Suite s = createSuite();
        Room r = createRoom();
        assertFalse(s.contains(r));
    }
}

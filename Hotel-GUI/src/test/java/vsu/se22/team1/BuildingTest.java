package vsu.se22.team1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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

package vsu.se22.team1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class UtilsTest {
    private static final String TEST_UUID_STRING = "12345678-9012-3456-7890-123456789012";
    private static final byte[] TEST_UUID_BYTES = { 18, 52, 86, 120, -112, 18, 52, 86, 120, -112, 18, 52, 86, 120, -112,
            18 };
    private static final int TEST_INT = 12345;
    private static final byte[] TEST_INT_BYTES = { 0, 0, 48, 57 };
    private static final long TEST_LONG = 1234567890123456789L;
    private static final byte[] TEST_LONG_BYTES = { 17, 34, 16, -12, 125, -23, -127, 21 };

    @Test
    void test_UuidToBytes() {
        UUID uuid = UUID.fromString(TEST_UUID_STRING);
        assertTrue(Arrays.equals(Utils.uuidToBytes(uuid), TEST_UUID_BYTES));
    }

    @Test
    void test_UuidFromBytes() {
        UUID expected = UUID.fromString(TEST_UUID_STRING);
        assertEquals(expected, Utils.uuidFromBytes(TEST_UUID_BYTES));
    }

    @Test
    void test_Uuid_Reversible() {
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, Utils.uuidFromBytes(Utils.uuidToBytes(uuid)));
    }

    @Test
    void test_LongToBytes() {
        assertTrue(Arrays.equals(Utils.longToBytes(TEST_LONG), TEST_LONG_BYTES));
    }

    @Test
    void test_LongFromBytes() {
        assertEquals(TEST_LONG, Utils.bytesToLong(TEST_LONG_BYTES));
    }

    @Test
    void test_Long_Reversible() {
        assertEquals(TEST_LONG, Utils.bytesToLong(Utils.longToBytes(TEST_LONG)));
    }

    @Test
    void test_IntToBytes() {
        assertTrue(Arrays.equals(Utils.intToBytes(TEST_INT), TEST_INT_BYTES));
    }

    @Test
    void test_IntFromBytes() {
        assertEquals(TEST_INT, Utils.bytesToInt(TEST_INT_BYTES));
    }

    @Test
    void test_Int_Reversible() {
        assertEquals(TEST_INT, Utils.bytesToInt(Utils.intToBytes(TEST_INT)));
    }

    /**
     * The SHA-1 hash of an empty manager should be as follows:
     * B4 CC C8 BA 2C 72 DC 2C 73 5C 2B 1B 9F 1B 8F C4 2D C7 F1 B5
     */
    @Test
    void test_Sha1() {
        Manager m = new Manager();
        // This is equivalent to the string of hex numbers above
        byte[] expected = { -76, -52, -56, -70, 44, 114, -36, 44, 115, 92, 43, 27, -97, 27, -113, -60, 45, -57, -15,
                -75 };
        assertTrue(Arrays.equals(Utils.sha1(m), expected));
    }

    /**
     * The SHA-1 hash of the string "password123" saltedwith
     * with the string "abc" is as follows:
     * 9b7c5097ac524cde1622f3845dc7fa14
     * 1ef829bb83dab7feef3f97dec52de77d
     * d8c120ca429529ea41ed7a4f05229fd3
     * 84d255fca416314e2794985bc4090de7
     */
    @Test
    void test_Sha512() {
        byte[] salt = "abc".getBytes();
        byte[] toHash = "password123".getBytes();
        assertEquals(
                "9b7c5097ac524cde1622f3845dc7fa14" + "1ef829bb83dab7feef3f97dec52de77d"
                        + "d8c120ca429529ea41ed7a4f05229fd3" + "84d255fca416314e2794985bc4090de7",
                Utils.sha512(salt, toHash));
    }

    @Test
    void test_IsDark() {
        assertTrue(Utils.isDark(0, 0, 0));
        assertFalse(Utils.isDark(255, 255, 255));
    }
}

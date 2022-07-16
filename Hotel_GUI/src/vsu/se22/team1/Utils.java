package vsu.se22.team1;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class Utils {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss Z");
    public static final int UUID_BYTES = Long.BYTES * 2;

    public static byte[] uuidToBytes(UUID uuid) {
        byte[] most = longToBytes(uuid.getMostSignificantBits());
        byte[] least = longToBytes(uuid.getLeastSignificantBits());
        byte[] out = new byte[most.length + least.length];
        for(int i = 0; i < most.length; i++) {
            out[i] = most[i];
            out[i + most.length] = least[i];
        }
        return out;
    }

    public static UUID uuidFromBytes(byte[] bytes) {
        long most = 0;
        long least = 0;
        for(int i = 0; i < Long.BYTES; i++) {
            most <<= Byte.SIZE;
            most |= (bytes[i] & 0xFF);
            least <<= Byte.SIZE;
            least |= (bytes[i + Long.BYTES] & 0xFF);
        }
        return new UUID(most, least);
    }

    public static byte[] longToBytes(long l) {
        byte[] out = new byte[Long.BYTES];
        for(int i = Long.BYTES - 1; i >= 0; i--) {
            out[i] = (byte) (l & 0xFF);
            l >>= Byte.SIZE;
        }
        return out;
    }

    public static long bytesToLong(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static byte[] intToBytes(int n) {
        byte[] out = new byte[Integer.BYTES];
        for(int i = Integer.BYTES - 1; i >= 0; i--) {
            out[i] = (byte) (n & 0xFF);
            n >>= Byte.SIZE;
        }
        return out;
    }

    public static int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static byte[] sha1(Manager manager) {
        try {
            return MessageDigest.getInstance("SHA-1").digest(Persistence.serialize(manager));
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

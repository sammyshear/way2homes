package dev.sshear.way2homes;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class HomeDataCodec {

    private HomeDataCodec() {}

    /* ---------- WRITE ---------- */

    public static void write(ByteBuffer buf, HomeData data) {
        writeString(buf, data.name());
        writeVarInt(buf, data.x());
        writeVarInt(buf, data.y());
        writeVarInt(buf, data.z());
    }

    /* ---------- READ ---------- */

    public static HomeData read(ByteBuffer buf) {
        String name = readString(buf);
        int x = readVarInt(buf);
        int y = readVarInt(buf);
        int z = readVarInt(buf);
        return new HomeData(name, x, y, z);
    }

    /* ---------- Primitives ---------- */

    public static void writeString(ByteBuffer buf, String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeVarInt(buf, bytes.length);
        buf.put(bytes);
    }

    public static String readString(ByteBuffer buf) {
        int len = readVarInt(buf);
        byte[] bytes = new byte[len];
        buf.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeVarInt(ByteBuffer buf, int value) {
        while ((value & ~0x7F) != 0) {
            buf.put((byte)((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        buf.put((byte)value);
    }

    public static int readVarInt(ByteBuffer buf) {
        int value = 0;
        int position = 0;
        byte current;

        while (true) {
            current = buf.get();
            value |= (current & 0x7F) << position;

            if ((current & 0x80) == 0) break;
            position += 7;

            if (position >= 32) {
                throw new IllegalStateException("VarInt too large");
            }
        }
        return value;
    }
}

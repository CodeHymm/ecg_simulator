package com.codyhymm;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EcgPacketEncoder {
    private byte sequence = 0x00;

    public byte[] encodeWave(int[] samples6, byte leadType) {
        if (samples6.length != 6) {
            throw new IllegalArgumentException("samples must be 6");
        }

        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.put((byte) 0x02);      // packetType
        buffer.put(sequence++);       // sequence
        buffer.put(leadType);         // leadType
        buffer.put((byte) 0x00);       // flags/reserved

        for (int s : samples6) {
            buffer.putShort((short) s);
        }

        return buffer.array();
    }
}

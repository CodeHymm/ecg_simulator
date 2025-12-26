package com.codyhymm;

public class SimulatorConfig {

    public static final String SERVER_HOST = "127.0.0.1";
    public static final int SERVER_PORT = 9000;

    public static final int SAMPLE_RATE = 250;   // Hz
    public static final int SAMPLES_PER_PACKET = 6;
    public static final int PACKET_INTERVAL_MS = 24; // ≈ 1000 / (250/6)

    private SimulatorConfig() {}
}

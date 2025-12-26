package com.codyhymm;

import java.util.Random;

public class EcgWaveGenerator {

    private final int sampleRate;   // e.g. 250
    private double bpm;             // e.g. 75
    private double phase = 0.0;     // 0..1 (1 beat within phase)

    // 출력 스케일: 커질수록 파형이 커짐 (대략 3000~12000 사이 튜닝)
    private double scale = 7000.0;

    // 아주 약한 노이즈(원하면 0으로)
    private double noiseAmp = 0.0;

    // baseline wander (원하면 0으로)
    private double baselineAmp = 0.01;

    private final Random rnd = new Random();

    public EcgWaveGenerator() {
        this(SimulatorConfig.SAMPLE_RATE, 75.0);
    }

    public EcgWaveGenerator(int sampleRate, double bpm) {
        this.sampleRate = sampleRate;
        this.bpm = bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setNoiseAmp(double noiseAmp) {
        this.noiseAmp = noiseAmp;
    }

    public void setBaselineAmp(double baselineAmp) {
        this.baselineAmp = baselineAmp;
    }

    public int nextSample() {
        // phase increment: (1/fs) / (60/bpm) = bpm/(60*fs)
        phase += bpm / (60.0 * sampleRate);
        if (phase >= 1.0) phase -= 1.0;

        double v = ecgOneBeat(phase);

        int out = (int) Math.round(v * scale);

        // int16 범위 클램프
        if (out > 32767) out = 32767;
        if (out < -32768) out = -32768;

        return out;
    }

    public int[] nextSamples(int count) {
        int[] samples = new int[count];
        for (int i = 0; i < count; i++) {
            samples[i] = nextSample();
        }
        return samples;
    }

    /**
     * 1 beat(phase 0..1) 안에서 P/QRS/T를 합성
     * - QRS는 sigma(폭)를 작게 해야 "뾰족"해짐
     */
    private double ecgOneBeat(double x) {
        // baseline wander (아주 약하게)
        double baseline = baselineAmp * Math.sin(2 * Math.PI * x);

        // P wave: 0.18 근처
        double p  = gauss(x, 0.18, 0.025,  0.12);

        // QRS: 0.40 근처 (Q, R, S로 "뾰족"하게)
        double q  = gauss(x, 0.38, 0.008, -0.15);
        double r  = gauss(x, 0.40, 0.006,  1.20); // ⭐ 여기 sigma가 핵심 (0.004~0.010 튜닝)
        double s  = gauss(x, 0.42, 0.010, -0.25);

        // T wave: 0.62 근처
        double t  = gauss(x, 0.62, 0.050,  0.35);

        // 약한 노이즈
        double noise = (noiseAmp == 0.0) ? 0.0 : (rnd.nextGaussian() * noiseAmp);

        return baseline + p + q + r + s + t + noise;
    }

    private double gauss(double x, double mu, double sigma, double amp) {
        double z = (x - mu) / sigma;
        return amp * Math.exp(-0.5 * z * z);
    }
}

package com.codyhymm;



import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.buffer.Unpooled;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EcgSimulatorClient {

    private final EcgWaveGenerator generator;
    private final EcgPacketEncoder encoder = new EcgPacketEncoder();

    public EcgSimulatorClient() {
        // ✅ 샘플레이트/심박/스케일 세팅
        this.generator = new EcgWaveGenerator(SimulatorConfig.SAMPLE_RATE, 75.0);
        this.generator.setScale(8000);       // 파형 크기(추천 7000~9000)
        this.generator.setNoiseAmp(0.002);   // 약간 자연스럽게 (싫으면 0)
        this.generator.setBaselineAmp(0.01); // baseline wander (싫으면 0)
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) {
                        }
                    });

            Channel channel = bootstrap
                    .connect(SimulatorConfig.SERVER_HOST, SimulatorConfig.SERVER_PORT)
                    .sync()
                    .channel();

            startSending(channel);

            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private void startSending(Channel channel) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
                    int[] samples = generator.nextSamples(SimulatorConfig.SAMPLES_PER_PACKET);

                    byte[] packet = encoder.encodeWave(samples, (byte) 0x02); // LEAD_II
                    channel.writeAndFlush(Unpooled.wrappedBuffer(packet));
                },
                0,
                SimulatorConfig.PACKET_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );
    }
}
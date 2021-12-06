package hr.fer.rassus.lab2.lab2node.udpclient;

import hr.fer.rassus.lab2.lab2node.model.SensorReading;
import hr.fer.rassus.lab2.lab2node.model.message.AckMessage;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
public class UdpClient {

    private final AtomicBoolean running;
    private final Map<Long, SensorReading> readings;

    private BlockingQueue<AckMessage> ackMessageBlockingQueue;

    public UdpClient(AtomicBoolean running, Map<Long, SensorReading> readings) {
        this.running = running;
        this.readings = readings;

        ackMessageBlockingQueue = new LinkedBlockingQueue<>();
    }

    public Thread startListener() {
        Thread thread = new Thread(() -> {
            while (running.get()) {

            }
        });
        thread.start();
        return thread;
    }
}

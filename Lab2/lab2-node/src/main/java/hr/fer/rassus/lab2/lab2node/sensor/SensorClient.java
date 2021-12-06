package hr.fer.rassus.lab2.lab2node.sensor;


import hr.fer.rassus.lab2.lab2node.model.Node;
import hr.fer.rassus.lab2.lab2node.model.SensorReading;
import hr.fer.rassus.lab2.lab2node.udpclient.UdpClient;
import hr.fer.rassus.lab2.lab2node.util.NodeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 28/10/2021
 */
@Component
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class SensorClient {
    private final Random random = new Random();

    private double longitude;
    private double latitude;
    private Set<Node> peers;

    private AtomicBoolean running;

    private UdpClient udpClient;

    private Map<Long, SensorReading> readings = Collections.synchronizedMap(new HashMap<>());

    public Thread init(Set<Node> peers) throws SocketException {
        longitude = 15.87 + (16 - 15.87) * random.nextDouble();
        latitude = 45.75 + (45.85 - 45.75) * random.nextDouble();
        this.peers = peers;
        this.running = new AtomicBoolean(true);

        udpClient = new UdpClient(running, readings);
        return udpClient.startListener();
    }

    public boolean isRunning() {
        return running.get();
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public void generateAndSendReading() {
        int currentLine = (int) (NodeUtil.getUptimeSeconds() % 100);
        SensorReading currentReading = SensorReadingsAdapter.getReadingFromLine(currentLine);

        log.info("Generated reading: {}", currentReading);

        List<Thread> threads = new ArrayList<>();
        for (Node node : peers) {
            Thread thread = new Thread(() -> {
                try {
                    udpClient.sendReadingToNode(currentReading, node);
                } catch (IOException e) {
                    log.error("Error while sending message to node.", e);
                }
            });
            thread.start();
            threads.add(thread);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                log.error("Sending thread interrupted.", e);
            }
        });
        log.info("Sent reading to all peers.");
    }

    public void printAllData() {

    }

    public void printLastIntervalData() {

    }
}

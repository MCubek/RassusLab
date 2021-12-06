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

    public Thread init(Set<Node> peers) {
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

        //TODO
    }

    public void getAndStorePeerReadings() {
        // TODO
    }

    public void printAllData() {

    }

    public void printLastIntervalData() {

    }
}

package hr.fer.rassus.lab2.lab2node.sensor;


import hr.fer.rassus.lab2.lab2node.model.Node;
import hr.fer.rassus.lab2.lab2node.model.SensorReading;
import hr.fer.rassus.lab2.lab2node.model.TimedIdentifiedSensorReading;
import hr.fer.rassus.lab2.lab2node.udpclient.UdpClient;
import hr.fer.rassus.lab2.lab2node.udpclient.message.DataMessage;
import hr.fer.rassus.lab2.lab2node.util.NodeUtil;
import hr.fer.rassus.stupidudp.network.EmulatedSystemClock;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private List<Thread> threads;

    private UdpClient udpClient;

    @Value("${node-id}")
    private int id;
    @Value("${node-port}")
    private int port;

    private Map<Long, TimedIdentifiedSensorReading> readings = Collections.synchronizedMap(new HashMap<>());
    private Map<Integer, Integer> vectorTimestamp = Collections.synchronizedMap(new HashMap<>());
    private EmulatedSystemClock clock = new EmulatedSystemClock();


    public Thread init(Set<Node> peers) throws SocketException {
        longitude = 15.87 + (16 - 15.87) * random.nextDouble();
        latitude = 45.75 + (45.85 - 45.75) * random.nextDouble();
        this.peers = peers;
        this.running = new AtomicBoolean(true);

        udpClient = new UdpClient(running, this, id, port);
        return udpClient.startListener();
    }

    public boolean isRunning() {
        return running.get();
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public void generateAndSendReading() {
        log.debug("Generating reading...");

        int currentLine = (int) (NodeUtil.getUptimeSeconds() % 100);
        SensorReading currentReading = SensorReadingsAdapter.getReadingFromLine(currentLine);

        incrementThisTimestamp();
        TimedIdentifiedSensorReading timedIdentifiedSensorReading = createReading(currentReading);
        saveReading(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE, timedIdentifiedSensorReading, true);

        log.info("Generated new reading.");

        threads = new ArrayList<>();
        for (Node node : peers) {
            Thread thread = new Thread(() -> {
                try {
                    udpClient.sendReadingToNode(timedIdentifiedSensorReading, node);
                } catch (IOException e) {
                    log.error("Error while sending message to node.", e);
                }
            });
            thread.start();
            threads.add(thread);
        }
        log.debug("Started all threads for sending current reading.");

        boolean interrupted = false;

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                interrupted = true;
                log.debug("Sending thread interrupted.");
            }
        }
        if (! interrupted) {
            log.info("Sent reading to all peers.");
        } else {
            log.debug("Sending thread interrupted and readings have not been sent to all peers.");
        }
    }

    @Synchronized
    private TimedIdentifiedSensorReading createReading(SensorReading currentReading) {
        return new TimedIdentifiedSensorReading(currentReading, id, clock.currentTimeMillis(), Map.copyOf(vectorTimestamp));
    }

    public void saveReading(DataMessage receivedMessage) {
        saveReading(receivedMessage.getMessageId(), receivedMessage.getReading(), false);
    }

    public void saveReading(long messageId, TimedIdentifiedSensorReading reading, boolean localReading) {
        if (! localReading) {
            clock.updateTimeIfNeeded(reading.getTimestamp());
            updateVectorTimestamp(reading);
        }

        readings.putIfAbsent(messageId, reading);
    }

    @Synchronized
    private void updateVectorTimestamp(TimedIdentifiedSensorReading reading) {
        for (Map.Entry<Integer, Integer> entry : reading.getVectorTimestamp().entrySet()) {
            if (entry.getValue() > vectorTimestamp.getOrDefault(entry.getKey(), 0)) {
                vectorTimestamp.put(entry.getKey(), entry.getValue());
            }
        }
        incrementThisTimestamp();
    }

    @Synchronized
    private void incrementThisTimestamp() {
        vectorTimestamp.compute(id, (k, v) -> {
            if (v == null) return 1;
            return v + 1;
        });
    }

    public void interrupt() {
        threads.forEach(Thread::interrupt);
        udpClient.getSocket().close();
    }

    public void printData() {
        System.out.println("Skalarne oznake:");
        readings.values().stream()
                .sorted(Comparator.comparing(TimedIdentifiedSensorReading::getTimestamp))
                .forEach(System.out::println);

        System.out.println("\nVektorske oznake:");
        readings.values().stream()
                .sorted(TimedIdentifiedSensorReading.VECTOR_TIMESTAMP_COMPARATOR)
                .forEach(System.out::println);
    }
}

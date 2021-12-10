package hr.fer.rassus.lab2.lab2node.service;

import hr.fer.rassus.lab2.lab2node.model.Node;
import hr.fer.rassus.lab2.lab2node.sensor.SensorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.util.Set;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 28/10/2021
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SensorService {

    private final SensorClient sensorClient;

    private Thread listenerThread;
    private Thread sendThread;
    private Thread resultsThread;

    public void startSensor(Set<Node> peers) throws SocketException {
        log.info("Starting sensor client.");
        listenerThread = sensorClient.init(peers);

        log.info("Sensor started on longitude:{} and latitude:{}.", sensorClient.getLongitude(), sensorClient.getLatitude());


        sendThread = new Thread(() -> {
            while (sensorClient.isRunning() && ! sendThread.isInterrupted()) {
                sensorClient.generateAndSendReading();
            }
        });
        sendThread.start();
        log.debug("Started generate readings and send loop thread");

        resultsThread = new Thread(() -> {
            while (sensorClient.isRunning() && ! resultsThread.isInterrupted()) {
                sensorClient.printData();
                try {
                    // wait 5 seconds
                    //noinspection BusyWait
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
            }
        });
        resultsThread.start();
        log.debug("Started printResults loop thread.");
    }

    public void stopSensor() {
        log.info("Sensor stopping...");

        sensorClient.setRunning(false);
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
        if (sendThread != null) {
            sendThread.interrupt();
        }
        if (resultsThread != null) {
            resultsThread.interrupt();
        }
        sensorClient.interrupt();
        log.debug("All top level worker threads interrupted.");

        sensorClient.printData();
    }
}

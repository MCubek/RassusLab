package hr.fer.rassus.lab2.lab2node.service;

import hr.fer.rassus.lab2.lab2node.model.Node;
import hr.fer.rassus.lab2.lab2node.sensor.SensorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void startSensor(Set<Node> peers) {
        log.info("Starting sensor client.");
        listenerThread = sensorClient.init(peers);

        log.info("Sensor started on longitude:{} and latitude:{}.", sensorClient.getLongitude(), sensorClient.getLatitude());


        sendThread = new Thread(() -> {
            while (sensorClient.isRunning() && ! sendThread.isInterrupted()) {
                sensorClient.generateAndSendReading();
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        });
        sendThread.start();

        resultsThread = new Thread(() -> {
            while (sensorClient.isRunning() && ! resultsThread.isInterrupted()) {
                sensorClient.printLastIntervalData();
                try {
                    // wait 5 seconds
                    //noinspection BusyWait
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
            }
        });
        resultsThread.start();
    }

    public void stopSensor() {
        log.info("Sensor finished with transmission.");

        sensorClient.setRunning(false);
        listenerThread.interrupt();
        sendThread.interrupt();
        resultsThread.interrupt();

        sensorClient.printAllData();
    }
}

package hr.fer.rassus.lab1client;

import hr.fer.rassus.lab1client.sensor.SensorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 28/10/2021
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SensorLauncher implements ApplicationListener<ApplicationReadyEvent> {

    private final SensorClient sensorClient;

    private Thread loopThread;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Starting sensor client.");
        sensorClient.init();

        log.info("Sensor started on longitude:{} and latitude:{}.", sensorClient.getLongitude(), sensorClient.getLatitude());

        log.info("Registering sensor to server on {}:{}", sensorClient.getServerAddress(), sensorClient.getServerPort());
        sensorClient.registerToServer();

        loopThread = new Thread(() -> {
            while (sensorClient.isRunning() && ! loopThread.isInterrupted()) {
                sensorClient.readCalibrateAndStoreReadings();
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        });
        loopThread.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("Sensor shutdown stated.");
        sensorClient.setRunning(false);
        sensorClient.unregister();
        loopThread.interrupt();
    }
}

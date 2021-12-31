package hr.fer.rassus.lab3.humiditymicroservice.util;

import org.springframework.stereotype.Component;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@Component
public class SensorUtil {
    private static final long createdMillis = System.currentTimeMillis();

    public static long getUptimeSeconds() {
        return (System.currentTimeMillis() - createdMillis) / 1000;
    }
}

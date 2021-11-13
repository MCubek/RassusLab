package hr.fer.rassus.lab1client.util;

import org.springframework.stereotype.Component;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 02/11/2021
 */
@Component
public class Global {
    private static final long createdMillis = System.currentTimeMillis();

    public static long getUptimeSeconds() {
        return (System.currentTimeMillis() - createdMillis) / 1000;
    }
}

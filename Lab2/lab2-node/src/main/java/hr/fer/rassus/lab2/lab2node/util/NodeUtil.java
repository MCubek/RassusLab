package hr.fer.rassus.lab2.lab2node.util;

import hr.fer.rassus.lab2.lab2node.model.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
@Component
public class NodeUtil {
    @Value("${node-id}")
    private int id;

    @Value("${node-address:localhost}")
    private String address;

    @Value("${node-port}")
    private int port;

    public Node thisNode() {
        return new Node(id, address, port);
    }

    private static final long createdMillis = System.currentTimeMillis();

    public static long getUptimeSeconds() {
        return (System.currentTimeMillis() - createdMillis) / 1000;
    }
}

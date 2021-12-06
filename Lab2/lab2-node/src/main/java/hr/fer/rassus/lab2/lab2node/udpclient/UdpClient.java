package hr.fer.rassus.lab2.lab2node.udpclient;

import hr.fer.rassus.lab2.lab2node.model.Node;
import hr.fer.rassus.lab2.lab2node.model.TimedIdentifiedSensorReading;
import hr.fer.rassus.lab2.lab2node.model.message.AckMessage;
import hr.fer.rassus.lab2.lab2node.model.message.DataMessage;
import hr.fer.rassus.lab2.lab2node.model.message.Message;
import hr.fer.rassus.lab2.lab2node.util.MessageUtil;
import hr.fer.rassus.stupidudp.network.SimpleSimulatedDatagramSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
@Slf4j
public class UdpClient {

    private final AtomicBoolean running;
    private final Map<Long, TimedIdentifiedSensorReading> readings;
    private final SimpleSimulatedDatagramSocket socket;
    private Thread listenerThread;

    @Value("${node-port}")
    private int port;
    @Value("${node-id}")
    private int id;

    private final Map<Integer, BlockingQueue<AckMessage>> nodeAckMessages;

    public UdpClient(AtomicBoolean running, Map<Long, TimedIdentifiedSensorReading> readings) throws SocketException {
        this.running = running;
        this.readings = readings;

        nodeAckMessages = Collections.synchronizedMap(new HashMap<>());
        socket = new SimpleSimulatedDatagramSocket(port, 0.3, 1000);
    }

    public Thread startListener() {
        listenerThread = new Thread(() -> {
            byte[] rcvBuf = new byte[256];

            while (running.get() && ! listenerThread.isInterrupted()) {
                DatagramPacket response = new DatagramPacket(rcvBuf, rcvBuf.length);
                try {
                    socket.receive(response);
                } catch (IOException e) {
                    log.error("Error while receiving response", e);
                    continue;
                }

                Message receivedMessage;
                try {
                    receivedMessage = MessageUtil.deserializeMessage(response.getData());
                    log.debug("Received message {}.", receivedMessage);
                } catch (IOException | ClassNotFoundException e) {
                    log.error("Error while parsing message.", e);
                    continue;
                }

                try {
                    switch (receivedMessage.getMessageType()) {
                        case ACK -> handleAckMessage((AckMessage) receivedMessage);
                        case DATA -> handleDataMessage((DataMessage) receivedMessage, response);
                    }
                } catch (IOException e) {
                    log.error("Error while handling received message.", e);
                }
            }

            socket.close();
        });
        listenerThread.start();
        return listenerThread;
    }

    private void handleDataMessage(DataMessage receivedMessage, DatagramPacket packet) throws IOException {
        AckMessage ackMessage = new AckMessage(receivedMessage.getMessageId(), id);
        byte[] sendBuf = MessageUtil.serializeMessage(ackMessage);

        DatagramPacket sendPacket = new DatagramPacket(sendBuf,
                sendBuf.length, packet.getAddress(), packet.getPort());

        socket.send(sendPacket);

        readings.put(receivedMessage.getMessageId(), receivedMessage.getReading());
    }

    private void handleAckMessage(AckMessage receivedMessage) {
        int nodeId = receivedMessage.getNodeId();

        BlockingQueue<AckMessage> queue = nodeAckMessages.computeIfAbsent(nodeId, k -> new LinkedBlockingQueue<>());
        queue.add(receivedMessage);
    }

    public void sendReadingToNode(TimedIdentifiedSensorReading currentReading, Node node) throws IOException {
        long messageId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

        byte[] sendBuf = MessageUtil.serializeMessage(new DataMessage(messageId, id, currentReading));

        DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length,
                InetAddress.getByName(node.getAddress()), node.getPort());

        BlockingQueue<AckMessage> queue = nodeAckMessages.computeIfAbsent(node.getId(), (k) -> new LinkedBlockingQueue<>());

        boolean ack = false;
        do {
            socket.send(packet);

            AckMessage ackMessage = null;
            try {
                ackMessage = queue.poll(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                if (running.get())
                    continue;
            }
            if (ackMessage != null && ackMessage.getMessageId() == messageId)
                ack = true;

        } while (! ack);
        log.info("Reading sent to node {}.", node.getId());
    }
}

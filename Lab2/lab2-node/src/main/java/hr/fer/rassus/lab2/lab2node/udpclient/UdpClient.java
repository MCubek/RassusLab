package hr.fer.rassus.lab2.lab2node.udpclient;

import hr.fer.rassus.lab2.lab2node.model.Node;
import hr.fer.rassus.lab2.lab2node.model.TimedIdentifiedSensorReading;
import hr.fer.rassus.lab2.lab2node.sensor.SensorClient;
import hr.fer.rassus.lab2.lab2node.udpclient.message.AckMessage;
import hr.fer.rassus.lab2.lab2node.udpclient.message.DataMessage;
import hr.fer.rassus.lab2.lab2node.udpclient.message.Message;
import hr.fer.rassus.lab2.lab2node.util.MessageUtil;
import hr.fer.rassus.stupidudp.network.SimpleSimulatedDatagramSocket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
    private final SensorClient sensorClient;
    @Getter
    private final SimpleSimulatedDatagramSocket socket;
    private Thread listenerThread;

    @SuppressWarnings("FieldCanBeLocal")
    private final int port;
    private final int id;

    private final Map<Integer, BlockingQueue<AckMessage>> nodeAckMessages;

    public UdpClient(AtomicBoolean running, SensorClient sensorClient, int id, int port) throws SocketException {
        this.running = running;
        this.sensorClient = sensorClient;
        this.id = id;
        this.port = port;

        nodeAckMessages = Collections.synchronizedMap(new HashMap<>());
        socket = new SimpleSimulatedDatagramSocket(this.port, 0.3, 200);
    }

    public Thread startListener() {
        log.info("Starting udp listener on port {}", port);
        listenerThread = new Thread(() -> {
            byte[] rcvBuf = new byte[2048];

            while (running.get() && ! listenerThread.isInterrupted()) {
                DatagramPacket response = new DatagramPacket(rcvBuf, rcvBuf.length);
                try {
                    socket.receive(response);
                    log.debug("Packet received with size {}.", response.getLength());
                } catch (SocketException e) {
                    log.warn("Socket closed. Exiting.");
                    break;
                } catch (IOException e) {
                    log.error("Error while receiving response", e);
                    continue;
                }

                Message receivedMessage;
                try {
                    receivedMessage = MessageUtil.deserializeMessage(response.getData(), response.getOffset(), response.getLength());
                    log.debug("Received message {}.", receivedMessage.toString());
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

        log.debug("Received data with id {} from node {}, sending ack message...", receivedMessage.getMessageId(), receivedMessage.getNodeId());
        socket.send(sendPacket);
        log.debug("Ack message for {} sent to node {}", receivedMessage.getMessageId(), receivedMessage.getNodeId());

        sensorClient.saveReading(receivedMessage);
    }

    private void handleAckMessage(AckMessage receivedMessage) {
        log.debug("Received ack message from node {}, sending it to its queue.", receivedMessage.getNodeId());
        int nodeId = receivedMessage.getNodeId();

        BlockingQueue<AckMessage> queue = nodeAckMessages.computeIfAbsent(nodeId, k -> new LinkedBlockingQueue<>());
        queue.add(receivedMessage);
    }

    public void sendReadingToNode(TimedIdentifiedSensorReading currentReading, long readingId, Node node) throws IOException {
        log.debug("Sending reading {} to node {}.", readingId, node.getId());

        byte[] sendBuf = MessageUtil.serializeMessage(new DataMessage(readingId, id, currentReading));

        DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length,
                InetAddress.getByName(node.getAddress()), node.getPort());

        BlockingQueue<AckMessage> queue = nodeAckMessages.computeIfAbsent(node.getId(), (k) -> new LinkedBlockingQueue<>());

        boolean ack = false;
        do {
            socket.send(packet);
            log.debug("Reading {} sent to node {}. Waiting for ack...", readingId, node.getId());

            AckMessage ackMessage;
            try {
                ackMessage = queue.poll(4, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                if (this.running.get())
                    continue;
                else return;
            }
            if (ackMessage != null && ackMessage.getMessageId() == readingId) {
                ack = true;
                log.debug("Valid ack received from node {} for reading {}.", node.getId(), readingId);
            } else {
                log.debug("Valid ack not received from node {} for reading {}, retrying...", node.getId(), readingId);
            }
        } while (! ack && this.running.get());
        if (ack)
            log.info("Reading {} acknowledged by node {}.", readingId, node.getId());
        else
            log.warn("Sending thread interrupted and is exiting.");
    }
}

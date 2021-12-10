package hr.fer.rassus.lab2.lab2node.util;

import hr.fer.rassus.lab2.lab2node.model.TimedIdentifiedSensorReading;
import hr.fer.rassus.lab2.lab2node.udpclient.message.AckMessage;
import hr.fer.rassus.lab2.lab2node.udpclient.message.DataMessage;
import hr.fer.rassus.lab2.lab2node.udpclient.message.Message;
import hr.fer.rassus.lab2.lab2node.udpclient.message.MessageType;
import org.springframework.util.SerializationUtils;

import java.io.*;
import java.util.Objects;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
public class MessageUtil {

    public static byte[] serializeMessage(Message message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeByte(message.getMessageType().getNumber());
        dos.writeLong(message.getMessageId());

        switch (message.getMessageType()) {
            case DATA -> {
                DataMessage dataMessage = (DataMessage) message;
                dos.writeInt(dataMessage.getNodeId());
                dos.write(Objects.requireNonNull(SerializationUtils.serialize(dataMessage.getReading())));
            }
            case ACK -> {
                AckMessage ackMessage = (AckMessage) message;
                dos.writeInt(ackMessage.getNodeId());
            }
        }

        dos.close();
        return bos.toByteArray();
    }


    public static Message deserializeMessage(byte[] buf, int offset, int length) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(buf, offset, length);
        DataInputStream dos = new DataInputStream(bis);

        MessageType messageType = MessageType.getTypeFromNumber(dos.readByte());
        long id = dos.readLong();

        Message message;

        message = switch (messageType) {
            case DATA -> new DataMessage(id, dos.readInt(), (TimedIdentifiedSensorReading) SerializationUtils.deserialize(dos.readAllBytes()));
            case ACK -> new AckMessage(id, dos.readInt());
        };

        dos.close();

        return message;
    }

}

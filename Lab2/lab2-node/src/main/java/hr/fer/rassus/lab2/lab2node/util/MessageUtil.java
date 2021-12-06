package hr.fer.rassus.lab2.lab2node.util;

import hr.fer.rassus.lab2.lab2node.model.SensorReading;
import hr.fer.rassus.lab2.lab2node.model.message.AckMessage;
import hr.fer.rassus.lab2.lab2node.model.message.DataMessage;
import hr.fer.rassus.lab2.lab2node.model.message.Message;
import hr.fer.rassus.lab2.lab2node.model.message.MessageType;

import java.io.*;

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
                dos.write(serializeObject(dataMessage.getReading()));
            }
            case ACK -> {
                AckMessage ackMessage = (AckMessage) message;
            }
        }

        dos.close();
        return bos.toByteArray();
    }


    public static Message deserializeMessage(byte[] buf) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(buf);
        DataInputStream dos = new DataInputStream(bis);

        MessageType messageType = MessageType.getTypeFromNumber(dos.readByte());
        long id = dos.readLong();

        Message message;

        message = switch (messageType) {
            case DATA -> new DataMessage(id, (SensorReading) deserializeObject(dos.readAllBytes()));
            case ACK -> new AckMessage(id);
        };

        dos.close();

        return message;
    }

    private static byte[] serializeObject(Serializable object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] bytes;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            bytes = bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException ignore) {
            }
        }
        return bytes;
    }

    private static Object deserializeObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object o;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignore) {
            }
        }
        return o;
    }
}

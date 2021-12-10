package hr.fer.rassus.lab2.lab2node.udpclient.message;

import hr.fer.rassus.lab2.lab2node.model.TimedIdentifiedSensorReading;
import lombok.Getter;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
@Getter
public class DataMessage extends Message {
    private final int nodeId;
    private final TimedIdentifiedSensorReading reading;

    public DataMessage(long messageId,int nodeId, TimedIdentifiedSensorReading reading) {
        super(MessageType.DATA, messageId);
        this.nodeId = nodeId;
        this.reading = reading;
    }
}

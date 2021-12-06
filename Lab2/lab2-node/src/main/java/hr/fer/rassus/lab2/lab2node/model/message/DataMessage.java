package hr.fer.rassus.lab2.lab2node.model.message;

import hr.fer.rassus.lab2.lab2node.model.SensorReading;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
@Getter
public class DataMessage extends Message {
    private final SensorReading reading;

    public DataMessage(long messageId, SensorReading reading) {
        super(MessageType.DATA, messageId);
        this.reading = reading;
    }
}

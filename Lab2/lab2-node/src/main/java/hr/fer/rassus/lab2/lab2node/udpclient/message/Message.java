package hr.fer.rassus.lab2.lab2node.udpclient.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
@AllArgsConstructor
@Getter
@ToString
public class Message {
    private final MessageType messageType;
    private final long messageId;
}

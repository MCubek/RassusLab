package hr.fer.rassus.lab2.lab2node.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
@AllArgsConstructor
@Getter
public class Message {
    private final MessageType messageType;
    private final long messageId;
}

package hr.fer.rassus.lab2.lab2node.model.message;

import lombok.Getter;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
public class AckMessage extends Message {
    @Getter
    private final int nodeId;
    public AckMessage(long messageId, int nodeId) {
        super(MessageType.ACK, messageId);
        this.nodeId = nodeId;
    }
}

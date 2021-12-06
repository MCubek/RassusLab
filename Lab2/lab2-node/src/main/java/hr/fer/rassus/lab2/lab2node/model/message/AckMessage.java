package hr.fer.rassus.lab2.lab2node.model.message;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
public class AckMessage extends Message {
    public AckMessage(long messageId) {
        super(MessageType.ACK, messageId);
    }
}

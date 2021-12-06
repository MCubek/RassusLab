package hr.fer.rassus.lab2.lab2node.model.message;

import lombok.Getter;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */
@Getter
public enum MessageType {
    DATA(1),
    ACK(2);

    private final int number;

    MessageType(int number) {
        this.number = number;
    }

    public static MessageType getTypeFromNumber(int number) {
        return switch (number) {
            case 1 -> DATA;
            case 2 -> ACK;
            default -> throw new IllegalStateException("Unexpected message number: " + number);
        };
    }
}

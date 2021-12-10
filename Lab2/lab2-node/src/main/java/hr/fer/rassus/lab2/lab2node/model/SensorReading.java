package hr.fer.rassus.lab2.lab2node.model;

import lombok.*;

import java.io.Serializable;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 26/10/2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SensorReading implements Serializable {
    private Integer temperature;
    private Integer pressure;
    private Integer humidity;
    private Integer co;
    private Integer no2;
    private Integer so2;

    @Override
    public String toString() {
        return "[" +
               "temp=" + temperature +
               ", pressure=" + pressure +
               ", humidity=" + humidity +
               ", co=" + co +
               ", no2=" + no2 +
               ", so2=" + so2 +
               ']';
    }
}

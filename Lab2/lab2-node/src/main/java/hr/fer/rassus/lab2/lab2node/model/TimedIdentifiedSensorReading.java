package hr.fer.rassus.lab2.lab2node.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */

@NoArgsConstructor
public class TimedIdentifiedSensorReading extends SensorReading {
    @Getter
    private int nodeId;

    public TimedIdentifiedSensorReading(Integer temperature, Integer pressure, Integer humidity, Integer co, Integer no2, Integer so2, int nodeId) {
        super(temperature, pressure, humidity, co, no2, so2);
        this.nodeId = nodeId;
    }

    public TimedIdentifiedSensorReading(SensorReading sensorReading, int nodeId) {
        this(sensorReading.getTemperature(), sensorReading.getPressure(), sensorReading.getHumidity(), sensorReading.getCo(), sensorReading.getNo2(), sensorReading.getSo2(), nodeId);
    }
}

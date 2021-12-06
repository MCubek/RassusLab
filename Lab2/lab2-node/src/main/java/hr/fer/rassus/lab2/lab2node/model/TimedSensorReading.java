package hr.fer.rassus.lab2.lab2node.model;

import lombok.NoArgsConstructor;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */

@NoArgsConstructor
public class TimedSensorReading extends SensorReading {
    public TimedSensorReading(Integer temperature, Integer pressure, Integer humidity, Integer co, Integer no2, Integer so2) {
        super(temperature, pressure, humidity, co, no2, so2);
    }

    public TimedSensorReading(SensorReading sensorReading) {
        this(sensorReading.getTemperature(), sensorReading.getPressure(), sensorReading.getHumidity(), sensorReading.getCo(), sensorReading.getNo2(), sensorReading.getSo2());
    }
}

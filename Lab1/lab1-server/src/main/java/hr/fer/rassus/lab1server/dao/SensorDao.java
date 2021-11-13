package hr.fer.rassus.lab1server.dao;

import hr.fer.rassus.lab1server.model.Sensor;
import lombok.*;

import javax.persistence.Column;

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
public class SensorDao {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String ip;
    private Integer port;

    public static SensorDao mapFromSensor(Sensor sensor) {
        if (sensor == null) return null;
        return SensorDao.builder()
                .id(sensor.getId())
                .latitude(sensor.getLatitude())
                .longitude(sensor.getLongitude())
                .ip(sensor.getIp())
                .port(sensor.getPort())
                .build();
    }

    public static Sensor mapFromDAO(SensorDao sensorDao) {
        Sensor sensor = new Sensor();
        sensor.setLatitude(sensorDao.getLatitude());
        sensor.setLongitude(sensorDao.getLongitude());
        sensor.setIp(sensorDao.getIp());
        sensor.setPort(sensorDao.getPort());
        return sensor;
    }
}

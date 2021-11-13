package hr.fer.rassus.lab1server.dao;

import hr.fer.rassus.lab1server.model.SensorReading;
import lombok.*;

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
public class SensorReadingDao {
    private Integer temperature;
    private Integer pressure;
    private Integer humidity;
    private Integer co;
    private Integer no2;
    private Integer so2;

    public static SensorReadingDao mapFromSensorReading(SensorReading sensorReading) {
        return SensorReadingDao.builder()
                .temperature(sensorReading.getTemperature())
                .pressure(sensorReading.getPressure())
                .humidity(sensorReading.getHumidity())
                .co(sensorReading.getCo())
                .so2(sensorReading.getSo2())
                .no2(sensorReading.getNo2())
                .build();
    }

    public static SensorReading mapFromDAO(SensorReadingDao sensorReadingDao) {
        SensorReading sensorReading = new SensorReading();
        sensorReading.setTemperature(sensorReadingDao.getTemperature());
        sensorReading.setPressure(sensorReadingDao.getPressure());
        sensorReading.setHumidity(sensorReadingDao.getHumidity());
        sensorReading.setCo(sensorReadingDao.getCo());
        sensorReading.setSo2(sensorReadingDao.getSo2());
        sensorReading.setNo2(sensorReadingDao.getNo2());

        return sensorReading;
    }
}

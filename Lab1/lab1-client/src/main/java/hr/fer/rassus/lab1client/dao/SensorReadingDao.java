package hr.fer.rassus.lab1client.dao;

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
@ToString
public class SensorReadingDao {
    private Integer temperature;
    private Integer pressure;
    private Integer humidity;
    private Integer co;
    private Integer no2;
    private Integer so2;
}

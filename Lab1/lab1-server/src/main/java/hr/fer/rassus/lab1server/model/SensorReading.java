package hr.fer.rassus.lab1server.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 26/10/2021
 */
@Entity
@Data
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private Integer temperature;

    private Integer pressure;

    private Integer humidity;

    private Integer co;

    private Integer so2;

    private Integer no2;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;
}

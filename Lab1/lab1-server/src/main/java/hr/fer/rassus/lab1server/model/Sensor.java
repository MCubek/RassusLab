package hr.fer.rassus.lab1server.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 26/10/2021
 */
@Entity
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"ip", "port"})})
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private Integer port;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "sensor")
    private List<SensorReading> readings;
}

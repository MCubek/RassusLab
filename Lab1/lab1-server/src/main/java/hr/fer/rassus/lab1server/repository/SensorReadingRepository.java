package hr.fer.rassus.lab1server.repository;

import hr.fer.rassus.lab1server.model.Sensor;
import hr.fer.rassus.lab1server.model.SensorReading;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 27/10/2021
 */
public interface SensorReadingRepository extends CrudRepository<SensorReading, Long> {
    List<SensorReading> findAllBySensor(Sensor sensor);
}

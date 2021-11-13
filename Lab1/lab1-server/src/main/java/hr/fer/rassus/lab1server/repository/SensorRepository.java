package hr.fer.rassus.lab1server.repository;

import hr.fer.rassus.lab1server.model.Sensor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 26/10/2021
 */
public interface SensorRepository extends CrudRepository<Sensor, Long> {
    List<Sensor> findAll();
}

package hr.fer.rassus.lab3.temperaturemicroservice.repository;

import hr.fer.rassus.lab3.temperaturemicroservice.model.TemperatureReading;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
public interface TemperatureRepository extends CrudRepository<TemperatureReading, Long> {
    List<TemperatureReading> findAll();
}

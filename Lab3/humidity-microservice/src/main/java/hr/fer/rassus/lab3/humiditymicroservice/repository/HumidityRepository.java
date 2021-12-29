package hr.fer.rassus.lab3.humiditymicroservice.repository;

import hr.fer.rassus.lab3.humiditymicroservice.model.HumidityReading;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
public interface HumidityRepository extends CrudRepository<HumidityReading, Long> {
    List<HumidityReading> findAll();
}

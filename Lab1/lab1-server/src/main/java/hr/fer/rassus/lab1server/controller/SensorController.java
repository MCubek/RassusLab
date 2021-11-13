package hr.fer.rassus.lab1server.controller;

import hr.fer.rassus.lab1server.dao.SensorDao;
import hr.fer.rassus.lab1server.dao.SensorReadingDao;
import hr.fer.rassus.lab1server.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 26/10/2021
 */
@RestController
@RequestMapping("rest/sensor")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;

    @GetMapping(value = "/list", produces = "application/json")
    public List<SensorDao> getSensors() {
        return sensorService.getSensors();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public SensorDao getSensorById(@PathVariable Long id) {
        return sensorService.getSensorById(id);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> registerSensor(@RequestBody SensorDao sensorDao) {
        return sensorService.registerSensor(sensorDao);
    }

    @DeleteMapping(value = "/unregister")
    public ResponseEntity<Void> unregisterSensor(@RequestParam Long sensorId) {
        return sensorService.unregisterSensor(sensorId);
    }

    @GetMapping(value = "/nearestNeighbour", produces = "application/json")
    public SensorDao getNearestNeighbour(@RequestParam Long sensorId) {
        return sensorService.getNearestNeighbour(sensorId);
    }

    @PostMapping(value = "data/save")
    public ResponseEntity<Void> saveDate(@RequestParam Long sensorId, @RequestBody SensorReadingDao sensorReadingDao) {
        return sensorService.saveData(sensorId, sensorReadingDao);
    }

    @GetMapping(value = "data/{id}", produces = "application/json")
    public List<SensorReadingDao> getReadingsBySensor(@PathVariable Long id) {
        return sensorService.loadSensorReadingsById(id);
    }
}

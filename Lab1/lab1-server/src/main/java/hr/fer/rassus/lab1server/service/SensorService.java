package hr.fer.rassus.lab1server.service;

import hr.fer.rassus.lab1server.dao.SensorDao;
import hr.fer.rassus.lab1server.dao.SensorReadingDao;
import hr.fer.rassus.lab1server.model.Sensor;
import hr.fer.rassus.lab1server.model.SensorReading;
import hr.fer.rassus.lab1server.repository.SensorReadingRepository;
import hr.fer.rassus.lab1server.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 26/10/2021
 */
@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;

    public List<SensorDao> getSensors() {
        return sensorRepository.findAll().stream()
                .map(SensorDao::mapFromSensor)
                .collect(Collectors.toList());
    }


    public SensorDao getSensorById(Long id) {
        return SensorDao.mapFromSensor(sensorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT)));
    }

    public ResponseEntity<Void> registerSensor(SensorDao sensorDao) {
        Sensor newSensor = SensorDao.mapFromDAO(sensorDao);
        newSensor = sensorRepository.save(newSensor);

        URI dataURI = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/sensor/{id}")
                .build(newSensor.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("HTTP Location", dataURI.toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    public SensorDao getNearestNeighbour(Long id) {
        Sensor currentSensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Id not found."));

        List<Sensor> allSensors = sensorRepository.findAll();

        Sensor closest = allSensors.stream()
                .filter(sensor -> ! sensor.equals(currentSensor))
                .min(Comparator.comparing(sensor -> sensorDistance(sensor, currentSensor)))
                .orElse(null);

        return SensorDao.mapFromSensor(closest);
    }

    public ResponseEntity<Void> saveData(Long sensorId, SensorReadingDao sensorReadingDao) {
        Sensor currentSensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Id not found."));

        SensorReading sensorReading = SensorReadingDao.mapFromDAO(sensorReadingDao);
        sensorReading.setSensor(currentSensor);
        sensorReading = sensorReadingRepository.save(sensorReading);

        URI dataURI = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/sensor/data/{id}")
                .build(sensorReading.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("HTTP Location", dataURI.toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    public List<SensorReadingDao> loadSensorReadingsById(Long id) {
        Sensor currentSensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Id not found."));

        List<SensorReading> readings = sensorReadingRepository.findAllBySensor(currentSensor);

        return readings.stream()
                .map(SensorReadingDao::mapFromSensorReading)
                .collect(Collectors.toList());
    }

    private Double sensorDistance(Sensor sensor1, Sensor sensor2) {
        final double R = 6371;
        double dlon = sensor2.getLongitude() - sensor1.getLongitude();
        double dlat = sensor2.getLatitude() - sensor1.getLatitude();
        double alpha = Math.pow(Math.sin(dlat / 2), 2)
                       + Math.cos(sensor1.getLatitude()) * Math.cos(sensor2.getLatitude()) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(alpha), Math.sqrt(1 - alpha));
        return R * c;
    }

    public ResponseEntity<Void> unregisterSensor(Long sensorId) {
        if (sensorId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be sent");

        sensorRepository.deleteById(sensorId);
        return ResponseEntity.ok().build();
    }
}

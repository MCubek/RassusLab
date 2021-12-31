package hr.fer.rassus.lab3.temperaturemicroservice.service;

import hr.fer.rassus.lab3.temperaturemicroservice.model.SensorReading;
import hr.fer.rassus.lab3.temperaturemicroservice.model.TemperatureDao;
import hr.fer.rassus.lab3.temperaturemicroservice.model.TemperatureReading;
import hr.fer.rassus.lab3.temperaturemicroservice.readings.SensorReadingAdapter;
import hr.fer.rassus.lab3.temperaturemicroservice.repository.TemperatureRepository;
import hr.fer.rassus.lab3.temperaturemicroservice.util.SensorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@Service
@RequiredArgsConstructor
public class TemperatureService {
    private final TemperatureRepository temperatureRepository;

    @Value("${temperature.source-unit}")
    private TemperatureUnit sourceUnit;

    public TemperatureDao getTemperature() {
        SensorReading reading = generateReading();
        saveTemperatureFromReading(reading);

        TemperatureDao temperatureDao = new TemperatureDao();
        temperatureDao.setTemperature(reading.getTemperature());
        temperatureDao.setUnit(sourceUnit.toString());
        return temperatureDao;
    }

    public List<TemperatureDao> getAllTemperatureReadings() {
        return temperatureRepository.findAll().stream()
                .map(reading -> {
                    TemperatureDao dao = new TemperatureDao();
                    dao.setTemperature(reading.getTemperature());
                    dao.setUnit(reading.getUnit().toString());

                    return dao;
                })
                .toList();
    }

    private void saveTemperatureFromReading(SensorReading reading) {
        TemperatureReading temperatureReading = new TemperatureReading();
        temperatureReading.setTemperature(reading.getTemperature());
        temperatureReading.setUnit(sourceUnit);

        temperatureRepository.save(temperatureReading);
    }

    private SensorReading generateReading() {
        int currentLine = (int) (SensorUtil.getUptimeSeconds() % 100);
        return SensorReadingAdapter.getReadingFromLine(currentLine);
    }
}

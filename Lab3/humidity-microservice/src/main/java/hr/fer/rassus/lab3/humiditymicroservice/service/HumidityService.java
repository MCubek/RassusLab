package hr.fer.rassus.lab3.humiditymicroservice.service;

import hr.fer.rassus.lab3.humiditymicroservice.model.HumidityDao;
import hr.fer.rassus.lab3.humiditymicroservice.model.HumidityReading;
import hr.fer.rassus.lab3.humiditymicroservice.model.SensorReading;
import hr.fer.rassus.lab3.humiditymicroservice.readings.SensorReadingAdapter;
import hr.fer.rassus.lab3.humiditymicroservice.repository.HumidityRepository;
import hr.fer.rassus.lab3.humiditymicroservice.util.SensorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@Service
@RequiredArgsConstructor
public class HumidityService {

    private final HumidityRepository humidityRepository;

    public HumidityDao getHumidity() {
        SensorReading reading = generateReading();

        saveHumidityFromReading(reading);

        HumidityDao humidityDao = new HumidityDao();
        humidityDao.setHumidity(reading.getHumidity());
        return humidityDao;
    }

    private void saveHumidityFromReading(SensorReading reading) {
        HumidityReading humidityReading = new HumidityReading();
        humidityReading.setHumidity(reading.getHumidity());

        humidityRepository.save(humidityReading);
    }

    private SensorReading generateReading() {
        int currentLine = (int) (SensorUtil.getUptimeSeconds() % 100);
        return SensorReadingAdapter.getReadingFromLine(currentLine);
    }

    public List<HumidityDao> getAllHumidityReadings() {
        return humidityRepository.findAll().stream()
                .map(reading -> {
                    var dao = new HumidityDao();
                    dao.setHumidity(reading.getHumidity());
                    return dao;
                })
                .toList();
    }
}

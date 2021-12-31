package hr.fer.rassus.lab3.aggregatormicroservice.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import hr.fer.rassus.lab3.aggregatormicroservice.model.TemperatureUnit;
import hr.fer.rassus.lab3.aggregatormicroservice.model.AggregatedDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@SuppressWarnings("HttpUrlsUsage")
@Service
@RequiredArgsConstructor
@Slf4j
public class AggregatorService {

    private final RestTemplate restTemplate;

    @Value("http://${sensor.temperature}/rest/temperature-sensor/")
    private String temperatureServiceAddress;
    @Value("http://${sensor.humidity}/rest/humidity-sensor/")
    private String humidityServiceAddress;

    @Value("${temperature.serve-unit}")
    private TemperatureUnit destinationTemperatureUnit;

    public List<AggregatedDao> getAggregation() {
        List<AggregatedDao> list = new ArrayList<>();

        list.add(fetchHumidityReading());
        list.add(fetchTemperatureReading());

        return list;
    }

    private AggregatedDao fetchTemperatureReading() {
        ObjectNode temperatureReading = restTemplate.getForObject(temperatureServiceAddress, ObjectNode.class);

        AggregatedDao aggregatedDao = new AggregatedDao();
        aggregatedDao.setName(temperatureReading.get("name").asText());
        aggregatedDao.setUnit(destinationTemperatureUnit.toString());

        double temperatureCelsius = convertTemperatureToCelsius(temperatureReading.get("unit").asText()
                , temperatureReading.get("temperature").asDouble());

        aggregatedDao.setValue(convertTemperatureFromCelsius(destinationTemperatureUnit, temperatureCelsius));

        return aggregatedDao;
    }

    private double convertTemperatureFromCelsius(TemperatureUnit destinationTemperatureUnit, double temperatureCelsius) {
        return switch (destinationTemperatureUnit) {
            case C -> temperatureCelsius;
            case F -> (temperatureCelsius * 9 / 5) + 32;
            case K -> temperatureCelsius + 273.15;
        };
    }

    private double convertTemperatureToCelsius(String unit, double temperature) {
        return switch (unit) {
            case "C" -> temperature;
            case "F" -> (temperature - 32) * 5 / 9;
            case "K" -> temperature - 273.15;
            default -> throw new IllegalArgumentException();
        };
    }

    private AggregatedDao fetchHumidityReading() {
        ObjectNode humidityReading = restTemplate.getForObject(humidityServiceAddress, ObjectNode.class);

        AggregatedDao aggregatedDao = new AggregatedDao();
        aggregatedDao.setName(humidityReading.get("name").asText());
        aggregatedDao.setUnit(humidityReading.get("unit").asText());
        aggregatedDao.setValue(humidityReading.get("humidity").asDouble());

        return aggregatedDao;
    }
}

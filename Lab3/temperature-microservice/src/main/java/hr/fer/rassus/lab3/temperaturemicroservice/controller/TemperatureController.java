package hr.fer.rassus.lab3.temperaturemicroservice.controller;

import hr.fer.rassus.lab3.temperaturemicroservice.model.TemperatureDao;
import hr.fer.rassus.lab3.temperaturemicroservice.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@RestController
@RequestMapping("rest/temperature-sensor")
@RequiredArgsConstructor
public class TemperatureController {
    private final TemperatureService temperatureService;

    @GetMapping(value = "", produces = "application/json")
    public TemperatureDao getTemperature() {
        return temperatureService.getTemperature();
    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<TemperatureDao> getAllTemperatureReadings(){
        return temperatureService.getAllTemperatureReadings();
    }
}
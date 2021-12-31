package hr.fer.rassus.lab3.humiditymicroservice.controller;

import hr.fer.rassus.lab3.humiditymicroservice.model.HumidityDao;
import hr.fer.rassus.lab3.humiditymicroservice.service.HumidityService;
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
@RequestMapping("rest/humidity-sensor")
@RequiredArgsConstructor
public class HumidityController {
    private final HumidityService humidityService;

    @GetMapping(value = "", produces = "application/json")
    public HumidityDao getHumidity() {
        return humidityService.getHumidity();
    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<HumidityDao> getAllHumidityReadings(){
        return humidityService.getAllHumidityReadings();
    }
}

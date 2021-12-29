package hr.fer.rassus.lab3.temperaturemicroservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@Getter
@Setter
@RequiredArgsConstructor
public class TemperatureDao {
    private final String name = "Temperature";
    private String unit;
    private int temperature;
}

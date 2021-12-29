package hr.fer.rassus.lab3.humiditymicroservice.model;

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
public class HumidityDao {
    private final String name = "Humidity";
    private final String unit = "%";
    private int humidity;
}

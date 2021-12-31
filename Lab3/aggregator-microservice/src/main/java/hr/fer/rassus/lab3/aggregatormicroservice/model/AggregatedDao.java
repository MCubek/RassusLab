package hr.fer.rassus.lab3.aggregatormicroservice.model;

import lombok.*;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedDao {
    private String name;
    private String unit;
    private Double value;
}

package hr.fer.rassus.lab1client.dao;

import lombok.*;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 26/10/2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SensorDao {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String ip;
    private Integer port;
}

package hr.fer.rassus.lab3.temperaturemicroservice.model;

import hr.fer.rassus.lab3.temperaturemicroservice.service.TemperatureUnit;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TemperatureReading {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private TemperatureUnit unit;

    @Column(nullable = false)
    private Integer temperature;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TemperatureReading that = (TemperatureReading) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

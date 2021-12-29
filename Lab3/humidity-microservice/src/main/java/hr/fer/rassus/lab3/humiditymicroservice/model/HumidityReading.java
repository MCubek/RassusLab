package hr.fer.rassus.lab3.humiditymicroservice.model;

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
public class HumidityReading {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String unit = "%";

    @Column(nullable = false)
    private Integer humidity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HumidityReading that = (HumidityReading) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

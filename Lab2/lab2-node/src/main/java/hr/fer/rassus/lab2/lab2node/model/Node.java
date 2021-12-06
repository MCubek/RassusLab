package hr.fer.rassus.lab2.lab2node.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 05/12/2021
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Node {

    private int id;

    private String address;

    private int port;

}

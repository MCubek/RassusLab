package hr.fer.rassus.lab2.lab2node.model;

import lombok.*;

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

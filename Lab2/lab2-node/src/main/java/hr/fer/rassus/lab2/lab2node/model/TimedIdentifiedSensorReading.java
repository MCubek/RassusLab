package hr.fer.rassus.lab2.lab2node.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 06/12/2021
 */

@NoArgsConstructor
public class TimedIdentifiedSensorReading extends SensorReading {
    @Getter
    private int nodeId;
    @Getter
    private long timestamp;
    @Getter
    @Setter
    private Map<Integer, Integer> vectorTimestamp;

    public TimedIdentifiedSensorReading(Integer temperature, Integer pressure, Integer humidity, Integer co, Integer no2, Integer so2, int nodeId, long timestamp, Map<Integer, Integer> vectorTimestamp) {
        super(temperature, pressure, humidity, co, no2, so2);
        this.nodeId = nodeId;
        this.timestamp = timestamp;
        this.vectorTimestamp = vectorTimestamp;
    }

    public TimedIdentifiedSensorReading(SensorReading sensorReading, int nodeId, long timestamp, Map<Integer, Integer> vectorTimestamp) {
        this(sensorReading.getTemperature(), sensorReading.getPressure(), sensorReading.getHumidity(), sensorReading.getCo(), sensorReading.getNo2(), sensorReading.getSo2(), nodeId, timestamp, vectorTimestamp);
    }

    @Override
    public String toString() {
        return "nodeId=" + nodeId + " " + super.toString()
               + ", " + timestamp + ", "
               + vectorTimestamp.entrySet().stream()
                       .sorted(Map.Entry.comparingByKey())
                       .map(el -> el.getKey() + "->" + el.getValue())
                       .collect(Collectors.joining(",", "[", "]"));
    }

    public static Comparator<TimedIdentifiedSensorReading> VECTOR_TIMESTAMP_COMPARATOR = (o1, o2) -> {
        var timestamp1 = o1.getVectorTimestamp();
        var timestamp2 = o2.getVectorTimestamp();

        Set<Integer> nodes = new HashSet<>();
        nodes.addAll(timestamp1.keySet());
        nodes.addAll(timestamp2.keySet());

        boolean oneLower = false;
        boolean twoLower = false;
        for (int node : nodes) {
            var value1 = timestamp1.getOrDefault(node, 0);
            var value2 = timestamp2.getOrDefault(node, 0);

            if (value1 < value2)
                oneLower = true;
            else if (value1 > value2)
                twoLower = true;
        }

        if (oneLower && ! twoLower) return - 1;
        if (! oneLower && twoLower) return 1;

        return 0;
    };
}

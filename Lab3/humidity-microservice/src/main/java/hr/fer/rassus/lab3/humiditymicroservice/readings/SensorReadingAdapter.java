package hr.fer.rassus.lab3.humiditymicroservice.readings;

import hr.fer.rassus.lab3.humiditymicroservice.model.SensorReading;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@Slf4j
public class SensorReadingAdapter {
    private static Path readingsCsv = null;

    static {
        try {
            readingsCsv = new ClassPathResource("readings.csv").getFile().toPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SensorReading getReadingFromLine(int line) {
        try {
            Reader in = new FileReader(readingsCsv.toAbsolutePath().toString());
            //noinspection deprecation
            CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            var record = csvParser.getRecords().get(line);

            var reading = mapCsvRecordToReadingDao(record);

            log.info("Collected record with id {}: {}", line, reading);

            return reading;
        } catch (IOException e) {
            log.error("Error while reading csv.", e);
            throw new RuntimeException(e);
        }
    }

    private static SensorReading mapCsvRecordToReadingDao(CSVRecord csvRecord) {
        SensorReading sensorReading = new SensorReading();

        sensorReading.setTemperature(Integer.valueOf(csvRecord.get("Temperature")));
        sensorReading.setPressure(Integer.valueOf(csvRecord.get("Pressure")));
        sensorReading.setHumidity(Integer.valueOf(csvRecord.get("Humidity")));
        sensorReading.setCo(Integer.valueOf(csvRecord.get("CO")));
        try {
            sensorReading.setNo2(Integer.valueOf(csvRecord.get("NO2")));
        } catch (IllegalArgumentException | IllegalStateException ignored) {
        }
        try {
            sensorReading.setSo2(Integer.valueOf(csvRecord.get("SO2")));
        } catch (IllegalArgumentException | IllegalStateException ignored) {
        }

        return sensorReading;
    }

}

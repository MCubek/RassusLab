package hr.fer.rassus.lab1client.sensor;

import hr.fer.rassus.lab1client.dao.SensorReadingDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 28/10/2021
 */
@Slf4j
public class SensorReading {
    private static final Path readingsCsv = Path.of("lab1-client/src/main/resources/readings[2].csv");

    public static SensorReadingDao getReadingFromLine(int line) {
        try {
            Reader in = new FileReader(readingsCsv.toAbsolutePath().toString());
            CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            var record = csvParser.getRecords().get(line);

            var reading = mapCsvRecordToReadingDao(record);

            log.info("Collected record with id {}: {}", line, reading);

            return reading;
        } catch (IOException e) {
            log.error("Error while reading csv.", e);
            return null;
        }
    }

    private static SensorReadingDao mapCsvRecordToReadingDao(CSVRecord csvRecord) {
        SensorReadingDao sensorReadingDao = new SensorReadingDao();

        sensorReadingDao.setTemperature(Integer.valueOf(csvRecord.get("Temperature")));
        sensorReadingDao.setPressure(Integer.valueOf(csvRecord.get("Pressure")));
        sensorReadingDao.setHumidity(Integer.valueOf(csvRecord.get("Humidity")));
        sensorReadingDao.setCo(Integer.valueOf(csvRecord.get("CO")));
        try {
            sensorReadingDao.setNo2(Integer.valueOf(csvRecord.get("NO2")));
        } catch (IllegalArgumentException | IllegalStateException ignored) {
        }
        try {
            sensorReadingDao.setSo2(Integer.valueOf(csvRecord.get("SO2")));
        } catch (IllegalArgumentException | IllegalStateException ignored) {
        }

        return sensorReadingDao;
    }

    public static SensorReadingDao calibrateReading(SensorReadingDao reading1, SensorReadingDao reading2) {
        SensorReadingDao calibrated = new SensorReadingDao();

        calibrated.setTemperature(avg(reading1.getTemperature(), reading2.getTemperature()));
        calibrated.setPressure(avg(reading1.getPressure(), reading2.getPressure()));
        calibrated.setHumidity(avg(reading1.getHumidity(), reading2.getHumidity()));
        calibrated.setCo(avg(reading1.getCo(), reading2.getCo()));

        int so2Both = 0;
        if (reading1.getSo2() != null && reading1.getSo2() > 0) {
            calibrated.setSo2(reading1.getSo2());
            so2Both++;
        }
        if (reading2.getSo2() != null && reading2.getSo2() > 0) {
            calibrated.setSo2(reading2.getSo2());
            so2Both++;
        }
        if (so2Both == 2) {
            calibrated.setSo2(avg(reading1.getSo2(), reading2.getSo2()));
        }

        int no2Both = 0;
        if (reading1.getNo2() != null && reading1.getNo2() > 0) {
            calibrated.setNo2(reading1.getNo2());
            no2Both++;
        }
        if (reading2.getNo2() != null && reading2.getNo2() > 0) {
            calibrated.setNo2(reading2.getNo2());
            no2Both++;
        }
        if (no2Both == 2) {
            calibrated.setNo2(avg(reading1.getNo2(), reading2.getNo2()));
        }

        return calibrated;
    }

    private static int avg(int first, int second) {
        return (first + second) / 2;
    }
}

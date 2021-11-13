package hr.fer.rassus.lab1client.grpc;

import hr.fer.rassus.lab1.grpc.ReadingRequest;
import hr.fer.rassus.lab1.grpc.ReadingResponse;
import hr.fer.rassus.lab1.grpc.ReadingsServiceGrpc;
import hr.fer.rassus.lab1client.dao.SensorReadingDao;
import hr.fer.rassus.lab1client.sensor.SensorReading;
import hr.fer.rassus.lab1client.util.Global;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Objects;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 02/11/2021
 */
@GrpcService
@Slf4j
public class ReadingsServiceImpl extends ReadingsServiceGrpc.ReadingsServiceImplBase {
    @Override
    public void getReadings(ReadingRequest request, StreamObserver<ReadingResponse> responseObserver) {
        int currentLine = (int) (Global.getUptimeSeconds() % 100);
        SensorReadingDao currentReading = Objects.requireNonNull(SensorReading.getReadingFromLine(currentLine));

        log.info("Generated reading for grpc client: {}", currentReading);

        ReadingResponse response = ReadingResponse.newBuilder()
                .setTemperature(currentReading.getTemperature())
                .setHumidity(currentReading.getHumidity())
                .setPressure(currentReading.getPressure())
                .setCo(currentReading.getCo())
                .setNo2(currentReading.getNo2() != null ? currentReading.getNo2() : 0)
                .setSo2(currentReading.getSo2() != null ? currentReading.getSo2() : 0)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        log.info("Sent reading to grpc client.");
    }
}

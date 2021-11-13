package hr.fer.rassus.lab1client.sensor;

import hr.fer.rassus.lab1.grpc.ReadingRequest;
import hr.fer.rassus.lab1.grpc.ReadingResponse;
import hr.fer.rassus.lab1.grpc.ReadingsServiceGrpc;
import hr.fer.rassus.lab1.grpc.ReadingsServiceGrpc.ReadingsServiceBlockingStub;
import hr.fer.rassus.lab1client.dao.SensorDao;
import hr.fer.rassus.lab1client.dao.SensorReadingDao;
import hr.fer.rassus.lab1client.util.Global;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author MatejCubek
 * @project lab1-server
 * @created 28/10/2021
 */
@Component
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class SensorClient {
    private final Random random = new Random();


    @Value("${grpc.server.port}")
    private int port;
    @Value("${grpc.server.address}")
    private String address;

    @Value("${lab1.server.port}")
    private int serverPort;
    @Value("${lab1.server.address}")
    private String serverAddress;

    private volatile boolean running = true;

    private long id;

    private double longitude;
    private double latitude;

    private URI baseUri;

    public void init() {
        longitude = 15.87 + (16 - 15.87) * random.nextDouble();
        latitude = 45.75 + (45.85 - 45.75) * random.nextDouble();

        baseUri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(serverAddress)
                .port(serverPort)
                .path("/rest/sensor")
                .build()
                .toUri();
    }

    public void registerToServer() {
        WebClient webClient = WebClient.builder()
                .baseUrl(UriComponentsBuilder.fromUri(baseUri)
                        .path("/register")
                        .build()
                        .toUriString())
                .build();

        SensorDao sensorDao = SensorDao.builder()
                .ip(address)
                .port(port)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        var monoResponse = webClient.post()
                .body(Mono.just(sensorDao), SensorDao.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.CREATED)) {
                        var split = response.headers().header("HTTP").get(0).split("/");
                        id = Long.parseLong(split[split.length - 1]);
                        return response.toBodilessEntity();
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                });

        monoResponse.doOnError(err -> {
                    running = false;
                    log.error("Error while registering to server.", err);
                })
                .doOnSuccess(res -> log.info("Registered to server. Received id:{}", id))
                .block();
    }

    public void readCalibrateAndStoreReadings() {
        int currentLine = (int) (Global.getUptimeSeconds() % 100);
        SensorReadingDao currentReading = SensorReading.getReadingFromLine(currentLine);

        log.info("Generated reading: {}", currentReading);

        SensorDao closest = getClosestNeighbour();

        if (closest != null) {
            currentReading = SensorReading.calibrateReading(Objects.requireNonNull(currentReading), getRemoteReading(closest));
            log.info("Calibrated reading: {}", currentReading);
        }

        publishReading(currentReading);
    }

    private SensorReadingDao getRemoteReading(SensorDao closest) {
        if (closest == null) return null;

        String ip = closest.getIp();
        Integer port = closest.getPort();

        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();

        ReadingsServiceBlockingStub stub = ReadingsServiceGrpc.newBlockingStub(channel);

        log.info("Retrieving reading from grpc server {}:{}...", ip, port);

        ReadingResponse response;
        try {
            response = stub.getReadings(ReadingRequest.newBuilder().build());
            Objects.requireNonNull(response);
        } catch (StatusRuntimeException | NullPointerException e) {
            log.error("Grpc reading fetch failed.", e);
            return null;
        }
        var reading = SensorReadingDao.builder()
                .temperature(response.getTemperature())
                .humidity(response.getHumidity())
                .pressure(response.getPressure())
                .co(response.getCo())
                .no2(response.getNo2())
                .so2(response.getSo2())
                .build();

        log.info("Reading from neighbour received: {}", reading);

        try {
            channel.shutdown();
            channel.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Error while terminating channel.", e);
        }

        return reading;
    }

    private void publishReading(SensorReadingDao currentReading) {
        WebClient webClient = WebClient.builder()
                .baseUrl(UriComponentsBuilder.fromUri(baseUri)
                        .path("/data/save")
                        .replaceQueryParam("sensorId", id)
                        .build()
                        .toUriString())
                .build();

        var monoResponse = webClient.post()
                .body(Mono.just(currentReading), SensorReadingDao.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.CREATED)) {
                        return response.toBodilessEntity();
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                });

        monoResponse.doOnError(err -> {
                    running = false;
                    log.error("Error while sending data to server.", err);
                })
                .doOnSuccess(res -> log.info("Reading data sent to server."))
                .block();
    }

    private SensorDao getClosestNeighbour() {
        WebClient webClient = WebClient.builder()
                .baseUrl(UriComponentsBuilder.fromUri(baseUri)
                        .path("/nearestNeighbour")
                        .replaceQueryParam("sensorId", id)
                        .build()
                        .toUriString())
                .build();


        var monoResponse = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SensorDao.class);

        var closest = monoResponse.doOnError(err -> {
                    running = false;
                    log.error("Error while retrieving neighbour.", err);
                })
                .block();
        if (closest != null)
            log.info("Closest neighbour retrieved with id {}.", closest.getId());
        else
            log.info("No closest neighbour found.");

        return closest;
    }

    public void unregister() {
        WebClient webClient = WebClient.builder()
                .baseUrl(UriComponentsBuilder.fromUri(baseUri)
                        .path("/unregister")
                        .replaceQueryParam("sensorId", id)
                        .build()
                        .toUriString())
                .build();

        var monoResponse = webClient.delete()
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.toBodilessEntity();
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                });

        monoResponse.doOnError(err -> {
                    log.error("Could not unregister sensor from server.", err);
                })
                .doOnSuccess(res -> log.info("Unregistered sensor {} from server.", id))
                .block();
    }
}

package hr.fer.rassus.lab2.lab2controller.controller;

import hr.fer.rassus.lab2.lab2controller.service.CoordinatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 04/12/2021
 */

@RestController
@RequestMapping("rest/node-controller")
@RequiredArgsConstructor
public class CoordinatorController {
    private final CoordinatorService koordinatorService;

    @GetMapping("/start")
    public ResponseEntity<Void> startNodes() {
        return koordinatorService.start();
    }

    @GetMapping("/stop")
    public ResponseEntity<Void> stopNodes() {
        return koordinatorService.stop();
    }

}

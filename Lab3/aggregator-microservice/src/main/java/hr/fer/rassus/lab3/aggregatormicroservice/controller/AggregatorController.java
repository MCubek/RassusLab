package hr.fer.rassus.lab3.aggregatormicroservice.controller;

import hr.fer.rassus.lab3.aggregatormicroservice.model.AggregatedDao;
import hr.fer.rassus.lab3.aggregatormicroservice.service.AggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 29/12/2021
 */
@RestController
@RequestMapping("rest/aggregator")
@RequiredArgsConstructor
public class AggregatorController {

    private final AggregatorService aggregatorService;

    @GetMapping(value = "", produces = "application/json")
    public List<AggregatedDao> getAggregation() {
        return aggregatorService.getAggregation();
    }
}

package readLog.fromLogFile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import readLog.fromLogFile.service.HandlingDataService;
import readLog.fromLogFile.service.MongoService;

@RestController
@RequestMapping("/count")
public class CountController {

    private final MongoService mongoDbService;
    private final HandlingDataService handlingDataService;

    public CountController(MongoService mongoDbService, HandlingDataService handlingDataService) {
        this.mongoDbService = mongoDbService;
        this.handlingDataService = handlingDataService;
    }

    @GetMapping("/countByDay")
    public ResponseEntity<?> countByDay() {
        return handlingDataService.handlingData();
    }

}
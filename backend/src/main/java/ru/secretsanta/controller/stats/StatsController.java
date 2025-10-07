package ru.secretsanta.controller.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.secretsanta.dto.response.StatsResponse;
import ru.secretsanta.service.stats.StatsService;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<StatsResponse> getStats(){
        return ResponseEntity.ok(statsService.getAllStats());
    }

}

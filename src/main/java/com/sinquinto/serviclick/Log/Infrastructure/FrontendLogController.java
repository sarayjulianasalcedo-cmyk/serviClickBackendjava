package com.sinquinto.serviclick.Log.Infrastructure;

import com.sinquinto.serviclick.Log.Application.FrontendLogService;
import com.sinquinto.serviclick.Log.Domain.FrontendLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class FrontendLogController {

    private final FrontendLogService service;

    @PostMapping
    public ResponseEntity<Void> saveLogs(@RequestBody List<FrontendLog> logs) {
        service.saveLogs(logs);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FrontendLog>> getLogs() {
        return ResponseEntity.ok(service.getLogs());
    }
}
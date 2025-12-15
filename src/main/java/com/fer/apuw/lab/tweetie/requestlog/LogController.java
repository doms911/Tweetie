package com.fer.apuw.lab.tweetie.requestlog;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Tag(name = "Logs", description = "Log management")
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final RequestLogService logService;

    @Operation(summary = "Get all logs", description = "Retrieve a list of used methods")

    @GetMapping("/log")
    public List<RequestLogEntry> getLogs() throws IOException {
        return logService.readAll();
    }
}

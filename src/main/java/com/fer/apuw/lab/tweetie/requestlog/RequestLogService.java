package com.fer.apuw.lab.tweetie.requestlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestLogService {

    private static final Path LOG_DIR = Paths.get("logs");

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public RequestLogService() throws IOException {
        Files.createDirectories(LOG_DIR);
    }

    public void log(String method, String path) {
        try {
            String safePath = path.replace("/", "-");
            String fileName = Instant.now() + "_" + method + safePath + ".json";

            RequestLogEntry entry = new RequestLogEntry(
                    method,
                    path
            );

            mapper.writeValue(LOG_DIR.resolve(fileName).toFile(), entry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RequestLogEntry> readAll() throws IOException {
        List<RequestLogEntry> result = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(LOG_DIR, "*.json")) {
            for (Path file : stream) {
                result.add(
                        mapper.readValue(file.toFile(), RequestLogEntry.class)
                );
            }
        }
        return result;
    }
}


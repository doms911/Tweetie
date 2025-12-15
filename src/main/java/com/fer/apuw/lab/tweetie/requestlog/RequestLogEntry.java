package com.fer.apuw.lab.tweetie.requestlog;

public record RequestLogEntry(
        String method,
        String path
) {}

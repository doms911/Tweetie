package com.fer.apuw.lab.tweetie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.fer.apuw.lab.tweetie")
public class TweetieApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetieApplication.class, args);
	}

}

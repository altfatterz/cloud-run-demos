package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HelloWorldKnativeApplication {

    @Value("${TARGET:World}")
    String target;

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldKnativeApplication.class, args);
    }

    @GetMapping("/")
    public String greet() {
        return "Hello " + target + "!";
    }

}

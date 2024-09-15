package ru.itmo.pochtineploho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatsApplication.class, args);
    }
}

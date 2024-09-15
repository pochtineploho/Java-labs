package ru.itmo.pochtineploho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class OwnersApplication {
    public static void main(String[] args) {
        SpringApplication.run(OwnersApplication.class, args);
    }
}

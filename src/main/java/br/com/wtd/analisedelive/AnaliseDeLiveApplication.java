package br.com.wtd.analisedelive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnaliseDeLiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnaliseDeLiveApplication.class, args);
    }
}

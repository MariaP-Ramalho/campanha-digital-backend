package br.com.wtd.analisedelive;

import br.com.wtd.analisedelive.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnaliseDeLiveApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AnaliseDeLiveApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Main main = new Main();
        try {
            main.runMain();
        } catch (Exception e) {
            System.err.println("Erro ao rodar runMain: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
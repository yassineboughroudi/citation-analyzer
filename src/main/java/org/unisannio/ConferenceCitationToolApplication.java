package org.unisannio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.unisannio"})
public class ConferenceCitationToolApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConferenceCitationToolApplication.class, args);
    }
}
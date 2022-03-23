package com.springboot.rafael;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VendasApplication {

    @Gato
    private Animal animal;

    @Bean
    public CommandLineRunner runner(){
        return args -> {
            this.animal.fazerBarulho();
        };
    }

    @Value("${application.name}")
    private String applicationName;

    @GetMapping("/hello")
    public String olaMundo() {
        return "ola mundo " + applicationName;
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}

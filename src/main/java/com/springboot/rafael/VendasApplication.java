package com.springboot.rafael;

import com.springboot.rafael.domain.entity.Client;
import com.springboot.rafael.domain.entity.Product;
import com.springboot.rafael.domain.repository.Clients;
import com.springboot.rafael.domain.repository.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@SpringBootApplication
@RestController
public class VendasApplication {
    @Bean
    public CommandLineRunner commandLineRunner(@Autowired Clients clients, @Autowired Products products){
        return args -> {
            Client client = new Client("Rafael");
            Product product = new Product("Teste", new BigDecimal("1.25"));

            products.save(product);
            clients.save(client);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}

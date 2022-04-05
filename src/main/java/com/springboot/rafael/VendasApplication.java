package com.springboot.rafael;

import com.springboot.rafael.domain.entity.Client;
import com.springboot.rafael.domain.repository.Clients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class VendasApplication {
    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }

    @Bean
    public CommandLineRunner aplicacaoIniciada(@Autowired Clients clients){
        return args -> {
            System.out.println("Aplicação iniciada");
            Client client = new Client("Rafael");
            Client client1 = new Client("Rafael2");

            clients.save(client);
            clients.save(client1);

            List<Client> clientsList = clients.findAll();
            clientsList.forEach(System.out::println);

            clients.delete(client);
            System.out.println("Após deletar");
            clientsList = clients.findAll();

            clientsList.forEach(System.out::println);

            List<Client> clientsByName = clients.buscaporNomeSql(client1.getName());
            System.out.println("Por nome");
            clientsByName.forEach(System.out::println);

            client1.setName("Teste de update");
            client1.setId(2);
            clients.save(client1);
            System.out.println("Após atualizar nome");
            clientsList = clients.findAll();

            clientsList.forEach(System.out::println);


        };
    }
}

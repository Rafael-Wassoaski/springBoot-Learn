package com.springboot.rafael;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@Development
public class RafaelConfiguration {

    @Bean(name = "applicationName")
    public String applicationName() {
        return "Minha Aplicação";
    }

    @Bean
    public CommandLineRunner executar(){
        return args -> {
            System.out.println("Rodando a config de production");
        };
    }


}

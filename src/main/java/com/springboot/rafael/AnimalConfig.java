package com.springboot.rafael;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnimalConfig {

    @Bean(name = "cao")
    public Animal cao(){
        return new Animal() {
            @Override
            public void fazerBarulho() {
                System.out.println("au au");
            }
        };
    }

    @Bean(name = "gato")
    public Animal gato(){
        return new Animal() {
            @Override
            public void fazerBarulho() {
                System.out.println("miau");
            }
        };
    }
}

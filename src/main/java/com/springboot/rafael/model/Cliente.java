package com.springboot.rafael.model;

public class Cliente {
    private String name;
    private int idade;

    public Cliente(String name, int idade) {
        this.name = name;
        this.idade = idade;
    }

    public int getIdade() {
        return idade;
    }

    public String getName() {
        return name;
    }

}

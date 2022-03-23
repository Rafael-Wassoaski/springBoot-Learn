package com.springboot.rafael.services;

import com.springboot.rafael.model.Cliente;
import com.springboot.rafael.repositories.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientesService {
    private ClientesRepository repository;

    @Autowired
    public ClientesService(ClientesRepository repository) {
        this.repository = repository;
    }

    public void saveCliente(Cliente cliente) {
        this.validateCliente(cliente);
        this.repository.persistir(cliente);
    }

    public void validateCliente(Cliente cliente) {
        //aplica validacoes
        if (cliente.getName().isEmpty()) {
            throw new Error("Erro: nome vazio");
        }
    }
}

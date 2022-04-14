package com.springboot.rafael.api.controller;

import com.springboot.rafael.domain.entity.Client;
import com.springboot.rafael.domain.repository.Clients;
import org.springframework.data.domain.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private Clients clients;

    @GetMapping
    public List<Client> find(Client filtro) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        return clients.findAll(example);
    }

    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    public String helloClient(@PathVariable("name") String nameClient) {
        return String.format("Hello %s", nameClient);
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable Integer id) {
        return clients.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encotrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client createClient(@RequestBody Client client) {
        return clients.save(client);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClientById(@PathVariable Integer id) {
        Optional<Client> client = clients.findById(id);

        if (client.isPresent()) {
            clients.deleteById(id);
            return;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
    }

    @PutMapping("/{id}")
    public void updateClient(@PathVariable Integer id, @RequestBody Client client) {
        clients.findById(id).map(clientFound -> {
            client.setId(clientFound.getId());
            clients.save(client);

            return clientFound;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encotrado"));
    }
}

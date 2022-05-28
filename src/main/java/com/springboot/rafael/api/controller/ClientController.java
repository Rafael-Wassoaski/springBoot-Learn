package com.springboot.rafael.api.controller;

import com.springboot.rafael.domain.entity.Client;
import com.springboot.rafael.domain.repository.Clients;
import org.springframework.data.domain.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/clients")
@Api("Clientes")
public class ClientController {
    @Autowired
    private Clients clients;

    @GetMapping
    public List<Client> find(Client filtro) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        return clients.findAll(example);
    }

    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    public String helloClient(@PathVariable("name") String nameClient) {
        return String.format("Hello %s", nameClient);
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Cliente encontrado"),
        @ApiResponse(code = 404, message = "Cliente não localizado"),

    })
    public Client getClientById(@PathVariable @ApiParam("ID do cliente") Integer id) {
        return clients.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encotrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo cliente")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
        @ApiResponse(code = 400, message = "Erro de validação"),

    })
    public Client createClient(@RequestBody @Valid Client client) {
        return clients.save(client);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta um cliente")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Cliente deletado com sucesso"),
        @ApiResponse(code = 404, message = "Cliente não localizado"),

    })
    public void deleteClientById(@PathVariable Integer id) {
        Optional<Client> client = clients.findById(id);

        if (client.isPresent()) {
            clients.deleteById(id);
            return;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
    }

    @PutMapping("/{id}")
    @ApiOperation("Atualiza um cliente")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Cliente atualizado com sucesso"),
        @ApiResponse(code = 404, message = "Cliente não localizado"),
    })
    public void updateClient(@PathVariable Integer id, @RequestBody @Valid Client client) {
        clients.findById(id).map(clientFound -> {
            client.setId(clientFound.getId());
            clients.save(client);

            return clientFound;
        }).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encotrado"));
    }
}

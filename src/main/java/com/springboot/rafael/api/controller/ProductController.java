package com.springboot.rafael.api.controller;

import com.springboot.rafael.domain.entity.Product;
import com.springboot.rafael.domain.repository.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private Products products;

    @GetMapping
    public List<Product> find(Product fitro) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(fitro, matcher);
        return products.findAll(example);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Integer id) {
        return products
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product){
        return products.save(product);
    }

    @PutMapping("/{id}")
    public void createProduct(@PathVariable Integer id, @RequestBody Product product) {
        products.findById(id)
                .map(productFound -> {
                    product.setId(productFound.getId());
                    products.save(product);

                    return productFound;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer id){
        Optional<Product> product = products.findById(id);

        if(product.isPresent()){
            products.deleteById(id);
            return;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}

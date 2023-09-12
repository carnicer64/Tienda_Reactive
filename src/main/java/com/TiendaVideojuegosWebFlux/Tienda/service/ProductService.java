package com.TiendaVideojuegosWebFlux.Tienda.service;

import com.TiendaVideojuegosWebFlux.Tienda.domain.Product;
import com.TiendaVideojuegosWebFlux.Tienda.exception.ProductNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ProductService {

    Flux<Product> findAll();

    Flux<Product> findByName(String name);

    Flux<Product> findByCost(double cost);

    Flux<Product> findBySale(double sale);

    Mono<Product> findById(String id) throws ProductNotFoundException;

    Mono<Product> addProduct(Product product);

    Mono<Product> deleteProduct(String id) throws ProductNotFoundException;

    Mono<Product> modifyProduct(String id, Product product) throws ProductNotFoundException;
}

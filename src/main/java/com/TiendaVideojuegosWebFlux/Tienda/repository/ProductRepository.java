package com.TiendaVideojuegosWebFlux.Tienda.repository;

import com.TiendaVideojuegosWebFlux.Tienda.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    Flux<Product> findAll();

    Flux<Product> findByName(String name);

    Flux<Product> findByCost(double cost);

    Flux<Product> findBySale(double sale);

    Mono<Product> findById(String id);


}

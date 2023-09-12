package com.TiendaVideojuegosWebFlux.Tienda.service;

import com.TiendaVideojuegosWebFlux.Tienda.domain.Product;
import com.TiendaVideojuegosWebFlux.Tienda.exception.ProductNotFoundException;
import com.TiendaVideojuegosWebFlux.Tienda.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService{


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Flux<Product> findByName(String name) {
        logger.info("Name: " + name);
        return productRepository.findByName(name);
    }

    @Override
    public Flux<Product> findByCost(double cost) {
        logger.info("Cost price: " + cost);
        return productRepository.findByCost(cost);
    }

    @Override
    public Flux<Product> findBySale(double sale) {
        logger.info("Sale price: " + sale);
        return productRepository.findBySale(sale);
    }

    @Override
    public Mono<Product> findById(String id) throws ProductNotFoundException {
        logger.info("ID: " + id);
        return productRepository.findById(id).switchIfEmpty(Mono.defer(()  -> Mono.error(new ProductNotFoundException())));
    }

    @Override
    public Mono<Product> addProduct(Product product) {
        logger.info("Creating the product");

        return productRepository.save(product);
    }

    @Override
    public Mono<Product> deleteProduct(String id) throws ProductNotFoundException {
        Mono<Product> product = productRepository.findById(id).switchIfEmpty(Mono.defer(()  -> Mono.error(new ProductNotFoundException())));
        productRepository.deleteById(id).block();
        return product;
    }

    @Override
    public Mono<Product> modifyProduct(String id, Product product) throws ProductNotFoundException {
        logger.info("Modifying the product");

        Mono<Product> modProduct = productRepository.findById(id).switchIfEmpty(Mono.defer(()  -> Mono.error(new ProductNotFoundException())));
        Product newProduct = modProduct.block();
        modelMapper.map(product, newProduct);
        newProduct.setId(id);


        return productRepository.save(newProduct);
    }
}

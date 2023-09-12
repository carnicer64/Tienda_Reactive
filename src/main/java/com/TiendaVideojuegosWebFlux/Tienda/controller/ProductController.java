package com.TiendaVideojuegosWebFlux.Tienda.controller;

import com.TiendaVideojuegosWebFlux.Tienda.domain.Product;
import com.TiendaVideojuegosWebFlux.Tienda.exception.ErrorMessage;
import com.TiendaVideojuegosWebFlux.Tienda.exception.ProductNotFoundException;
import com.TiendaVideojuegosWebFlux.Tienda.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping(value = "/products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProducts(@RequestParam Map<String, String> data) throws ProductNotFoundException {
        logger.info("GET Product");

        if(data.containsKey("name")) {
            logger.info("Name: " + data.get("name"));
            Flux<Product> products = productService.findByName(data.get(("name")));
            logger.info("Showing all products by name");
            return products.delayElements(Duration.ofSeconds(3));
        } else if(data.containsKey("cost")) {
            logger.info("Cost price: " + data.get("cost"));
            Flux<Product> products = productService.findByCost(Double.parseDouble(data.get(("cost"))));
            logger.info("Showing all products by cost price");
            return products.delayElements(Duration.ofSeconds(3));
        } else if (data.containsKey("sale")) {
            logger.info("Sale price: " + data.get("sale"));
            Flux<Product> products = productService.findBySale(Double.parseDouble(data.get(("sale"))));
            logger.info("Showing all products by sale price");
            return products.delayElements(Duration.ofSeconds(3));
        }
        logger.info("Showing all products");
        return productService.findAll().delayElements(Duration.ofSeconds(3));
    }

    @GetMapping("/products/{id}")
    public Mono<Product> getProduct(@PathVariable String id) throws ProductNotFoundException {
        logger.info("Showing all products by ID");
        Mono<Product> product = productService.findById(id);
        logger.info("GET END");
        return product.delayElement(Duration.ZERO);
    }

    @PostMapping("/products")
        public ResponseEntity<Product> addProduct(@RequestBody Product product){
        logger.info("POST Adding product - addProduct" + product);
        Product newProduct = productService.addProduct(product).block();
        logger.info("POST addProduct - end");
        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) throws ProductNotFoundException {
        logger.info("DELETE product");
        productService.deleteProduct(id);
        logger.info("DELETE END");
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{id}")
    public Mono<Product> modifyProduct(@PathVariable String id, @RequestBody Product product) throws ProductNotFoundException {
        logger.info("PUT modify Product");
        Mono<Product> newProduct = productService.modifyProduct(id, product);
        logger.info("PUT END");
        return ResponseEntity.status(HttpStatus.OK).body(newProduct).getBody();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleProductNotFoundException(ProductNotFoundException pnfe) {
        logger.error(pnfe.getMessage(), pnfe);
        ErrorMessage errorMessage = new ErrorMessage(404, pnfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        ErrorMessage badRequestErrorMessage = new ErrorMessage(400, "Bad Request", errors);
        return new ResponseEntity<>(badRequestErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}

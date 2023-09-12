package com.TiendaVideojuegosWebFlux.Tienda.exception;

public class ProductNotFoundException extends Exception{

    public ProductNotFoundException() {
        super("Product not found");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}

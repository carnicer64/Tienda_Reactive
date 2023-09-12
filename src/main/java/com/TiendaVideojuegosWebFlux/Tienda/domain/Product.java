package com.TiendaVideojuegosWebFlux.Tienda.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Product {

    @Id
    private String id;
    @Field
    private String name;
    @Field
    private double cost; //Precio de coste
    @Field
    private double sale; //Precio de venta
    @Field
    private int barCode;
    @Field
    private String imageURL;
}

package org.github.holmistr.devconfdemo.entity;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import java.io.Serializable;

/**
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@Indexed
public class Product implements Serializable {

    private Long id;
    @Field
    private String name;
    @Field
    private String description;
    private int pieces;
    private double price;

    public Product(Long id, String name, String description, int pieces, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pieces = pieces;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pieces=" + pieces +
                ", price=" + price +
                '}';
    }
}

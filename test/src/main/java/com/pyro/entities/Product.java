package com.pyro.entities;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product extends AbstractEntity {

    @Column
    private String name;

    @Column
    private double cost;

    @Column
    private String src;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    Category category;

    public Product() {
    }

    public Product(String name, double cost, String description, String image ,Category category) {
        this.name = name;
        this.cost = cost;
        this.description = description;
        this.category= category;
        if (image.contains("http://localhost:9090/getFile/")) {
            this.src = image;
        } else {
            this.src = "http://localhost:9090/getFile/" + image;
        }
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String image) {
        if (image.contains("http://localhost:9090/getFile/")) {
            this.src = image;
        } else {
            this.src = "http://localhost:9090/getFile/" + image;
        }
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}

package com.pyro.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Deal extends AbstractEntity implements Comparable<Deal> {

    @Column
    private int bought;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "customer", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "product", nullable = false)
    private Product product;

    @Column
    private Date date;

    public Deal() {
    }

    public Deal(User user, Product product, Date date, int bought) {
        this.user = user;
        this.product = product;
        this.date = date;
        this.bought = bought;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }

    @Override
    public int compareTo(Deal o) {
        return this.date.after(o.date)?-1:this.date.before(o.date)?1:0;
    }
}

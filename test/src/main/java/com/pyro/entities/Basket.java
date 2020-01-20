package com.pyro.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Basket extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "customer", nullable = false)
    private User user;

    @OneToMany
    private List<Deal> deals;

    public Basket(){
    }

    public Basket(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public Basket(User user, List<Deal> deals) {
        this.user=user;
        this.deals = deals;
    }

    public double getTotalCost() {
        double total = 0;
        if(deals == null)
            return 0;
        for (Deal i: deals) {
            total+=i.getProduct().getCost();
        }
        return total;
    }

    public void clearBasket() {
        this.deals.removeAll(deals);
    }
}

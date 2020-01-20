package com.pyro.repositories;

import com.pyro.entities.Basket;
import com.pyro.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findByUser(User user);
}

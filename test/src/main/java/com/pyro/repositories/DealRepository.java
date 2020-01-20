package com.pyro.repositories;

import com.pyro.entities.Deal;
import com.pyro.entities.Product;
import com.pyro.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findByProduct(Product product);
    List<Deal>  findByUser(User user);
    void deleteByBoughtAndUser(int bought, User user);
}

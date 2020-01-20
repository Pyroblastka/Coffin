package com.pyro.repositories;

import com.pyro.entities.Category;
import com.pyro.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    List<Product> findByCategory(Category category);
    List<Product> findByNameContainsIgnoreCase(String letter);

}

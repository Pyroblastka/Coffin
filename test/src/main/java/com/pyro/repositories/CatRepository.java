package com.pyro.repositories;

import com.pyro.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    List<Category> findByNameContainsIgnoreCase(String name);
}

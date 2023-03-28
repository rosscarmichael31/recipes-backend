package com.thg.accelerator.recipesbackend.repository;

import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientDatabaseRepository extends JpaRepository<Ingredient, Long> {
    @Query("""
            SELECT DISTINCT r FROM Ingredient r
            WHERE r.id = :id
            """)
    Optional<Ingredient> findById(UUID id);
    List<Ingredient> findByNameIn(List<String> ingredientNames);
    Optional<Ingredient> findByName(String ingredientName);
}


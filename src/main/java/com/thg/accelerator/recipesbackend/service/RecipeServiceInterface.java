package com.thg.accelerator.recipesbackend.service;

import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeServiceInterface {
    Recipe create(Recipe recipe);
    List<Recipe> createAll(List<Recipe> recipes);
    Optional<Recipe> findById(UUID id);
    Page<Recipe> findAll(Pageable pageable);
    Page<Recipe> findByIngredients(List<Ingredient> listOfIngredients, Pageable pageable);
    Recipe findRandom();
    void delete(UUID id);
    List<UUID> findRandomIds(int number);
    Page<Recipe> findByTitle(String wordInput, Pageable pageable);
}

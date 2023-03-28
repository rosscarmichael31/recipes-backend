package com.thg.accelerator.recipesbackend.service;

import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import com.thg.accelerator.recipesbackend.repository.IngredientDatabaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngredientService implements IngredientServiceInterface {
    private final IngredientDatabaseRepository ingredientDatabaseRepository;

    @Override
    public List<Ingredient> findAll() {
        try {
            return ingredientDatabaseRepository.findAll();
        } catch (Exception e) {
            log.error("Database error in ingredientRepository.findAll()");
            throw new RuntimeException("Database error in ingredientRepository.findAll()", e);
        }
    }

    @Override
    public List<Ingredient> findByIngredientNames(List<String> ingredients) {
        try {
            return ingredientDatabaseRepository.findByNameIn(ingredients);
        } catch (Exception e) {
            log.error("Database error in ingredientRepository.findByNameIn()");
            throw new RuntimeException("Database error in ingredientRepository.findByNameIn()", e);
        }

    }

    public void addIngredients(Recipe recipe) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            Optional<Ingredient> existingIngredient = ingredientDatabaseRepository.findByName(ingredient.getName());
            if (existingIngredient.isPresent()) {
                ingredients.add(existingIngredient.get());
            } else {
                ingredients.add(ingredient);
            }
        }
        recipe.setIngredients(ingredients);
    }

}

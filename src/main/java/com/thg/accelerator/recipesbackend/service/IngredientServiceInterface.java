package com.thg.accelerator.recipesbackend.service;

import com.thg.accelerator.recipesbackend.entity.Ingredient;

import java.util.List;

public interface IngredientServiceInterface {
    List<Ingredient> findAll();
    List<Ingredient> findByIngredientNames (List<String> ingredientNames);
}

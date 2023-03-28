package com.thg.accelerator.recipesbackend.service;

import com.thg.accelerator.recipesbackend.dto.IngredientDTO;
import com.thg.accelerator.recipesbackend.dto.RecipeDTO;
import com.thg.accelerator.recipesbackend.entity.*;

import java.util.Set;

public interface UserServiceInterface {
    Set<Recipe> findFavoriteRecipesByUsername(String username);
    Set<Ingredient> findFridgeListByUsername(String username);
    User addFavouriteRecipe(RecipeDTO recipeId);
    User addIngredientToFridge(IngredientDTO ingredientDTO);
    User deleteFavouriteRecipe(RecipeDTO recipeIdDTO);
    User deleteIngredientFromFridge(IngredientDTO ingredientDTO);
    User deleteAllFridgeList();



}

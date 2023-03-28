package com.thg.accelerator.recipesbackend.service;

import com.thg.accelerator.recipesbackend.dto.IngredientDTO;
import com.thg.accelerator.recipesbackend.dto.RecipeDTO;
import com.thg.accelerator.recipesbackend.entity.*;
import com.thg.accelerator.recipesbackend.exceptions.IngredientNotFoundException;
import com.thg.accelerator.recipesbackend.exceptions.UserNotFoundException;
import com.thg.accelerator.recipesbackend.repository.IngredientDatabaseRepository;
import com.thg.accelerator.recipesbackend.repository.RecipeDatabaseRepository;
import com.thg.accelerator.recipesbackend.repository.UserDatabaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserServiceInterface {

    private final UserDatabaseRepository userDatabaseRepository;
    private final RecipeDatabaseRepository recipeDatabaseRepository;
    private final IngredientDatabaseRepository ingredientDatabaseRepository;

    @Override
    public Set<Recipe> findFavoriteRecipesByUsername(String username) {
        try {
            return userDatabaseRepository.findFavoriteRecipesByUsername(username);
        } catch (Exception e) {
            log.error("Database error in userDatabaseRepository.findFavoriteRecipesByUsername()");
            throw new RuntimeException("Database error in userDatabaseRepository.findFavoriteRecipesByUsername()", e);
        }
    }

    @Override
    public Set<Ingredient> findFridgeListByUsername(String username) {
        try {
            return userDatabaseRepository.findFridgeListByUsername(username);
        } catch (Exception e) {
            log.error("Database error in userDatabaseRepository.findFridgeListByUsername()");
            throw new RuntimeException("Database error in userDatabaseRepository.findFridgeListByUsername()", e);
        }
    }

    @Override
    public User addFavouriteRecipe(RecipeDTO recipeDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Recipe recipe = recipeDatabaseRepository.findById(recipeDTO.getId()).orElseThrow(
                () -> new RuntimeException("Recipe not found"));
        User user = userDatabaseRepository.findUserByUsername(username).orElseThrow(
                UserNotFoundException::new);

        user.getFavouriteRecipes().add(recipe);
        return userDatabaseRepository.save(user);
    }

    @Override
    public User addIngredientToFridge(IngredientDTO ingredientDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDatabaseRepository.findUserByUsername(username).orElseThrow(
                UserNotFoundException::new);

        Ingredient ingredient = ingredientDatabaseRepository.findById(ingredientDTO.getId())
                .orElseThrow(IngredientNotFoundException::new);

        user.getFridgeList().add(ingredient);
        return userDatabaseRepository.save(user);
    }

    @Override
    public User deleteFavouriteRecipe(RecipeDTO recipeDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Recipe recipe = recipeDatabaseRepository.findById(recipeDTO.getId()).orElseThrow(
                () -> new RuntimeException("Recipe not found"));
        User user = userDatabaseRepository.findUserByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found"));
        user.getFavouriteRecipes().remove(recipe);
        return userDatabaseRepository.save(user);

    }

    @Override
    public User deleteIngredientFromFridge(IngredientDTO ingredientDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Ingredient ingredient = ingredientDatabaseRepository.findById(ingredientDTO.getId()).orElseThrow(
                () -> new RuntimeException("Recipe not found"));
        User user = userDatabaseRepository.findUserByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found"));
        user.getFridgeList().remove(ingredient);
        return userDatabaseRepository.save(user);

    }

    @Override
    public User deleteAllFridgeList(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDatabaseRepository.findUserByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found"));
        user.getFridgeList().clear();
        return userDatabaseRepository.save(user);
    }


}

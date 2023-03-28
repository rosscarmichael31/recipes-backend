package com.thg.accelerator.recipesbackend.controller;

import com.thg.accelerator.recipesbackend.dto.IngredientDTO;
import com.thg.accelerator.recipesbackend.dto.RecipeDTO;
import com.thg.accelerator.recipesbackend.entity.*;
import com.thg.accelerator.recipesbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/favourites")
    public ResponseEntity<Set<Recipe>> getFavourites() {
        try {
            Set<Recipe> favouriteRecipes = userService.findFavoriteRecipesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            log.info("UserController.GET favourites: Success");
            return new ResponseEntity<>(favouriteRecipes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UserController.GET favourites: Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/favourites")
    public ResponseEntity<User> addFavouriteRecipe(@RequestBody RecipeDTO recipeDTO) {
        try {
            User user = userService.addFavouriteRecipe(recipeDTO);
            log.info("UserController.POST add fave recipe: Success");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error("UserController.POST add fave recipe: Internal Server Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/favourites")
    public ResponseEntity<User> deleteFavouriteRecipe(@RequestBody RecipeDTO recipeDTO) {
        try {
            User user = userService.deleteFavouriteRecipe(recipeDTO);
            log.info("UserController.DELETE delete a fave recipe: Success");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error("UserController.POST delete a fave recipe: Internal Server Error");
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/fridge")
    public ResponseEntity<Set<Ingredient>> getFridgeList() {
        try {
            Set<Ingredient> fridgeList = userService.findFridgeListByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            log.info("UserController.GET fridgeList: Success");
            return new ResponseEntity<>(fridgeList, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UserController.GET fridgeList: Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/fridge")
    public ResponseEntity<User> addIngredientToFridge(@RequestBody IngredientDTO ingredientDTO) {
        try {
            User user = userService.addIngredientToFridge(ingredientDTO);
            log.info("UserController.POST added ingredient to fridge: Success");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UserController.POST failed to add ingredient to fridge: Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/fridge")
    public ResponseEntity<User> deleteIngredientFromFridge(@RequestBody IngredientDTO ingredientDTO) {
        try {
            User user = userService.deleteIngredientFromFridge(ingredientDTO);
            log.info("UserController.DELETE deleted ingredient from fridge: Success");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UserController.DELETE failed to delete ingredient from fridge: Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/fridge/all")
    public ResponseEntity<User> deleteAllFridgeList() {
        try {
            User user = userService.deleteAllFridgeList();
            log.info("UserController.POST deleted fridge list: Success");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UserController.POST failed to delete fridge list: Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}

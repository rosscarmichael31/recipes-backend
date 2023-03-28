package com.thg.accelerator.recipesbackend.controller;

import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import com.thg.accelerator.recipesbackend.service.IngredientService;
import com.thg.accelerator.recipesbackend.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<Recipe> create(@RequestBody Recipe recipe) {
        try {
            ingredientService.addIngredients(recipe);
            Recipe createdRecipe = recipeService.create(recipe);
            log.info("RecipeController.POST: Success");
            return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("RecipeController.POST: Internal Server Error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/all")
    public ResponseEntity<List<Recipe>> createAll(@RequestBody List<Recipe> recipes) {
        log.info("POST in bulk");
        List<Recipe> createdRecipesList = new ArrayList<>();
        try {
            for (Recipe recipe : recipes) {
                ingredientService.addIngredients(recipe);
                Recipe createdRecipe = recipeService.create(recipe);
                createdRecipesList.add(createdRecipe);
            }
            log.info("RecipeController.POST{/all}: Success");
            return new ResponseEntity<>(createdRecipesList, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("RecipeController.POST{/all}: Internal Server Error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> findById(@PathVariable UUID id) {
        try {
            Optional<Recipe> optionalTask = recipeService.findById(id);
            log.info("RecipeController.GET{/id}: Success");
            return optionalTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            log.error("RecipeController.GET{/id}: Internal Server Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/random")
    public ResponseEntity<Recipe> findRandom() {
        try {
            Recipe randomRecipe = recipeService.findRandom();
            log.info("RecipeController.GET{/random}: Success");
            return new ResponseEntity<>(randomRecipe, HttpStatus.OK);
        } catch (Exception e) {
            log.error("RecipeController.GET{/random}: Internal Server Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/random/{number}")
    public ResponseEntity<List<UUID>> findRandomIds(@PathVariable int number) {
        try {
            List<UUID> randomIdsList = recipeService.findRandomIds(number);
            log.info("RecipeController.GET{/random/{}}: Success", number);
            return new ResponseEntity<>(randomIdsList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("RecipeController.GET{/random/{}}: Internal Server Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> findByIngredients(
            @RequestParam(required = false) List<String> ingredientNames,
            @PageableDefault() Pageable pageable) {

        Page<Recipe> recipes;
        try {
            if (ingredientNames != null) {
                List<Ingredient> ingredients = ingredientService.findByIngredientNames(ingredientNames);
                log.info("RecipeController.GET{/{}}: Success", ingredients);
                recipes = recipeService.findByIngredients(ingredients, pageable);

            } else {
                log.info("RecipeController.GET: Success");
                recipes = recipeService.findAll(pageable);
            }
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Access-Control-Expose-Headers", "Total-Pages");
            responseHeaders.add("Total-Pages", Integer.toString(recipes.getTotalPages()));

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(recipes.getContent());

        } catch (Exception e) {
            log.error("RecipeController.GET: Internal Server Error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/titleSearch")
    public ResponseEntity<List<Recipe>> findByTitle(@RequestParam(required = false) String wordInput,  @PageableDefault() Pageable pageable) {
        Page<Recipe> recipes;
        try {
            if (wordInput != null) {
                log.info("GET: by titles by word: {}", wordInput);
                recipes = recipeService.findByTitle(wordInput, pageable);
                System.out.println(recipes);
            } else {
                log.info("GET: page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
                recipes = recipeService.findAll(pageable);
                System.out.println(recipes.getTotalPages());
            }
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Access-Control-Expose-Headers", "Total-Pages");
            responseHeaders.add("Total-Pages", Integer.toString(recipes.getTotalPages()));

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(recipes.getContent());
            } catch(Exception e){
                log.error(e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
    }
}

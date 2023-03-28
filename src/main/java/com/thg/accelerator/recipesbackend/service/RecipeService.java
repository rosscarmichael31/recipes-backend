package com.thg.accelerator.recipesbackend.service;

import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import com.thg.accelerator.recipesbackend.repository.RecipeDatabaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService implements RecipeServiceInterface {

    private final RecipeDatabaseRepository recipeDatabaseRepository;

    @Override
    public Recipe create(Recipe recipe) {
        try {
            return recipeDatabaseRepository.save(recipe);
        } catch (Exception e) {
            log.error("Database error in recipeDatabaseRepository.save()");
            throw new RuntimeException("Database error in recipeDatabaseRepository.save()", e);
        }
    }

    @Override
    public List<Recipe> createAll(List<Recipe> recipes) {
        try {
            return recipeDatabaseRepository.saveAll(recipes);
        } catch (Exception e) {
            log.error("Database error in recipeDatabaseRepository.createAll()");
            throw new RuntimeException("Database error in recipeDatabaseRepository.createAll()", e);
        }

    }

    @Override
    public Optional<Recipe> findById(UUID id) {
        try {
            return recipeDatabaseRepository.findById(id);
        } catch (Exception e) {
            log.error("Database error in recipeDatabaseRepository.findById()");
            throw new RuntimeException("Database error in recipeDatabaseRepository.findById()", e);
        }
    }

    @Override
    public Page<Recipe> findAll(Pageable pageable) {
        try {
            return recipeDatabaseRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Database error in recipeDatabaseRepository.findAll()");
            throw new RuntimeException("Database error in recipeDatabaseRepository.findAll()", e);
        }
    }

    @Override
    public Page<Recipe> findByIngredients(List<Ingredient> ingredients, Pageable pageable) {
        Page<UUID> recipeIds;
        try {
            recipeIds = recipeDatabaseRepository.findRecipeIdsByIngredients(ingredients, pageable);
        } catch (Exception e) {
            log.error("Database error in recipeDatabaseRepository.findRecipeIdsByIngredients()");
            throw new RuntimeException("Database error in recipeDatabaseRepository.findRecipeIdsByIngredients()", e);
        }
        try {
            List<Recipe> recipes = recipeIds.getContent().stream()
                    .map(id -> recipeDatabaseRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return new PageImpl<>(recipes, pageable, recipeIds.getTotalElements());
        } catch (Exception e) {
            log.error("Database error in recipeDatabaseRepository.findById()");
            throw new RuntimeException("Database error in recipeDatabaseRepository.findById()", e);

        }
    }

    @Override
    public void delete(UUID id) {
        try {
            recipeDatabaseRepository.deleteRecipeById(id);
        } catch (Exception e) {
            log.error("Database error in recipeDatabaseRepository.deleteRecipeById()");
            throw new RuntimeException("Database error in recipeDatabaseRepository.deleteRecipeById()", e);
        }
    }

    @Override
    public Recipe findRandom() {
        try {
            return recipeDatabaseRepository.findRandom();
        } catch (Exception e) {
            log.error("Database error in recipeDatabaseRepository.findRandom()");
            throw new RuntimeException("Database error in recipeDatabaseRepository.findRandom()", e);
        }
    }

    @Override
    public List<UUID> findRandomIds(int number) {
        try {
            return recipeDatabaseRepository.findRandomIds(number);
        } catch (Exception e) {
            log.error("Database error in recipeDatabaseRepository.findRandomIds()");
            throw new RuntimeException("Database error in recipeDatabaseRepository.findRandomIds()", e);
        }
    }

    @Override
    public Page<Recipe> findByTitle(String wordInput, Pageable pageable) {
        Page<UUID> recipeIds = recipeDatabaseRepository.findRecipeIdsByTitle(wordInput, pageable);
        List<Recipe> recipes = recipeIds.getContent().stream()
                .map(id -> recipeDatabaseRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new PageImpl<>(recipes, pageable, recipeIds.getTotalElements());
    }
}

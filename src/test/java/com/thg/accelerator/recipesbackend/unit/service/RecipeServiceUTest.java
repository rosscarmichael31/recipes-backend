package com.thg.accelerator.recipesbackend.unit.service;

import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import com.thg.accelerator.recipesbackend.repository.IngredientDatabaseRepository;
import com.thg.accelerator.recipesbackend.repository.RecipeDatabaseRepository;
import com.thg.accelerator.recipesbackend.service.RecipeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class RecipeServiceUTest {

    @Autowired
    RecipeService recipeService;

    @MockBean
    RecipeDatabaseRepository recipeDatabaseRepository;

    @MockBean
    IngredientDatabaseRepository ingredientDatabaseRepository;

    UUID uuid;

    private List<Ingredient> getListIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.builder().name("testName1").build());
        ingredients.add(Ingredient.builder().name("testName2").build());
        return ingredients;
    }

    private Recipe getARecipe() {
        List<Ingredient> ingredients = getListIngredients();
        uuid = UUID.randomUUID();
        return Recipe.builder()
                .id(uuid)
                .name("testName")
                .description("testDescription")
                .method("testMethod")
                .ingredients(ingredients)
                .ingredientsAndQuantities("testIngredientsAndQuantities")
                .prepTime("99")
                .cookTime("99")
                .servings("99")
                .vegetarian(false)
                .vegan(false)
                .glutenFree(false)
                .image("testImg")
                .build();
    }

    @Test
    void create() {
        // given
        Recipe recipe = getARecipe();

        // when
        when(recipeDatabaseRepository.save(recipe)).thenReturn(recipe);
        Recipe createdRecipe = recipeService.create(recipe);

        // then
        verify(recipeDatabaseRepository, times(1)).save(recipe);
        Assertions.assertEquals(recipe, createdRecipe);
    }

    @Test
    void findById() {
        // given
        Recipe recipe = getARecipe();
        UUID recipeId = recipe.getId();

        // when
        when(recipeDatabaseRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        Optional<Recipe> foundRecipe = recipeService.findById(recipeId);

        // then
        verify(recipeDatabaseRepository, times(1)).findById(recipeId);
        Assertions.assertTrue(foundRecipe.isPresent());
        Assertions.assertEquals(recipe, foundRecipe.get());
    }

    @Test
    void findAll() {
        // given
        List<Recipe> recipeList = List.of(
                getARecipe(),
                getARecipe());

        PageImpl<Recipe> recipePage = new PageImpl<>(recipeList);

        Pageable pageable = PageRequest.of(0, 2);

        // when
        when(recipeDatabaseRepository.findAll(any(Pageable.class))).thenReturn(recipePage);
        Page<Recipe> allRecipes = recipeService.findAll(pageable);

        // then
        Assertions.assertArrayEquals(recipeList.toArray(), allRecipes.getContent().toArray());
    }

    @Test
    void findByIngredients() {
        //TODO: implement

        // given


        // when


        // then
    }

    @Test
    void delete() {
        //TODO: implement

        // given


        // when


        // then
    }

}

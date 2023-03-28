package com.thg.accelerator.recipesbackend.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import com.thg.accelerator.recipesbackend.service.IngredientService;
import com.thg.accelerator.recipesbackend.service.RecipeService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RecipesControllerExceptionsUTest {
    private static String testToken;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecipeService recipeService;
    @MockBean
    private IngredientService ingredientService;
    private UUID uuid;

    @BeforeAll
    protected static void setUpToken() throws UnirestException {
        HttpResponse<String> res = Unirest.post("https://dev-cybtep0lwvv6ze0t.us.auth0.com/oauth/token")
                .header("content-type", "application/json")
                .body("{\"client_id\":\"Ck7NP5aeu9crDUxHbzYS3PIg87lQD8FY\",\"client_secret\":\"ivIrtdHebhqgc1eQHtUB1oO34dTZcN8OQ2xIC6rtcxIsL-Z2g2uQr75EemLA3o6j\",\"audience\":\"https://lastsupper.3ajlo.icekube.ics.cloud/api/v1/\",\"grant_type\":\"client_credentials\"}")
                .asString();


        JSONObject responseBody = new JSONObject(res.getBody());
        testToken = responseBody.getString("access_token");
    }

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
    void createException() throws Exception {
        // given
        Recipe recipe = getARecipe();

        // when
        doThrow(new RuntimeException("Failed to create ingredients")).when(ingredientService).addIngredients(recipe);
        when(recipeService.create(recipe)).thenThrow(new RuntimeException("Failed to create recipe"));
        ResultActions response = mockMvc.perform(post("/api/v1/recipes")
                .header("Authorization", "Bearer " + testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipe)));

        // then
        response.andExpect(status().isInternalServerError());

    }

    @Test
    void findByIdException() throws Exception {
        getARecipe();
        // when
        when(recipeService.findById(uuid)).thenThrow(new RuntimeException("Failed to get recipe"));
        ResultActions response = mockMvc.perform(get("/api/v1/recipes/{id}", uuid)
                .header("Authorization", "Bearer " + testToken));

        // then
        response.andExpect(status().isInternalServerError());
    }

    @Test
    void findByIngredientsException() throws Exception {
        getARecipe();
        // when
        when(recipeService.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Failed to get all recipes"));
        ResultActions response = mockMvc.perform(get("/api/v1/recipes")
                .header("Authorization", "Bearer " + testToken));

        // then
        response.andExpect(status().isInternalServerError());
    }

}

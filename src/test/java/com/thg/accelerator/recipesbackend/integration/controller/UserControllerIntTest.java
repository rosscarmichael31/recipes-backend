package com.thg.accelerator.recipesbackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import com.thg.accelerator.recipesbackend.dto.RecipeDTO;
import com.thg.accelerator.recipesbackend.repository.RecipeDatabaseRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntTest {
    private static String testToken;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RecipeDatabaseRepository recipeDatabaseRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    protected static void setUpToken() throws UnirestException {
        HttpResponse<String> res = Unirest.post("https://dev-cybtep0lwvv6ze0t.us.auth0.com/oauth/token")
                .header("content-type", "application/json")
                .body("{\"client_id\":\"Ck7NP5aeu9crDUxHbzYS3PIg87lQD8FY\",\"client_secret\":\"ivIrtdHebhqgc1eQHtUB1oO34dTZcN8OQ2xIC6rtcxIsL-Z2g2uQr75EemLA3o6j\",\"audience\":\"https://lastsupper.3ajlo.icekube.ics.cloud/api/v1/\",\"grant_type\":\"client_credentials\"}")
                .asString();


        JSONObject responseBody = new JSONObject(res.getBody());
        testToken = responseBody.getString("access_token");
    }

    private List<Ingredient> getListIngredients(String ingredientName) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.builder().name(ingredientName).build());
        return ingredients;
    }

    private Recipe getARecipe(String name, String ingredientName) {
        List<Ingredient> ingredients = getListIngredients(ingredientName);
        return Recipe.builder()
                .name(name)
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

    @BeforeEach
    void setUp() {
        recipeDatabaseRepository.deleteAll();
    }

    @Test
    public void addAndGetFavouriteRecipes() throws Exception {
        // given
        recipeDatabaseRepository.deleteAll();
        Recipe recipe1 = getARecipe("test1", "testIngredient1");
        Recipe recipe2 = getARecipe("test2", "testIngredient2");
        Recipe recipe3 = getARecipe("test3", "testIngredient3");

        Recipe savedRecipe1 = recipeDatabaseRepository.save(recipe1);
        Recipe savedRecipe2 = recipeDatabaseRepository.save(recipe2);
        Recipe savedRecipe3 = recipeDatabaseRepository.save(recipe3);


        RecipeDTO recipeDTO1 = RecipeDTO.builder().id(savedRecipe1.getId()).build();
        RecipeDTO recipeDTO2 = RecipeDTO.builder().id(savedRecipe2.getId()).build();
        RecipeDTO recipeDTO3 = RecipeDTO.builder().id(savedRecipe3.getId()).build();


        // when
        ResultActions result1 = mockMvc.perform(post("/api/v1/user/favourites")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + testToken)
                .content(objectMapper.writeValueAsString(recipeDTO1)));

        ResultActions result2 = mockMvc.perform(post("/api/v1/user/favourites")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + testToken)
                .content(objectMapper.writeValueAsString(recipeDTO2)));

        ResultActions result3 = mockMvc.perform(post("/api/v1/user/favourites")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + testToken)
                .content(objectMapper.writeValueAsString(recipeDTO3)));

        // then
        result1.andExpect(status().isOk());
        result2.andExpect(status().isOk());
        result3.andExpect(status().isOk())
                .andExpect(jsonPath("$.favouriteRecipes", hasSize(3)))
                .andExpect(jsonPath("$.favouriteRecipes[*].name", hasItem(recipe1.getName())))
                .andExpect(jsonPath("$.favouriteRecipes[*].name", hasItem(recipe2.getName())))
                .andExpect(jsonPath("$.favouriteRecipes[*].name", hasItem(recipe3.getName())));

        // when
        ResultActions getResult = mockMvc.perform(get("/api/v1/user/favourites")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + testToken));
        // then
        getResult.andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$.[*].name", hasItem(recipe1.getName())))
                .andExpect(jsonPath("$.[*].name", hasItem(recipe2.getName())))
                .andExpect(jsonPath("$.[*].name", hasItem(recipe3.getName())));

    }

    @Test
    @Disabled
    public void addAndGetFromFridgeList() throws Exception {
        // given


        // when


        // then
    }
}

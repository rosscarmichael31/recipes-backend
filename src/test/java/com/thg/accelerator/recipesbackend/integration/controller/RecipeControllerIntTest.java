package com.thg.accelerator.recipesbackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import com.thg.accelerator.recipesbackend.repository.RecipeDatabaseRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RecipeControllerIntTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RecipeDatabaseRepository recipeDatabaseRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private static String testToken;

    private List<Ingredient> getListIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.builder().name("testName1").build());
        ingredients.add(Ingredient.builder().name("testName2").build());
        return ingredients;
    }

    private Recipe getARecipe(String name) {
        List<Ingredient> ingredients = getListIngredients();
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

    @BeforeAll
    protected static void setUpToken() throws UnirestException {
        HttpResponse<String> res = Unirest.post("https://dev-cybtep0lwvv6ze0t.us.auth0.com/oauth/token")
                .header("content-type", "application/json")
                .body("{\"client_id\":\"Ck7NP5aeu9crDUxHbzYS3PIg87lQD8FY\",\"client_secret\":\"ivIrtdHebhqgc1eQHtUB1oO34dTZcN8OQ2xIC6rtcxIsL-Z2g2uQr75EemLA3o6j\",\"audience\":\"https://lastsupper.3ajlo.icekube.ics.cloud/api/v1/\",\"grant_type\":\"client_credentials\"}")
                .asString();


        JSONObject responseBody = new JSONObject(res.getBody());
        testToken = responseBody.getString("access_token");
    }

    @BeforeEach
    void setUp() {
        recipeDatabaseRepository.deleteAll();
    }

    @Test
    public void create() throws Exception {
        // given
        Recipe recipe = getARecipe("testName");

        // when
        ResultActions response = mockMvc.perform(post("/api/v1/recipes")
                .header("Authorization", "Bearer " + testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content((objectMapper.writeValueAsString(recipe))));


        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(recipe.getName())))
                .andExpect(jsonPath("$.ingredientsAndQuantities", is(recipe.getIngredientsAndQuantities())))
                .andExpect(jsonPath("$.prepTime", is(recipe.getPrepTime())));

        List<Recipe> retrievedRecipe = recipeDatabaseRepository.findAll();
        Assertions.assertEquals(1, retrievedRecipe.size());
    }

    @Test
    public void findByIdExists() throws Exception {
        // given
        Recipe recipe = getARecipe("testName");
        recipeDatabaseRepository.save(recipe);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/recipes/{id}", recipe.getId())
                .header("Authorization", "Bearer " + testToken));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(recipe.getName())))
                .andExpect(jsonPath("$.ingredientsAndQuantities", is(recipe.getIngredientsAndQuantities())))
                .andExpect(jsonPath("$.prepTime", is(recipe.getPrepTime())));
    }

    @Test
    public void findByIdInvalid() throws Exception {
        // given
        Recipe recipe = getARecipe("testName");
        recipeDatabaseRepository.save(recipe);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/recipes/{id}", UUID.randomUUID())
                .header("Authorization", "Bearer " + testToken));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void findByIngredients() {
        // TODO: implement
    }
}




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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RecipesControllerUTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    RecipeService recipeService;
    @MockBean
    IngredientService ingredientService;
    ObjectMapper objectMapper = new ObjectMapper();
    UUID uuid;
    private static String testToken;

    @BeforeAll
    protected static void setUp() throws UnirestException {
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
    void create() throws Exception {
        // given
        Recipe recipe = getARecipe();

        // when
        when(recipeService.create(recipe)).thenReturn(recipe);
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes")
                .header("Authorization", "Bearer " + testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipe)));


        // then
        response.andExpect(status().isCreated());
    }

    @Test
    void findByValidId() throws Exception {
        // given
        Recipe recipe = getARecipe();

        // when
        when(recipeService.findById(uuid)).thenReturn(Optional.of(recipe));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/{id}", uuid)
                .header("Authorization", "Bearer " + testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipe)));


        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(recipe.getId().toString())))
                .andExpect(jsonPath("$.name", is(recipe.getName())))
                .andExpect(jsonPath("$.prepTime", is(recipe.getPrepTime())))
                .andExpect(jsonPath("$.vegan", is(recipe.isVegan())));
    }

    @Test
    void findByInvalidId() throws Exception {
        // given
        UUID invalidId = UUID.randomUUID();

        // when
        when(recipeService.findById(invalidId)).thenReturn(Optional.empty());
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/{id}", invalidId)
                .header("Authorization", "Bearer " + testToken));

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    void findByIngredientsAll() throws Exception {
        // given
        List<Recipe> recipeList = List.of(
                getARecipe(),
                getARecipe(),
                getARecipe(),
                getARecipe());

        PageImpl<Recipe> recipePage = new PageImpl<>(recipeList);

        // when
        when(recipeService.findAll(any(Pageable.class))).thenReturn(recipePage);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes")
                        .header("Authorization", "Bearer " + testToken))
                        .andExpect(status().isOk())
                        .andReturn();

        // then
        String stringResult = result.getResponse().getContentAsString();
        System.out.println(stringResult);
        List<Recipe> content = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(4, content.size());
    }

    @Test
    void findByIngredientsSpecified() throws Exception {
        // given
        List<Ingredient> ingredients = getListIngredients();
        List<Recipe> recipeList = List.of(
                getARecipe(),
                getARecipe());

        PageImpl<Recipe> ingredientsPage = new PageImpl<>(recipeList);


        // when
        when(ingredientService.findByIngredientNames(anyList())).thenReturn(ingredients);
        when(recipeService.findByIngredients(anyList(), any(Pageable.class))).thenReturn(ingredientsPage);

        String url = String.format("/api/v1/recipes?ingredientNames=%s,%s",
                ingredients.get(0).getName(),
                ingredients.get(1).getName());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .header("Authorization", "Bearer " + testToken ))
                        .andExpect(status().isOk())
                        .andReturn();

        // then
        String stringResult = result.getResponse().getContentAsString();
        List<Recipe> listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(2, listResult.size());
    }

}

package com.thg.accelerator.recipesbackend.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.thg.accelerator.recipesbackend.entity.Ingredient;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IngredientsControllerUTest {
    private static String testToken;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    RecipeService recipeService;
    @MockBean
    IngredientService ingredientService;
    ObjectMapper objectMapper = new ObjectMapper();

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

    @Test
    void findAll() throws Exception {
        // given
        List<Ingredient> ingredients = getListIngredients();

        // when
        when(ingredientService.findAll()).thenReturn(ingredients);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ingredients")
                        .header("Authorization", "Bearer " + testToken))
                        .andExpect(status().isOk())
                        .andReturn();

        // then

        String stringResult = result.getResponse().getContentAsString();
        List<Ingredient> listResult = objectMapper.readValue(stringResult, List.class);

        Assertions.assertEquals(2, listResult.size());
    }
}


package com.thg.accelerator.recipesbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RECIPES")
public final class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private String method;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "recipe_ingredients",
            joinColumns = {
                    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
            @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    }
    )
    private List<Ingredient> ingredients;

    private String ingredientsAndQuantities;
    private String prepTime;
    private String cookTime;
    private String servings;
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;
    private String image;
}

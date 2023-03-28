package com.thg.accelerator.recipesbackend.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public IngredientNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

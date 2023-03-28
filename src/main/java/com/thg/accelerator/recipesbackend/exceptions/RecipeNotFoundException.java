package com.thg.accelerator.recipesbackend.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public RecipeNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}

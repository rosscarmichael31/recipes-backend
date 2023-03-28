package com.thg.accelerator.recipesbackend.repository;

import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import com.thg.accelerator.recipesbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserDatabaseRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsername(String username);
    @Query("""
            SELECT u.favouriteRecipes FROM User u 
            WHERE u.username = :username
           """)
    Set<Recipe> findFavoriteRecipesByUsername(String username);

    @Query("""
            SELECT u.fridgeList FROM User u 
            WHERE u.username = :username
           """)
    Set<Ingredient> findFridgeListByUsername(String username);
}

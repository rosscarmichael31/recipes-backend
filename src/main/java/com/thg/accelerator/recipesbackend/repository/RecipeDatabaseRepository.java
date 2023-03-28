package com.thg.accelerator.recipesbackend.repository;

import com.thg.accelerator.recipesbackend.entity.Ingredient;
import com.thg.accelerator.recipesbackend.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RecipeDatabaseRepository extends JpaRepository<Recipe, Long> {
    @Query("""
            SELECT DISTINCT r FROM Recipe r
            WHERE r.id = :id
            """)
    Optional<Recipe> findById(UUID id);

    @Query(""" 
             SELECT r.id FROM Recipe r
             JOIN r.ingredients i WHERE i IN ?1
             GROUP BY r.id
             ORDER BY COUNT(DISTINCT i) DESC, r.id
            """)
    Page<UUID> findRecipeIdsByIngredients(List<Ingredient> ingredients, Pageable pageable);


    void deleteRecipeById(UUID id);

    @Query(value = "SELECT * FROM RECIPES ORDER BY random() LIMIT 1", nativeQuery = true)
    Recipe findRandom();

    @Query(value = "SELECT r.id FROM RECIPES r ORDER BY random() LIMIT :number", nativeQuery = true)
    List<UUID> findRandomIds(int number);


    @Query(""" 
         SELECT r.id FROM Recipe r
         WHERE LOWER(r.name) LIKE CONCAT('%', LOWER(:wordInput), '%')
         GROUP BY r.id
         ORDER BY COUNT(DISTINCT r.name) DESC, r.id
        """)
    Page<UUID> findRecipeIdsByTitle(String wordInput, Pageable pageable);

}

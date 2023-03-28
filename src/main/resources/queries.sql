-- Selection
SELECT * FROM recipes r;
SELECT * FROM ingredients i;
SELECT * from recipe_ingredients ri;

-- Table data deletion
DELETE FROM recipe_ingredients;
DELETE FROM recipes;
DELETE FROM ingredients;

-- Alter types
ALTER TABLE recipes ALTER COLUMN ingredients_and_quantities SET DATA TYPE VARCHAR(62000);
ALTER TABLE recipes ALTER COLUMN method SET DATA TYPE VARCHAR(62000);
ALTER TABLE recipes ALTER COLUMN description SET DATA TYPE VARCHAR(62000);

-- DB Deletion
DROP DATABASE recipes_app;
DROP DATABASE recipes_app_dev;
DROP DATABASE recipes_app_test;

-- Creation
CREATE DATABASE recipes_app;
CREATE DATABASE recipes_app_dev;
CREATE DATABASE recipes_app_test;

-- Check for duplicates in ingredients
SELECT name, COUNT(*) as count
FROM ingredients
GROUP BY name
HAVING COUNT(*) > 1;

-- Check for duplicates in recipes
SELECT name, COUNT(*) as count
FROM recipes
GROUP BY name
HAVING COUNT(*) > 1;

-- Count entries
SELECT COUNT(*)
FROM recipes;

SELECT COUNT(*)
FROM ingredients;

SELECT COUNT(*)
FROM recipe_ingredients;

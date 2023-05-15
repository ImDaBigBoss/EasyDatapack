package com.github.imdabigboss.easydatapack.api.types.recipies;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * This class is used to manage custom recipes.
 */
public interface RecipeManager {
    /**
     * Gets a list of all registered custom recipes.
     * @return a list of all registered custom recipes
     */
    @NonNull List<NamespacedKey> getRecipeNamespaceKeys();

    /**
     * Gets a custom recipe from a {@link NamespacedKey}.
     * @param namespaceKey the namespace key of the custom recipe
     * @return the custom recipe with the given key. Will be null if the recipe does not exist.
     */
    @Nullable Recipe getRecipe(@NonNull NamespacedKey namespaceKey);
}

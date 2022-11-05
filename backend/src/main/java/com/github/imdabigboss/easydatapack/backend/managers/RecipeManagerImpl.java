package com.github.imdabigboss.easydatapack.backend.managers;

import com.github.imdabigboss.easydatapack.api.exceptions.CustomRecipeException;
import com.github.imdabigboss.easydatapack.api.managers.RecipeManager;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeManagerImpl implements RecipeManager {
    private final EasyDatapack datapack;

    private final List<NamespacedKey> recipes = new ArrayList<>();

    public RecipeManagerImpl(EasyDatapack datapack) {
        this.datapack = datapack;
    }

    public void registerCustomRecipe(Recipe recipe) throws CustomRecipeException {
        if (recipe instanceof Keyed keyed) {
            if (this.datapack.getServer().addRecipe(recipe)) {
                this.recipes.add(keyed.getKey());
            } else {
                throw new CustomRecipeException("Failed to register recipe: " + keyed.getKey());
            }
        } else {
            throw new CustomRecipeException("Recipe must be keyed so it can be added");
        }
    }

    public void unregisterAllRecipes() throws CustomRecipeException {
        for (NamespacedKey key : this.recipes) {
            this.datapack.getServer().removeRecipe(key);
        }
    }

    @Override
    public @NonNull List<NamespacedKey> getRecipeNamespaceKeys() {
        return new ArrayList<>(this.recipes);
    }

    @Override
    public Recipe getRecipe(@NonNull NamespacedKey namespaceKey) {
        return this.datapack.getServer().getRecipe(namespaceKey);
    }
}

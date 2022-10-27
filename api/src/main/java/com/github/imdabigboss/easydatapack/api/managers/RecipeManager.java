package com.github.imdabigboss.easydatapack.api.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;

import java.util.List;

public interface RecipeManager {
    List<NamespacedKey> getRecipeNamespaceKeys();

    Recipe getRecipe(NamespacedKey namespaceKey);
}

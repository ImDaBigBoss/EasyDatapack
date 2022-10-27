package com.github.imdabigboss.easydatapack.backend.managers;

import com.github.imdabigboss.easydatapack.api.enchantments.CustomEnchantment;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomEnchantmentException;
import com.github.imdabigboss.easydatapack.api.managers.EnchantmentManager;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import com.github.imdabigboss.easydatapack.backend.utils.LoreUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnchantmentManagerImpl implements Listener, EnchantmentManager {
    protected final EasyDatapack datapack;

    private final Map<String, CustomEnchantment> enchantments = new HashMap<>();

    public EnchantmentManagerImpl(EasyDatapack datapack) {
        this.datapack = datapack;
    }

    public void registerCustomEnchantment(CustomEnchantment enchantment) throws CustomEnchantmentException {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(enchantment);
        enchantments.put(enchantment.getNamespace(), enchantment);

        if (!registered) {
            this.registerEnchantment(enchantment);
        } else {
            throw new CustomEnchantmentException("Enchantment already registered: \"" + enchantment.getKey() + "\"");
        }
    }

    private void registerEnchantment(Enchantment enchantment) throws CustomEnchantmentException {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);

            Enchantment.registerEnchantment(enchantment);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new CustomEnchantmentException("Failed to register enchantment \"" + enchantment.getKey() + "\": Field value failure.", e);
        } catch (IllegalStateException e) {
            throw new CustomEnchantmentException("Failed to register enchantment \"" + enchantment.getKey() + "\": Not accepting new.", e);
        }
    }

    public void unregisterEnchantments() {
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            for (Enchantment enchantment : this.enchantments.values()) {
                byKey.remove(enchantment.getKey());
            }

            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            for (Enchantment enchantment : this.enchantments.values()) {
                byName.remove(enchantment.getName());
            }
        } catch (Exception ignored) {
        }
    }

    public void registerEventListeners() {
        for (CustomEnchantment enchantment : this.enchantments.values()) {
            if (enchantment.getEventListener() != null) {
                try {
                    Listener listener = enchantment.getEventListener().getConstructor(CustomEnchantment.class).newInstance(enchantment);
                    this.datapack.getServer().getPluginManager().registerEvents(listener, this.datapack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void reformatItemNameColours(ItemStack original, ItemStack result) {
        if (!original.hasItemMeta() || !result.hasItemMeta()) {
            return;
        }
        ItemMeta originalMeta = original.getItemMeta();
        ItemMeta resultMeta = result.getItemMeta();

        if (!originalMeta.hasDisplayName()) {
            return;
        }

        String originalName = resultMeta.getDisplayName();

        String name;
        if (resultMeta.hasDisplayName()) {
            String tmpName = originalName;
            name = resultMeta.getDisplayName();

            while (tmpName.contains(ChatColor.COLOR_CHAR + "")) {
                int index = tmpName.indexOf(ChatColor.COLOR_CHAR + "");

                if (index == 0) {
                    tmpName = tmpName.substring(2);
                    name = name.substring(1);
                } else {
                    tmpName = tmpName.substring(0, index) + name.substring(index + 2);
                    name = name.substring(0, index) + name.substring(index + 1);
                }
            }
        } else {
            name = ChatColor.stripColor(originalName);
        }

        resultMeta.setDisplayName(name);
        result.setItemMeta(resultMeta);
    }

    @Override
    public List<CustomEnchantment> getEnchantments() {
        return new ArrayList<>(enchantments.values());
    }

    private boolean enchantItemStack(ItemStack item, CustomEnchantment enchantment, int level, boolean force) {
        if (item == null) {
            return false;
        }
        if (!item.hasItemMeta()) {
            item.setItemMeta(this.datapack.getServer().getItemFactory().getItemMeta(item.getType()));
        }
        if (!force && !enchantment.canEnchantItem(item)) {
            return false;
        }

        this.removeEnchantItemStack(item, enchantment);

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof EnchantmentStorageMeta enchantMeta) {
            if (!enchantMeta.addStoredEnchant(enchantment, level, true)) {
                return false;
            }
        } else {
            if (!meta.addEnchant(enchantment, level, true)) {
                return false;
            }
        }
        item.setItemMeta(meta);

        this.updateItemLoreEnchants(item);

        return true;
    }

    private void removeEnchantItemStack(ItemStack item, CustomEnchantment enchantment) {
        if (item == null || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasEnchant(enchantment)) {
            return;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.remove(enchantment.getKey());

        if (meta instanceof EnchantmentStorageMeta enchantmentMeta) {
            enchantmentMeta.removeStoredEnchant(enchantment);
        } else {
            meta.removeEnchant(enchantment);
        }

        item.setItemMeta(meta);
    }

    private boolean isEnchantable(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        return item.getType() == Material.ENCHANTED_BOOK || Stream.of(EnchantmentTarget.values()).anyMatch(target -> target.includes(item));
    }

    private Map<Enchantment, Integer> getItemEnchants(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (meta instanceof EnchantmentStorageMeta enchantmentMeta) {
            return enchantmentMeta.getStoredEnchants();
        } else {
            return meta.getEnchants();
        }
    }

    private Map<CustomEnchantment, Integer> getItemCustomEnchants(ItemStack item) {
        Map<CustomEnchantment, Integer> customEnchants = new HashMap<>();
        for (Map.Entry<Enchantment, Integer> entry : this.getItemEnchants(item).entrySet()) {
            if (entry.getKey() instanceof CustomEnchantment) {
                if (customEnchants.containsKey(entry.getKey())) {
                    int maxLevel = Math.max(customEnchants.get(entry.getKey()), entry.getValue());
                    customEnchants.put((CustomEnchantment) entry.getKey(), maxLevel);
                } else {
                    customEnchants.put((CustomEnchantment) entry.getKey(), entry.getValue());
                }
            }
        }

        return customEnchants;
    }

    @Override
    public void updateItemLoreEnchants(ItemStack item) {
        for (CustomEnchantment enchantment : this.enchantments.values()) {
            LoreUtil.delLore(item, enchantment.getKey().getKey());
        }

        for (Map.Entry<CustomEnchantment, Integer> entry : this.getItemCustomEnchants(item).entrySet()) {
            CustomEnchantment enchantment = entry.getKey();
            int level = entry.getValue();

            LoreUtil.addLore(item, enchantment.getKey().getKey(), ChatColor.GRAY + enchantment.formatEnchantmentName(level), 0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareAnvilEvent(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();

        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);
        ItemStack result = event.getResult();

        if (first == null || !this.isEnchantable(first) || first.getAmount() > 1) {
            return;
        }

        if ((second == null || second.getType() == Material.AIR || !this.isEnchantable(second)) && (result != null && result.getType() == first.getType())) {
            ItemStack newResult = result.clone();

            this.getItemCustomEnchants(first).forEach((hasEach, hasLevel) -> {
                this.enchantItemStack(newResult, hasEach, hasLevel, true);
            });
            this.updateItemLoreEnchants(newResult);
            this.reformatItemNameColours(first, newResult);

            event.setResult(newResult);
            return;
        }

        if (second == null || second.getAmount() > 1 || !this.isEnchantable(second)) {
            return;
        }

        if (first.getType() == Material.ENCHANTED_BOOK && second.getType() != first.getType()) {
            return;
        }

        if (result == null || result.getType() == Material.AIR) {
            result = first.clone();
        }

        Map<CustomEnchantment, Integer> customEnchants = this.getItemCustomEnchants(first);
        int repairCost = inventory.getRepairCost();

        if (second.getType() == Material.ENCHANTED_BOOK || second.getType() == first.getType()) {
            for (Map.Entry<CustomEnchantment, Integer> en : this.getItemCustomEnchants(second).entrySet()) {
                if (customEnchants.containsKey(en.getKey())) {
                    int oldLevel = customEnchants.get(en.getKey());
                    if (oldLevel == en.getValue()) {
                        customEnchants.put(en.getKey(), oldLevel + 1);
                    } else {
                        customEnchants.replace(en.getKey(), Math.max(oldLevel, en.getValue()));
                    }
                } else {
                    customEnchants.put(en.getKey(), en.getValue());
                }
            }
        }

        for (Map.Entry<CustomEnchantment, Integer> ent : customEnchants.entrySet()) {
            CustomEnchantment enchant = ent.getKey();
            int level = Math.min(enchant.getMaxLevel(), ent.getValue());

            if (this.enchantItemStack(result, enchant, level, false)) {
                repairCost += enchant.getAnvilMergeCost(level);
            }
        }

        if (!first.equals(result)) {
            this.updateItemLoreEnchants(result);
            this.reformatItemNameColours(first, result);
            event.setResult(result);

            int newRepairCost = repairCost;
            this.datapack.getServer().getScheduler().runTask(this.datapack, () -> inventory.setRepairCost(newRepairCost));
        }
    }

    private void updateGrindstone(Inventory inventory) {
        this.datapack.getServer().getScheduler().runTask(this.datapack, () -> {
            ItemStack result = inventory.getItem(2);
            if (result == null || result.getType() == Material.AIR) {
                return;
            }

            Map<CustomEnchantment, Integer> curses = new HashMap<>();
            for (int slot = 0; slot < 2; slot++) {
                ItemStack source = inventory.getItem(slot);

                if (source == null || source.getType() == Material.AIR) {
                    continue;
                }

                curses.putAll(this.getItemCustomEnchants(source));
            }
            curses.entrySet().removeIf(entry -> !entry.getKey().isCursed());

            curses.forEach((enchant, level) -> this.enchantItemStack(result, enchant, level, true));
            this.updateItemLoreEnchants(result);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDragEvent(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getType() == InventoryType.GRINDSTONE) {
            this.updateGrindstone(inventory);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getType() == InventoryType.GRINDSTONE) {
            if (event.getRawSlot() == 2) {
                return;
            }

            this.updateGrindstone(inventory);
        }
    }
}

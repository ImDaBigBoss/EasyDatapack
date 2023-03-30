package com.github.imdabigboss.easydatapack.backend.managers;

import com.github.imdabigboss.easydatapack.api.enchantments.CustomEnchantment;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomEnchantmentException;
import com.github.imdabigboss.easydatapack.api.items.CustomItem;
import com.github.imdabigboss.easydatapack.api.items.CustomToolItem;
import com.github.imdabigboss.easydatapack.api.managers.EnchantmentManager;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import com.github.imdabigboss.easydatapack.backend.enchantments.CustomEnchantmentImpl;
import com.github.imdabigboss.easydatapack.backend.utils.GenericManager;
import com.github.imdabigboss.easydatapack.backend.utils.LoreUtil;
import net.kyori.adventure.util.TriState;
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
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class EnchantmentManagerImpl extends GenericManager implements EnchantmentManager {
    private final Map<String, CustomEnchantment> enchantments = new HashMap<>();

    public EnchantmentManagerImpl(EasyDatapack datapack) {
        super(datapack);
    }

    @Override
    public void registerBuilders() {
        this.datapack.registerBuilder(CustomEnchantment.Builder.class, CustomEnchantmentImpl.BuilderImpl.class);
    }

    public void registerCustomEnchantment(CustomEnchantment enchantment) throws CustomEnchantmentException {
        boolean registered = Arrays.stream(Enchantment.values()).toList().contains(enchantment.asEnchantment());
        enchantments.put(enchantment.getNamespace(), enchantment);

        if (!registered) {
            this.registerEnchantment(enchantment.asEnchantment());
        } else {
            throw new CustomEnchantmentException("Enchantment already registered: \"" + enchantment.asEnchantment().getKey() + "\"");
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

            for (CustomEnchantment enchantment : this.enchantments.values()) {
                byKey.remove(enchantment.asEnchantment().getKey());
            }

            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            for (CustomEnchantment enchantment : this.enchantments.values()) {
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
    public void reformatItemNameColours(@NonNull ItemStack result) {
        if (!result.hasItemMeta()) {
            return;
        }

        ItemMeta resultMeta = result.getItemMeta();

        String finalName = ChatColor.stripColor(resultMeta.getDisplayName());
        resultMeta.setDisplayName(finalName);

        result.setItemMeta(resultMeta);
    }

    @Override
    public @NonNull List<CustomEnchantment> getEnchantments() {
        return new ArrayList<>(enchantments.values());
    }

    private boolean enchantItemStack(ItemStack item, Enchantment enchantment, int level, boolean force) {
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

    private void removeEnchantItemStack(ItemStack item, Enchantment enchantment) {
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
    public void updateItemLoreEnchants(@NonNull ItemStack item) {
        for (CustomEnchantment enchantment : this.enchantments.values()) {
            LoreUtil.delLore(item, enchantment.asEnchantment().getKey().getKey());
        }

        for (Map.Entry<CustomEnchantment, Integer> entry : this.getItemCustomEnchants(item).entrySet()) {
            CustomEnchantment enchantment = entry.getKey();
            int level = entry.getValue();

            LoreUtil.addLore(item, enchantment.asEnchantment().getKey().getKey(), ChatColor.GRAY + enchantment.formatEnchantmentName(level), 0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareAnvilEvent(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();

        ItemStack first = inventory.getItem(0);
        ItemStack second = inventory.getItem(1);
        ItemStack result = event.getResult();

        if (result != null) { //Just do this anyway because the anvil removes the color char making the name strange
            this.reformatItemNameColours(result);
        }

        if (!this.isEnchantable(first) || first.getAmount() > 1) {
            return;
        }

        if ((second == null || second.getType() == Material.AIR || !this.isEnchantable(second)) && (result != null && result.getType() == first.getType())) {
            ItemStack newResult = result.clone();

            this.getItemCustomEnchants(first).forEach((hasEach, hasLevel) -> {
                this.enchantItemStack(newResult, hasEach.asEnchantment(), hasLevel, true);
            });
            this.updateItemLoreEnchants(newResult);
            this.reformatItemNameColours(newResult);

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

        CustomItem customFirst = null;
        if (first.hasItemMeta() && first.getItemMeta().hasCustomModelData()) {
            customFirst = this.datapack.getItemManager().getCustomItem(first.getItemMeta().getCustomModelData());
        }

        Map<Enchantment, Integer> existingEnchantments = this.getItemEnchants(first);
        Map<Enchantment, Integer> appliedEnchantments = new HashMap<>();
        List<Enchantment> forcedEnchantments = new ArrayList<>();
        int repairCost = inventory.getRepairCost();

        if (second.getType() == Material.ENCHANTED_BOOK || second.getType() == first.getType()) {
            for (Map.Entry<Enchantment, Integer> en : this.getItemEnchants(second).entrySet()) {
                if (en.getKey() instanceof CustomEnchantment customEnchantment) {
                    if (existingEnchantments.containsKey(en.getKey())) {
                        int oldLevel = existingEnchantments.get(en.getKey());
                        if (oldLevel == en.getValue()) {
                            appliedEnchantments.put(customEnchantment.asEnchantment(), oldLevel + 1);
                        } else {
                            appliedEnchantments.put(customEnchantment.asEnchantment(), Math.max(oldLevel, en.getValue()));
                        }
                    } else {
                        appliedEnchantments.put(customEnchantment.asEnchantment(), en.getValue());
                    }
                }

                if (customFirst != null) {
                    TriState canEnchant = customFirst.canEnchant(en.getKey());
                    if (canEnchant == TriState.FALSE) {
                        this.removeEnchantItemStack(result, en.getKey());
                    } else if (canEnchant == TriState.TRUE) {
                        appliedEnchantments.put(en.getKey(), en.getValue());
                        forcedEnchantments.add(en.getKey());
                    }
                }
            }
        }

        for (Map.Entry<Enchantment, Integer> ent : appliedEnchantments.entrySet()) {
            Enchantment enchant = ent.getKey();
            int level = Math.min(enchant.getMaxLevel(), ent.getValue());

            boolean force = forcedEnchantments.contains(enchant);

            if (!this.enchantItemStack(result, enchant, level, force)) {
                continue;
            }

            if (enchant instanceof CustomEnchantment customEnchantment) {
                repairCost += customEnchantment.getAnvilMergeCost(level);
            } else {
                if (customFirst instanceof CustomToolItem toolItem) {
                    toolItem.reformatLore(result);
                }
            }
        }

        if (!first.equals(result)) {
            this.updateItemLoreEnchants(result);
            this.reformatItemNameColours(result);
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

            curses.forEach((enchant, level) -> this.enchantItemStack(result, enchant.asEnchantment(), level, true));
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

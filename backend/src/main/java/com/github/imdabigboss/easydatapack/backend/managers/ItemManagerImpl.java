package com.github.imdabigboss.easydatapack.backend.managers;

import com.github.imdabigboss.easydatapack.api.exceptions.CustomItemException;
import com.github.imdabigboss.easydatapack.api.exceptions.EasyDatapackException;
import com.github.imdabigboss.easydatapack.api.items.CustomHatItem;
import com.github.imdabigboss.easydatapack.api.items.CustomItem;
import com.github.imdabigboss.easydatapack.api.items.CustomToolItem;
import com.github.imdabigboss.easydatapack.api.managers.ItemManager;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;
import java.util.function.Consumer;

public class ItemManagerImpl implements Listener, ItemManager {
    private final EasyDatapack datapack;

    private final Map<Integer, CustomItem> items = new HashMap<>();

    public ItemManagerImpl(EasyDatapack datapack) {
        this.datapack = datapack;
    }

    public void registerCustomItem(CustomItem item) throws EasyDatapackException {
        if (this.items.containsKey(item.getCustomModelData())) {
            throw new CustomItemException("Custom model data " + item.getCustomModelData() + " is already registered for " + item.getNamespaceKey() + "!");
        }

        this.items.put(item.getCustomModelData(), item);
    }

    public void registerEventListeners() {
        for (CustomItem item : this.items.values()) {
            if (item.getEventListener() != null) {
                try {
                    Listener listener = item.getEventListener().getConstructor(CustomItem.class).newInstance(item);
                    this.datapack.getServer().getPluginManager().registerEvents(listener, this.datapack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public @NonNull List<CustomItem> getCustomItems() {
        return new ArrayList<>(this.items.values());
    }

    @Override
    public CustomItem getCustomItem(@NonNull String namespaceKey) {
        for (CustomItem item : this.items.values()) {
            if (item.getNamespaceKey().equals(namespaceKey)) {
                return item;
            }
        }

        return null;
    }

    @Override
    public CustomItem getCustomItem(int customModelData) {
        return this.items.get(customModelData);
    }

    @Override
    public ItemStack getItemStack(@NonNull String namespaceKey) {
        CustomItem item = this.getCustomItem(namespaceKey);
        return item == null ? null : item.getItemStack();
    }

    @Override
    public ItemStack getItemStack(int customModelData) {
        CustomItem item = this.getCustomItem(customModelData);
        return item == null ? null : item.getItemStack();
    }

    @Override
    public boolean isCustomHat(@NonNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        if (!item.getItemMeta().hasCustomModelData()) {
            return false;
        }

        int customModelData = item.getItemMeta().getCustomModelData();
        for (CustomItem customItem : this.items.values()) {
            if (customItem.getCustomModelData() == customModelData && customItem instanceof CustomHatItem) {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onPrepareSmithingEvent(PrepareSmithingEvent event) {
        ItemStack equipment = event.getInventory().getInputEquipment();
        ItemStack mineral = event.getInventory().getInputMineral();

        if (equipment == null || mineral == null) { //Don't do anything if either of the slots are empty
            return;
        }

        CustomItem customEquipment = null;
        if (equipment.hasItemMeta() && equipment.getItemMeta().hasCustomModelData()) {
            customEquipment = this.items.get(equipment.getItemMeta().getCustomModelData());
        }
        CustomItem customMineral = null;
        if (mineral.hasItemMeta() && mineral.getItemMeta().hasCustomModelData()) {
            customMineral = this.items.get(mineral.getItemMeta().getCustomModelData());
        }

        Recipe targetRecipe = null;
        Iterator<Recipe> recipeList = this.datapack.getServer().recipeIterator();
        while (recipeList.hasNext()) {
            if (recipeList.next() instanceof SmithingRecipe smithingRecipe) {
                List<Material> baseMaterial = null;
                List<ItemStack> baseItem = null;
                List<Material> additionMaterial = null;
                List<ItemStack> additionItem = null;

                if (smithingRecipe.getBase() instanceof RecipeChoice.ExactChoice baseChoice) {
                    if (baseChoice.getChoices().size() > 0) {
                        baseItem = baseChoice.getChoices();
                    }
                } else if (smithingRecipe.getBase() instanceof RecipeChoice.MaterialChoice baseChoice) {
                    if (baseChoice.getChoices().size() > 0) {
                        baseMaterial = baseChoice.getChoices();
                    }
                }

                if (smithingRecipe.getAddition() instanceof RecipeChoice.ExactChoice additionChoice) {
                    if (additionChoice.getChoices().size() > 0) {
                        additionItem = additionChoice.getChoices();
                    }
                } else if (smithingRecipe.getAddition() instanceof RecipeChoice.MaterialChoice additionChoice) {
                    if (additionChoice.getChoices().size() > 0) {
                        additionMaterial = additionChoice.getChoices();
                    }
                }

                if (customEquipment == null || !customEquipment.isNewItem()) {
                    if (baseMaterial != null) {
                        if (!baseMaterial.contains(equipment.getType())) {
                            continue;
                        }
                    } else if (baseItem != null) {
                        if (!baseItem.contains(equipment)) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                } else {
                    if (baseItem != null) {
                        boolean found = false;
                        for (ItemStack item : baseItem) {
                            if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
                                if (item.getItemMeta().getCustomModelData() == customEquipment.getCustomModelData()) {
                                    found = true;
                                    break;
                                }
                            }
                        }

                        if (!found) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }

                if (customMineral == null || !customMineral.isNewItem()) {
                    if (additionMaterial != null) {
                        if (!additionMaterial.contains(mineral.getType())) {
                            continue;
                        }
                    } else if (additionItem != null) {
                        if (!additionItem.contains(mineral)) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                } else {
                    if (additionItem != null) {
                        boolean found = false;
                        for (ItemStack item : additionItem) {
                            if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
                                if (item.getItemMeta().getCustomModelData() == customMineral.getCustomModelData()) {
                                    found = true;
                                    break;
                                }
                            }
                        }

                        if (!found) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }

                targetRecipe = smithingRecipe;
                break;
            }
        }

        if (targetRecipe != null) {
            ItemStack result = targetRecipe.getResult();
            ItemMeta resultMeta = result.getItemMeta();
            if (resultMeta == null) {
                resultMeta = this.datapack.getServer().getItemFactory().getItemMeta(result.getType());
            }

            Map<Enchantment, Integer> inputEnchantments = equipment.getEnchantments();
            for (Map.Entry<Enchantment, Integer> entry : inputEnchantments.entrySet()) {
                resultMeta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
            this.datapack.getEnchantmentManager().updateItemLoreEnchants(result);
            if (!equipment.getItemMeta().getDisplayName().startsWith(ChatColor.COLOR_CHAR + "")) {
                resultMeta.setDisplayName(equipment.getItemMeta().getDisplayName());
            }

            result.setItemMeta(resultMeta);
            event.setResult(result);
        } else if ((customEquipment != null && customEquipment.isNewItem()) || (customMineral != null && customMineral.isNewItem())) {
            event.setResult(null);
        }
    }

    @EventHandler
    public void onInventoryCreativeEvent(InventoryCreativeEvent event) {
        event.setCancelled(this.onInventoryClickEventInternal(event.getClickedInventory(), event.getCursor(), event.getSlotType(), event.getSlot(), event.getCurrentItem(), event.getWhoClicked()));
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        event.setCancelled(this.onInventoryClickEventInternal(event.getClickedInventory(), event.getCursor(), event.getSlotType(), event.getSlot(), event.getCurrentItem(), event.getWhoClicked()));
    }

    private boolean onInventoryClickEventInternal(Inventory inventory, ItemStack cursor, InventoryType.SlotType slotType, int slot, ItemStack current, HumanEntity whoClicked) {
        if (inventory == null) {
            return false;
        }
        if (!inventory.getType().equals(InventoryType.PLAYER) || slotType != InventoryType.SlotType.ARMOR) {
            return false;
        }
        if (cursor == null) {
            return false;
        }
        if (!this.isCustomHat(cursor)) {
            return false;
        }

        if (slot != 39) {
            return true;
        }

        if (current == null || current.getType() == Material.AIR) {
            whoClicked.getInventory().setHelmet(cursor.clone());
            cursor.setAmount(0);
        } else {
            ItemStack newCursor = current.clone();
            whoClicked.getInventory().setHelmet(cursor.clone());
            whoClicked.setItemOnCursor(newCursor);
        }

        return true;
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() == null) {
                return;
            }
            if (event.getItem().getItemMeta() == null) {
                return;
            }
            if (!event.getItem().getItemMeta().hasCustomModelData()) {
                return;
            }

            CustomItem customItem = this.items.get(event.getItem().getItemMeta().getCustomModelData());
            if (customItem == null) {
                return;
            }

            if (customItem instanceof CustomHatItem) {
                Player player = event.getPlayer();
                PlayerInventory playerInventory = player.getInventory();
                if (playerInventory.getHelmet() == null) {
                    event.setCancelled(true);

                    ItemStack newItem = event.getItem().clone();
                    newItem.setAmount(1);
                    playerInventory.setHelmet(newItem);
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        event.getItem().setAmount(event.getItem().getAmount() - 1);
                    }
                }

            }

            Consumer<PlayerInteractEvent> consumer = customItem.getItemUseEvent();
            if (consumer != null) {
                consumer.accept(event);
            }
        }
    }

    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getItemMeta() == null) {
                return;
            }
            if (!item.getItemMeta().hasCustomModelData()) {
                return;
            }

            CustomItem customItem = this.items.get(item.getItemMeta().getCustomModelData());
            if (customItem == null) {
                return;
            }

            if (customItem instanceof CustomToolItem customToolItem) {
                Consumer<EntityDamageByEntityEvent> consumer = customToolItem.getPlayerHitEntityEvent();
                if (consumer != null) {
                    consumer.accept(event);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getItemMeta() == null) {
            return;
        }
        if (!item.getItemMeta().hasCustomModelData()) {
            return;
        }

        CustomItem customItem = this.items.get(item.getItemMeta().getCustomModelData());
        if (customItem == null) {
            return;
        }

        if (customItem instanceof CustomToolItem customToolItem) {
            Consumer<BlockBreakEvent> consumer = customToolItem.getPlayerBreakBlockEvent();
            if (consumer != null) {
                consumer.accept(event);
            }
        }
    }
}

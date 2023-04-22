package com.github.imdabigboss.easydatapack.backend.entities;

import com.destroystokyo.paper.entity.ai.MobGoals;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.github.imdabigboss.easydatapack.api.entities.CustomEntity;
import com.github.imdabigboss.easydatapack.api.entities.CustomEntityGoal;
import com.github.imdabigboss.easydatapack.api.entities.EntityManager;
import com.github.imdabigboss.easydatapack.api.entities.model.EntityBone;
import com.github.imdabigboss.easydatapack.api.entities.model.EntityModel;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomEntityException;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import com.github.imdabigboss.easydatapack.backend.entities.model.EntityBoneImpl;
import com.github.imdabigboss.easydatapack.backend.entities.model.EntityModelImpl;
import com.github.imdabigboss.easydatapack.backend.utils.GenericManager;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public class EntityManagerImpl extends GenericManager implements EntityManager {
    private final Map<UUID, EntityModelImpl> spawnedEntities = new HashMap<>();
    private final Map<Integer, CustomEntityImpl> entities = new HashMap<>();

    public EntityManagerImpl(EasyDatapack datapack) {
        super(datapack);
    }

    @Override
    public void registerBuilders() {
        this.datapack.registerBuilder(CustomEntity.Builder.class, CustomEntityImpl.BuilderImpl.class);
        this.datapack.registerBuilder(EntityModel.Builder.class, EntityModelImpl.BuilderImpl.class);
        this.datapack.registerBuilder(EntityBone.Builder.class, EntityBoneImpl.BuilderImpl.class);
    }

    public void registerCustomEntity(CustomEntity entity) throws CustomEntityException {
        if (this.entities.containsKey(entity.getEggCustomModelData())) {
            throw new CustomEntityException("A custom entity with the custom model data " + entity.getEggCustomModelData() + " already exists!");
        }

        this.entities.put(entity.getEggCustomModelData(), (CustomEntityImpl) entity);
    }

    @Override
    public @NonNull List<CustomEntity> getCustomEntities() {
        return new ArrayList<>(this.entities.values());
    }

    @Override
    public void spawnCustomEntity(@NonNull CustomEntity entityIn, Location location) {
        CustomEntityImpl entity = (CustomEntityImpl) entityIn;
        if (!this.entities.containsValue(entity)) {
            throw new IllegalArgumentException("The entity " + entity.getName() + " is not registered!");
        }

        Pig root = (Pig) location.getWorld().spawnEntity(location, EntityType.PIG);

        root.setSilent(true);
        root.setLootTable(null);
        root.setAdult();
        root.setInvisible(false); //TODO: Change this
        root.setBreed(false);
        root.setGravity(true);
        root.setCollidable(true);

        root.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(entity.getHealth());
        root.setHealth(entity.getHealth());

        MobGoals mobGoals = this.datapack.getServer().getMobGoals();
        mobGoals.removeAllGoals(root);

        Map<Integer, CustomEntityGoal> goals = entity.getGoals();
        for (int priority : goals.keySet()) {
            CustomEntityGoal goal = goals.get(priority);
            mobGoals.addGoal(root, priority, new CustomEntityGoalWrapper(root, goal));
        }

        EntityModelImpl model = entity.spawn(root, location);
        this.spawnedEntities.put(root.getUniqueId(), model);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityRemove(EntityRemoveFromWorldEvent event) {
        if (this.spawnedEntities.containsKey(event.getEntity().getUniqueId())) {
            EntityModelImpl model = this.spawnedEntities.remove(event.getEntity().getUniqueId());
            model.despawn();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() == null || event.getItem().getItemMeta() == null || !event.getItem().getItemMeta().hasCustomModelData()) {
                return;
            }
            if (event.getClickedBlock() == null) {
                return;
            }

            CustomEntity customEntity = this.entities.get(event.getItem().getItemMeta().getCustomModelData());
            if (customEntity == null) {
                return;
            }

            event.setCancelled(true);

            Location spawnLocation = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().add(0.5, 0, 0.5);
            this.spawnCustomEntity(customEntity, spawnLocation);
        }
    }
}

package com.github.imdabigboss.easydatapack.backend.entities;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.github.imdabigboss.easydatapack.api.entities.CustomEntityGoal;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Pig;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class CustomEntityGoalWrapper implements Goal<Pig> {
    private final Mob mob;
    private final GoalKey<Pig> goalKey;
    private final CustomEntityGoal goal;
    private final EnumSet<GoalType> types;

    public CustomEntityGoalWrapper(@NonNull Mob mob, CustomEntityGoal goal) {
        this.mob = mob;
        this.goalKey = GoalKey.of(Pig.class, goal.getKey());
        this.goal = goal;
        this.types = goal.getTypes();
    }

    @Override
    public boolean shouldActivate() {
        return this.goal.shouldActivate(this.mob);
    }

    @Override
    public boolean shouldStayActive() {
        return this.goal.shouldStayActive(this.mob);
    }

    @Override
    public void start() {
        this.goal.start(this.mob);
    }

    @Override
    public void stop() {
        this.goal.stop(this.mob);
    }

    @Override
    public void tick() {
        this.goal.tick(this.mob);
    }

    @Override
    public @NotNull GoalKey<Pig> getKey() {
        return this.goalKey;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return this.types;
    }
}

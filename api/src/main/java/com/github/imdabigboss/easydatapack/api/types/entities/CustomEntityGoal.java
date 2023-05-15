package com.github.imdabigboss.easydatapack.api.types.entities;

import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumSet;
import java.util.List;

/**
 * This class is used to create custom entity goals.
 */
public abstract class CustomEntityGoal {
    private final NamespacedKey key;
    private final EnumSet<GoalType> types;

    /**
     * Creates a new custom entity goal.
     * @param key the namespace key of the goal used to identify it
     * @param types the types of the goal
     */
    public CustomEntityGoal(@NonNull NamespacedKey key, @NonNull GoalType... types) {
        this.key = key;
        if (types.length == 0) {
            this.types = EnumSet.of(GoalType.UNKNOWN_BEHAVIOR);
        } else {
            this.types = EnumSet.copyOf(List.of(types));
        }
    }

    /**
     * Gets the namespace key of the goal.
     * @return the namespace key of the goal
     */
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Gets the types of the goal.
     * @return the types of the goal
     */
    public EnumSet<GoalType> getTypes() {
        return types;
    }

    /**
     * @see <a href="https://jd.papermc.io/paper/1.19/com/destroystokyo/paper/entity/ai/Goal.html">The Paper API javadocs</a>
     */
    public abstract boolean shouldActivate(@NonNull Mob mob);

    /**
     * @see <a href="https://jd.papermc.io/paper/1.19/com/destroystokyo/paper/entity/ai/Goal.html">The Paper API javadocs</a>
     */
    public abstract boolean shouldStayActive(@NonNull Mob mob);

    /**
     * @see <a href="https://jd.papermc.io/paper/1.19/com/destroystokyo/paper/entity/ai/Goal.html">The Paper API javadocs</a>
     */
    public abstract void start(@NonNull Mob mob);

    /**
     * @see <a href="https://jd.papermc.io/paper/1.19/com/destroystokyo/paper/entity/ai/Goal.html">The Paper API javadocs</a>
     */
    public abstract void stop(@NonNull Mob mob);

    /**
     * @see <a href="https://jd.papermc.io/paper/1.19/com/destroystokyo/paper/entity/ai/Goal.html">The Paper API javadocs</a>
     */
    public abstract void tick(@NonNull Mob mob);
}

package me.emmy.practice.util;

import lombok.experimental.UtilityClass;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * @author Emmy
 * @project praxi
 * @date 30/08/2024 - 15:47
 */
@UtilityClass
public class WorldUtil {

    /**
     * Clear all entities in a world except for players.
     *
     * @param world the world to clear entities in
     * @return the amount of entities removed
     */
    public int clearEntities(World world) {
        int removed = 0;

        for (Entity entity : world.getEntities()) {
            if (entity.getType() == EntityType.PLAYER) {
                continue;
            }

            removed++;
            entity.remove();
        }

        return removed;
    }
}

package me.emmy.practice.essentials;

import me.emmy.practice.Practice;
import me.emmy.practice.essentials.event.SpawnTeleportEvent;
import me.emmy.practice.util.LocationUtil;
import me.emmy.practice.util.chat.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Essentials {

	private final Practice plugin = Practice.getInstance();
	private Location spawn;

	/**
	 * Constructor for the Essentials class
	 */
	public Essentials() {
		this.spawn = LocationUtil.deserialize(Practice.getInstance().getMainConfig().getStringOrDefault("ESSENTIAL.SPAWN_LOCATION", null));
	}

	/**
	 * Set the spawn location
	 *
	 * @param location The location to set the spawn to
	 */
	public void setSpawn(Location location) {
		spawn = location;

		if (spawn == null) {
			plugin.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", null);
		} else {
			plugin.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", LocationUtil.serialize(this.spawn));
		}

		try {
			plugin.getMainConfig().getConfiguration().save(plugin.getMainConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Teleport a player to the spawn location
	 *
	 * @param player The player to teleport
	 */
	public void teleportToSpawn(Player player) {
		Location location = spawn == null ? plugin.getServer().getWorlds().get(0).getSpawnLocation() : spawn;

		SpawnTeleportEvent event = new SpawnTeleportEvent(player, location);
		event.call();

		if (!event.isCancelled() && event.getLocation() != null) {
			player.teleport(event.getLocation());
		}
	}

	/**
	 * Clear all entities in a world
	 *
	 * @param world The world to clear entities in
	 * @return The amount of entities removed
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

	/**
	 * Clear all entities in a world
	 *
	 * @param world The world to clear entities in
	 * @param excluded The entity types to exclude from removal
	 * @return The amount of entities removed
	 */
	public int clearEntities(World world, EntityType... excluded) {
		int removed = 0;

		entityLoop:
		for (Entity entity : world.getEntities()) {
			if (entity instanceof Item) {
				removed++;
				entity.remove();
				continue entityLoop;
			}

			for (EntityType type : excluded) {
				if (entity.getType() == EntityType.PLAYER) {
					continue entityLoop;
				}

				if (entity.getType() == type) {
					continue entityLoop;
				}
			}

			removed++;
			entity.remove();
		}

		return removed;
	}

}

package me.emmy.practice.essentials.event;

import lombok.Getter;
import lombok.Setter;
import me.emmy.practice.util.BaseEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
public class SpawnTeleportEvent extends BaseEvent implements Cancellable {

	private final Player player;
	@Setter private Location location;
	@Setter private boolean cancelled;

	/**
	 * Constructor for the SpawnTeleportEvent
	 *
	 * @param player The player to teleport
	 * @param location The location to teleport the player to
	 */
	public SpawnTeleportEvent(Player player, Location location) {
		this.player = player;
		this.location = location;
	}
}

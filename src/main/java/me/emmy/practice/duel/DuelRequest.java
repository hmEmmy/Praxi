package me.emmy.practice.duel;

import me.emmy.practice.arena.Arena;
import me.emmy.practice.kit.Kit;
import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DuelRequest {

	@Getter private final UUID sender;
	@Getter private final UUID target;
	@Getter private final boolean party;
	@Getter @Setter private Kit kit;
	@Getter @Setter private Arena arena;
	private long timestamp = System.currentTimeMillis();

	DuelRequest(UUID sender, UUID target, boolean party) {
		this.sender = sender;
		this.target = target;
		this.party = party;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() - this.timestamp >= 30_000;
	}

	public void expire() {
		Player sender = Bukkit.getPlayer(this.sender);
		Player target = Bukkit.getPlayer(this.target);

		if (sender != null && target != null) {
			sender.sendMessage(ChatColor.RED + "Your " + kit.getName() + " duel request to " +
			                   target.getName() + " has expired!");

			target.sendMessage(ChatColor.RED + "The " + kit.getName() + " duel request sent by " +
			                   sender.getName() + " has expired!");
		}
	}

}

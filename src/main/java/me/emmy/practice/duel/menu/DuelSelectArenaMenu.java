package me.emmy.practice.duel.menu;

import me.emmy.practice.arena.Arena;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.util.ItemBuilder;
import me.emmy.practice.api.menu.Button;
import me.emmy.practice.api.menu.Menu;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import me.emmy.practice.arena.ArenaType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class DuelSelectArenaMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&9&lSelect an arena";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Profile profile = Profile.getByUuid(player.getUniqueId());

		Map<Integer, Button> buttons = new HashMap<>();

		for (Arena arena : Arena.getArenas()) {
			if (!arena.isSetup()) {
				continue;
			}

			if (!arena.getKits().contains(profile.getDuelProcedure().getKit().getName())) {
				continue;
			}

			if (profile.getDuelProcedure().getKit().getGameRules().isBuild()) {
				if (arena.getType() == ArenaType.SHARED) {
					continue;
				}

				if (arena.getType() != ArenaType.STANDALONE) {
					continue;
				}

				if (arena.isActive()) {
					continue;
				}
			}

			buttons.put(buttons.size(), new SelectArenaButton(arena));
		}

		return buttons;
	}

	@Override
	public void onClose(Player player) {
		if (!isClosedByMenu()) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.setDuelProcedure(null);
		}
	}

	@AllArgsConstructor
	private class SelectArenaButton extends Button {

		private Arena arena;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.PAPER)
					.name("&a&l" + arena.getName())
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.getByUuid(player.getUniqueId());

			// Update and request the procedure
			profile.getDuelProcedure().setArena(this.arena);
			profile.getDuelProcedure().send();

			// Set closed by menu
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			// Force close inventory
			player.closeInventory();
		}

	}

}

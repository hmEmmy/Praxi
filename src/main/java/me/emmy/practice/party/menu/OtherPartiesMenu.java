package me.emmy.practice.party.menu;

import me.emmy.practice.party.Party;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.util.chat.CC;
import me.emmy.practice.util.ItemBuilder;
import me.emmy.practice.api.menu.Button;
import me.emmy.practice.api.menu.pagination.PaginatedMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class OtherPartiesMenu extends PaginatedMenu {

	@Override
	public String getPrePaginatedTitle(Player player) {
		return "&9&lOther Parties";
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player player) {
		Profile profile = Profile.getByUuid(player.getUniqueId());

		Map<Integer, Button> buttons = new HashMap<>();

		Party.getParties().forEach(party -> {
			if (!party.equals(profile.getParty())) {
				buttons.put(buttons.size(), new PartyDisplayButton(party));
			}
		});

		return buttons;
	}

	@AllArgsConstructor
	public static class PartyDisplayButton extends Button {

		private Party party;

		@Override
		public ItemStack getButtonItem(Player player) {
			List<String> lore = new ArrayList<>();
			int added = 0;

			for (Player partyPlayer : party.getListOfPlayers()) {
				if (added >= 10) {
					break;
				}

				lore.add(CC.GRAY + " - " + CC.RESET + partyPlayer.getPlayer().getName());

				added++;
			}

			if (party.getPlayers().size() != added) {
				lore.add(CC.GRAY + " and " + (party.getPlayers().size() - added) + " others...");
			}

			return new ItemBuilder(Material.SKULL_ITEM)
					.name("&6Party of &r" + party.getLeader().getName())
					.amount(party.getPlayers().size())
					.durability(3)
					.lore(lore)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.getByUuid(player.getUniqueId());

			if (profile.getParty() != null) {
				if (!profile.getParty().equals(party)) {
					if (profile.getParty().getLeader().equals(player)) {
						player.chat("/duel " + party.getLeader().getName());
					} else {
						player.sendMessage(ChatColor.RED + "You are not the leader of your party.");
					}
				}
			}
		}

	}
}

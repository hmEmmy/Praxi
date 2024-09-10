package me.emmy.practice.party.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.locale.Locale;
import me.emmy.practice.party.Party;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.profile.ProfileState;
import me.emmy.practice.profile.hotbar.Hotbar;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class PartyCreateCommand extends BaseCommand {

	@Command(name = "party.create", aliases = {"p.create"}, permission = "practice.party.create")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (player.hasMetadata("frozen")) {
			player.sendMessage(CC.translate("&cYou cannot create a party while frozen."));
			return;
		}

		if (profile.getParty() != null) {
			player.sendMessage(CC.translate("&cYou already have a party."));
			return;
		}

		if (profile.getState() != ProfileState.LOBBY) {
			player.sendMessage(CC.translate("&cYou must be in the lobby to create a party."));
			return;
		}

		profile.setParty(new Party(player));

		Hotbar.giveHotbarItems(player);

		player.sendMessage(Locale.PARTY_CREATE.format());
	}
}

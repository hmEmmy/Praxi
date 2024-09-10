package me.emmy.practice.party.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.party.PartyPrivacy;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class PartyCloseCommand extends BaseCommand {

	@Command(name = "party.close", aliases = {"p.close", "p.lock"}, permission = "practice.party.close")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (profile.getParty() == null) {
			player.sendMessage(CC.translate("&cYou do not have a party."));
			return;
		}

		if (!profile.getParty().getLeader().equals(player)) {
			player.sendMessage(CC.translate("&cYou are not the leader of your party."));
			return;
		}

		profile.getParty().setPrivacy(PartyPrivacy.CLOSED);
		player.sendMessage(CC.translate("&aYour party is now closed."));
	}
}

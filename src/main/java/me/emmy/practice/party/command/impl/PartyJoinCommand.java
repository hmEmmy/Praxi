package me.emmy.practice.party.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.party.Party;
import me.emmy.practice.party.PartyPrivacy;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class PartyJoinCommand extends BaseCommand {

	@Command(name = "party.join", aliases = {"p.join"}, permission = "practice.party.join")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 1) {
			player.sendMessage(CC.translate("&cPlease specify the party leader."));
			return;
		}

		Player target = command.getSender().getServer().getPlayer(args[0]);

		if (target == null) {
			player.sendMessage(CC.translate("&cA player with that name could not be found."));
			return;
		}

		if (player.hasMetadata("frozen")) {
			player.sendMessage(CC.translate("&cYou cannot join a party while frozen."));
			return;
		}

		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (profile.getParty() != null) {
			player.sendMessage(CC.translate("&cYou are already in a party."));
			return;
		}

		Profile targetProfile = Profile.getByUuid(target.getUniqueId());
		Party party = targetProfile.getParty();

		if (party == null) {
			player.sendMessage(CC.translate("&cNo party found with that player."));
			return;
		}

		if (party.getPrivacy() == PartyPrivacy.CLOSED) {
			if (party.getInvite(player.getUniqueId()) == null) {
				player.sendMessage(CC.translate("&cYou have not been invited to that party."));
				return;
			}
		}

		if (party.getPlayers().size() >= 32) {
			player.sendMessage(CC.translate("&cThe party is full and cannot accept more players."));
			return;
		}

		party.join(player);
		player.sendMessage(CC.translate("&aYou have joined the party."));
	}
}

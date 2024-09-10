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
public class PartyInviteCommand extends BaseCommand {

	@Command(name = "party.invite", aliases = {"p.invite"}, permission = "practice.party.invite")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 1) {
			player.sendMessage(CC.translate("&cPlease specify a player to invite."));
			return;
		}

		Player target = command.getSender().getServer().getPlayer(args[0]);

		if (target == null) {
			player.sendMessage(CC.translate("&cA player with that name could not be found."));
			return;
		}

		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (profile.getParty() == null) {
			player.sendMessage(CC.translate("&cYou do not have a party."));
			return;
		}

		if (!profile.getParty().getLeader().equals(player)) {
			player.sendMessage(CC.translate("&cYou are not the leader of your party."));
			return;
		}

		if (profile.getParty().getInvite(target.getUniqueId()) != null) {
			player.sendMessage(CC.translate("&cThat player has already been invited to your party."));
			return;
		}

		if (profile.getParty().containsPlayer(target.getUniqueId())) {
			player.sendMessage(CC.translate("&cThat player is already in your party."));
			return;
		}

		if (profile.getParty().getPrivacy() == PartyPrivacy.OPEN) {
			player.sendMessage(CC.translate("&cThe party is Open. You do not need to invite players."));
			return;
		}

		Profile targetData = Profile.getByUuid(target.getUniqueId());

		if (targetData.isBusy()) {
			player.sendMessage(CC.translate(target.getDisplayName() + " &cis currently busy."));
			return;
		}

		profile.getParty().invite(target);
		player.sendMessage(CC.translate("&aInvitation sent to " + target.getDisplayName() + "."));
	}
}

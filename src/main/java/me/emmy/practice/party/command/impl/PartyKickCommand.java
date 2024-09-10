package me.emmy.practice.party.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class PartyKickCommand extends BaseCommand {

	@Command(name = "party.kick", aliases = {"p.kick"}, permission = "practice.party.kick")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 1) {
			player.sendMessage(CC.translate("&cPlease specify the player to kick."));
			return;
		}

		Player target = command.getSender().getServer().getPlayer(args[0]);

		if (target == null) {
			player.sendMessage(CC.translate("&cA player with that name could not be found."));
			return;
		}

		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (profile.getParty() == null) {
			player.sendMessage(CC.translate("&cYou are not in a party."));
			return;
		}

		if (!profile.getParty().getLeader().equals(player)) {
			player.sendMessage(CC.translate("&cYou are not the leader of your party."));
			return;
		}

		if (!profile.getParty().containsPlayer(target.getUniqueId())) {
			player.sendMessage(CC.translate("&cThat player is not a member of your party."));
			return;
		}

		if (player.equals(target)) {
			player.sendMessage(CC.translate("&cYou cannot kick yourself from your party."));
			return;
		}

		profile.getParty().leave(target, true);
		player.sendMessage(CC.translate("&aThe player has been kicked from your party."));
	}
}

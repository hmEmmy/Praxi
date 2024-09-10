package me.emmy.practice.duel.command;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.duel.DuelProcedure;
import me.emmy.practice.duel.DuelRequest;
import me.emmy.practice.duel.menu.DuelSelectKitMenu;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class DuelCommand extends BaseCommand {

	@Command(name = "duel")
	@Override
	public void onCommand(CommandArgs command) {
		Player sender = command.getPlayer();
		String[] args = command.getArgs();
		Player target = Bukkit.getPlayer(args[0]);

		if (target == null) {
			sender.sendMessage(CC.translate("&cA player with that name could not be found."));
			return;
		}

		if (sender.hasMetadata("frozen")) {
			sender.sendMessage(CC.translate("&cYou cannot duel while frozen."));
			return;
		}

		if (target.hasMetadata("frozen")) {
			sender.sendMessage(CC.translate("&cYou cannot duel a frozen player."));
			return;
		}

		if (sender.getUniqueId().equals(target.getUniqueId())) {
			sender.sendMessage(CC.translate("&cYou cannot duel yourself."));
			return;
		}

		Profile senderProfile = Profile.getByUuid(sender.getUniqueId());
		Profile targetProfile = Profile.getByUuid(target.getUniqueId());

		if (senderProfile.isBusy()) {
			sender.sendMessage(CC.translate("&cYou cannot duel right now."));
			return;
		}

		if (targetProfile.isBusy()) {
			sender.sendMessage(CC.translate(target.getDisplayName() + "&c is currently busy."));
			return;
		}

		if (!targetProfile.getOptions().receiveDuelRequests()) {
			sender.sendMessage(CC.translate("&cThat player is not accepting duel requests at the moment."));
			return;
		}

		DuelRequest duelRequest = targetProfile.getDuelRequest(sender);

		if (duelRequest != null) {
			if (!senderProfile.isDuelRequestExpired(duelRequest)) {
				sender.sendMessage(CC.translate("&cYou already sent that player a duel request."));
				return;
			}
		}

		if (senderProfile.getParty() != null && targetProfile.getParty() == null) {
			sender.sendMessage(CC.translate("&cYou cannot send a duel request to a player that is not in a party."));
			return;
		}

		if (senderProfile.getParty() == null && targetProfile.getParty() != null) {
			sender.sendMessage(CC.translate("&cYou cannot send a duel request to a player in a party."));
			return;
		}

		if (senderProfile.getParty() != null && targetProfile.getParty() != null) {
			if (senderProfile.getParty().equals(targetProfile.getParty())) {
				sender.sendMessage(CC.translate("&cYou cannot duel your own party."));
				return;
			}
		}

		DuelProcedure procedure = new DuelProcedure(sender, target, senderProfile.getParty() != null);
		senderProfile.setDuelProcedure(procedure);

		new DuelSelectKitMenu().openMenu(sender);
	}
}

package me.emmy.practice.match.command.user;

import me.emmy.practice.profile.Profile;
import me.emmy.practice.profile.ProfileState;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class SpectateCommand extends BaseCommand {

	@Command(name = "spectate", aliases = {"spec"}, permission = "practice.spectate")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();
		Player target = Bukkit.getPlayer(args[0]);

		if (player.hasMetadata("frozen")) {
			player.sendMessage(CC.translate("&cYou cannot spectate while frozen."));
			return;
		}

		if (target == null) {
			player.sendMessage(CC.translate("&cA player with that name could not be found."));
			return;
		}

		Profile playerProfile = Profile.getByUuid(player.getUniqueId());

		if (playerProfile.isBusy()) {
			player.sendMessage(CC.translate("&cYou must be in the lobby and not queueing to spectate."));
			return;
		}

		if (playerProfile.getParty() != null) {
			player.sendMessage(CC.translate("&cYou must leave your party to spectate a match."));
			return;
		}

		Profile targetProfile = Profile.getByUuid(target.getUniqueId());

		if (targetProfile == null || targetProfile.getState() != ProfileState.FIGHTING) {
			player.sendMessage(CC.translate("&cThat player is not in a match."));
			return;
		}

		if (!targetProfile.getOptions().allowSpectators()) {
			player.sendMessage(CC.translate("&cThat player is not allowing spectators."));
			return;
		}

		targetProfile.getMatch().addSpectator(player, target);
	}
}

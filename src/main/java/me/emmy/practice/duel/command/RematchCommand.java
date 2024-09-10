package me.emmy.practice.duel.command;

import me.emmy.practice.profile.Profile;
import me.emmy.practice.profile.meta.ProfileRematchData;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class RematchCommand extends BaseCommand {

	@Command(name = "rematch")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		if (player.hasMetadata("frozen")) {
			player.sendMessage(CC.translate("&cYou cannot duel while frozen."));
			return;
		}

		Profile profile = Profile.getByUuid(player.getUniqueId());
		ProfileRematchData rematchData = profile.getRematchData();

		if (rematchData == null) {
			player.sendMessage(CC.translate("&cYou do not have anyone to rematch."));
			return;
		}

		rematchData.validate();

		if (rematchData.isCancelled()) {
			player.sendMessage(CC.translate("&cYou can no longer send that player a rematch."));
			return;
		}

		if (rematchData.isReceive()) {
			rematchData.accept();
		} else {
			if (rematchData.isSent()) {
				player.sendMessage(CC.translate("&cYou have already sent a rematch to that player."));
				return;
			}

			rematchData.request();
		}
	}
}

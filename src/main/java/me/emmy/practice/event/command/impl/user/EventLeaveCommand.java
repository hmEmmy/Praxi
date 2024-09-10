package me.emmy.practice.event.command.impl.user;

import me.emmy.practice.event.game.EventGame;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.profile.ProfileState;
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
public class EventLeaveCommand extends BaseCommand {

	@Command(name = "event.leave", permission = "practice.event.leave")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (profile.getState() == ProfileState.EVENT) {
			EventGame.getActiveGame().getGameLogic().onLeave(player);
		} else {
			player.sendMessage(CC.translate("&cYou are not in an event."));
		}
	}
}

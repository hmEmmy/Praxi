package me.emmy.practice.event.command.impl.user;

import me.emmy.practice.event.game.EventGame;
import me.emmy.practice.event.game.EventGameState;
import me.emmy.practice.profile.Profile;
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
public class EventJoinCommand extends BaseCommand {

	@Command(name = "event.join", permission = "practice.event.join")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (profile.getParty() != null) {
			player.sendMessage(CC.translate("&cYou cannot join the event while in a command."));
			return;
		}

		if (profile.isBusy()) {
			player.sendMessage(CC.translate("&cYou must be in the lobby to join the event."));
		} else {
			EventGame game = EventGame.getActiveGame();

			if (game != null) {
				if (game.getGameState() == EventGameState.WAITING_FOR_PLAYERS ||
						game.getGameState() == EventGameState.STARTING_EVENT) {
					if (game.getParticipants().size() < game.getMaximumPlayers()) {
						game.getGameLogic().onJoin(player);
					} else {
						player.sendMessage(CC.translate("&cThe event is full."));
					}
				} else {
					player.sendMessage(CC.translate("&cThe event has already started."));
				}
			} else {
				player.sendMessage(CC.translate("&cThere is no active event."));
			}
		}
	}
}

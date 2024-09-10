package me.emmy.practice.event.command.impl.user;

import me.emmy.practice.event.game.EventGame;
import me.emmy.practice.event.impl.sumo.SumoEvent;
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
public class EventInfoCommand extends BaseCommand {

	@Command(name = "event.info", permission = "practice.event.info")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		if (EventGame.getActiveGame() == null) {
			player.sendMessage(CC.translate("&cThere is no active event."));
			return;
		}

		EventGame game = EventGame.getActiveGame();

		player.sendMessage(CC.translate("&6&lEvent Information"));
		player.sendMessage(CC.translate("&9State: &e" + game.getGameState().getReadable()));
		player.sendMessage(CC.translate("&9Players: &e" + game.getRemainingPlayers() +
				"/" + game.getMaximumPlayers()));

		if (game.getEvent() instanceof SumoEvent) {
			player.sendMessage(CC.translate("&9Round: &e" + game.getGameLogic().getRoundNumber()));
		}
	}
}

package me.emmy.practice.event.command.impl.user;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.event.game.EventGame;
import me.emmy.practice.event.game.EventGameState;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class EventForceStartCommand extends BaseCommand {

	@Command(name = "event.forcestart", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		if (EventGame.getActiveGame() != null) {
			EventGame game = EventGame.getActiveGame();

			if (game.getGameState() == EventGameState.WAITING_FOR_PLAYERS ||
					game.getGameState() == EventGameState.STARTING_EVENT) {
				game.getGameLogic().startEvent();
				game.getGameLogic().preStartRound();
				game.setGameState(EventGameState.STARTING_ROUND);
				game.getGameLogic().getGameLogicTask().setNextAction(4);
				player.sendMessage(CC.translate("&aThe event has been force started."));
			} else {
				player.sendMessage(CC.translate("&cThe event has already started."));
			}
		} else {
			player.sendMessage(CC.translate("&cThere is no active event."));
		}
	}
}
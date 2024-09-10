package me.emmy.practice.event.command.impl.user;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.event.game.EventGame;
import me.emmy.practice.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class EventCancelCommand extends BaseCommand {

	@Command(name = "event.cancel", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		CommandSender sender = command.getSender();

		if (EventGame.getActiveGame() != null) {
			EventGame.getActiveGame().getGameLogic().cancelEvent();
			sender.sendMessage(CC.translate("&aThe active event has been canceled."));
		} else {
			sender.sendMessage(CC.translate("&cThere is no active event."));
		}
	}
}

package me.emmy.practice.event.command.impl.admin;

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
public class EventHelpCommand extends BaseCommand {

	@Command(name = "event", aliases = {"event help"}, permission = "practice.event.help")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		player.sendMessage("");
		player.sendMessage(CC.translate("&7&m------------------------------------------------"));
		player.sendMessage(CC.translate("&6Event Help"));
		player.sendMessage(CC.translate("&9/events &7- &fShow events that can be hosted"));
		player.sendMessage(CC.translate("&9/event host <type> &7- &fHost an event by name"));
		player.sendMessage(CC.translate("&9/event info &7- &fShows active event information"));
		player.sendMessage(CC.translate("&9/event join &7- &fJoin the event"));
		player.sendMessage(CC.translate("&9/event leave &7- &fLeave the event"));
		player.sendMessage(CC.translate("&7&m------------------------------------------------"));
		player.sendMessage("");
	}
}

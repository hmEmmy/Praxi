package me.emmy.practice.event.command;

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
public class EventCommand extends BaseCommand {

	private static final String[] HELP = {
			"/event forcestart", "Force start the active event",
			"/event cancel", "Cancel the active event",
			"/event clearcd", "Clear the event cooldown",
			"/event set lobby <event>", "Set lobby location",
			"/event add map <event> <map>", "Allow a map to be played",
			"/event remove map <event> <map>", "Deny a map to be played",
			"/event map create <name>", "Create a map",
			"/event map delete <name>", "Delete a map",
			"/event map set spawn <name>", "Set a spawn point",
			"/event map status <map>", "Check the status of a map"
	};

	@Command(name = "event", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&6Event Admin"));

		for (String commandInfo : HELP) {
			player.sendMessage(CC.translate("&9" + commandInfo + " &7- &f" + commandInfo));
		}

		player.sendMessage(CC.CHAT_BAR);
	}
}

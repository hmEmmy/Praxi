package me.emmy.practice.event.command.impl.admin;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.event.Event;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class EventSetLobbyCommand extends BaseCommand {

	@Command(name = "event.setlobby", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length == 0) {
			player.sendMessage(ChatColor.RED + "Please specify an event.");
			return;
		}

		String eventName = args[0];
		Event event = Event.getEventByName(eventName);

		if (event != null) {
			event.setLobbyLocation(player.getLocation());
			event.save();

			player.sendMessage(ChatColor.GOLD + "You updated the " + ChatColor.GREEN + event.getDisplayName() +
					ChatColor.GOLD + " event's lobby location.");
		} else {
			player.sendMessage(ChatColor.RED + "An event with that name does not exist.");
		}
	}
}

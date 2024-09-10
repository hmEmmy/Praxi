package me.emmy.practice.event.command.impl.admin;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.event.Event;
import me.emmy.practice.event.game.map.EventGameMap;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class EventRemoveMapCommand extends BaseCommand {

	@Command(name = "event.remove.map", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 2) {
			player.sendMessage(CC.translate("&cUsage: /event remove map <event> <map>"));
			return;
		}

		String eventName = args[0];
		String mapName = args[1];

		Event event = Event.getEventByName(eventName);
		EventGameMap gameMap = EventGameMap.getByName(mapName);

		if (event == null) {
			player.sendMessage(CC.translate("&cAn event type by that name does not exist."));
			player.sendMessage(CC.translate("&cTypes: sumo, corners"));
			return;
		}

		if (gameMap == null) {
			player.sendMessage(CC.translate("&cA map with that name does not exist."));
			return;
		}

		if (event.getAllowedMaps().remove(gameMap.getMapName())) {
			event.save();

			player.sendMessage(CC.translate("&aYou successfully removed the \"" + gameMap.getMapName() +
					"\" map from the \"" + event.getDisplayName() + "\" event."));
		} else {
			player.sendMessage(CC.translate("&cThe map was not found in the event."));
		}
	}
}

package me.emmy.practice.event.command.impl.admin;

import me.emmy.practice.event.Event;
import me.emmy.practice.event.game.map.EventGameMap;
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
public class EventAddMapCommand extends BaseCommand {

	@Command(name = "event.add.map", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 2) {
			player.sendMessage(CC.translate("&cUsage: /event add map <event> <map>"));
			return;
		}

		String eventName = args[0];
		String mapName = args[1];

		EventGameMap gameMap = EventGameMap.getByName(mapName);
		Event event = Event.getEventByName(eventName);
		if (event == null) {
			player.sendMessage(CC.translate("&cAn event type by that name does not exist."));
			player.sendMessage(CC.translate("&cTypes: sumo, corners"));
			return;
		}

		if (gameMap == null) {
			player.sendMessage(CC.translate("&cA map with that name does not exist."));
			return;
		}

		if (event.getAllowedMaps().contains(gameMap.getMapName())) {
			player.sendMessage(CC.translate("&cThe map is already added to this event."));
			return;
		}

		event.getAllowedMaps().add(gameMap.getMapName());
		event.save();

		player.sendMessage(CC.translate("&aYou successfully added the map &2" + gameMap.getMapName() +
				"&a to the event &2" + event.getDisplayName() + "&a."));
	}
}

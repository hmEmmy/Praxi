package me.emmy.practice.event.command.impl.map;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.event.game.map.EventGameMap;
import me.emmy.practice.event.game.map.impl.SpreadEventGameMap;
import me.emmy.practice.event.game.map.impl.TeamEventGameMap;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class EventMapCreateCommand extends BaseCommand {

	@Command(name = "event.map.create", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 2) {
			player.sendMessage(CC.translate("&cUsage: /event map create <mapName> <mapType>"));
			return;
		}

		String mapName = args[0];
		String mapType = args[1];

		if (EventGameMap.getByName(mapName) != null) {
			player.sendMessage(CC.translate("&cAn event map with that name already exists."));
			return;
		}

		EventGameMap gameMap;

		if (mapType.equalsIgnoreCase("TEAM")) {
			gameMap = new TeamEventGameMap(mapName);
		} else if (mapType.equalsIgnoreCase("SPREAD")) {
			gameMap = new SpreadEventGameMap(mapName);
		} else {
			player.sendMessage(CC.translate("&cThat event map type is not valid. Pick either \"TEAM\" or \"SPREAD\"!"));
			return;
		}

		gameMap.save();
		EventGameMap.getMaps().add(gameMap);

		player.sendMessage(CC.translate("&aYou successfully created the event map \"&e" + mapName + "&a\"."));
	}
}

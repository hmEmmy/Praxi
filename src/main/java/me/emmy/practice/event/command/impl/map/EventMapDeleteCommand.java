package me.emmy.practice.event.command.impl.map;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.event.game.map.EventGameMap;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class EventMapDeleteCommand extends BaseCommand {

	@Command(name = "event.map.delete", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 1) {
			player.sendMessage(CC.translate("&cUsage: /event map delete <mapName>"));
			return;
		}

		String mapName = args[0];
		EventGameMap gameMap = EventGameMap.getByName(mapName);

		if (gameMap == null) {
			player.sendMessage(CC.translate("&cAn event map with that name does not exist."));
			return;
		}

		gameMap.delete();
		EventGameMap.getMaps().remove(gameMap);

		player.sendMessage(CC.translate("&aYou successfully deleted the event map \"&e" + mapName + "&a\"."));
	}
}

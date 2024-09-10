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
public class EventMapsCommand extends BaseCommand {

	@Command(name = "event.maps", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		player.sendMessage(CC.translate("&6&lEvent Maps"));

		if (EventGameMap.getMaps().isEmpty()) {
			player.sendMessage(CC.translate("&7There are no event maps."));
		} else {
			for (EventGameMap gameMap : EventGameMap.getMaps()) {
				player.sendMessage(CC.translate(" - " + (gameMap.isSetup() ? "&a" : "&c") + gameMap.getMapName()));
			}
		}
	}
}

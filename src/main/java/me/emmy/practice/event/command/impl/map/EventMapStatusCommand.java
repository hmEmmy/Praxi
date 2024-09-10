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
public class EventMapStatusCommand extends BaseCommand {

	@Command(name = "event.map.status", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 1) {
			player.sendMessage(CC.translate("&cUsage: /event map status <mapName>"));
			return;
		}

		String mapName = args[0];
		EventGameMap gameMap = EventGameMap.getByName(mapName);

		if (gameMap == null) {
			player.sendMessage(CC.translate("&cAn event map with that name does not exist."));
			return;
		}

		player.sendMessage(CC.translate("&6&lEvent Map Status " + (gameMap.isSetup() ? "&a" : "&c") + gameMap.getMapName()));
		player.sendMessage(CC.translate("&aSpectator Location: " + (gameMap.getSpectatorPoint() == null ? "&c✗" : "&a✓")));

		if (gameMap instanceof TeamEventGameMap) {
			TeamEventGameMap teamGameMap = (TeamEventGameMap) gameMap;
			player.sendMessage(CC.translate("&aSpawn A Location: " + (teamGameMap.getSpawnPointA() == null ? "&c✗" : "&a✓")));
			player.sendMessage(CC.translate("&aSpawn B Location: " + (teamGameMap.getSpawnPointB() == null ? "&c✗" : "&a✓")));
		} else if (gameMap instanceof SpreadEventGameMap) {
			SpreadEventGameMap spreadGameMap = (SpreadEventGameMap) gameMap;
			player.sendMessage(CC.translate("&aSpread Locations: " + (spreadGameMap.getSpawnLocations().isEmpty() ? "&c✗" : "&a✓")));
		}
	}
}

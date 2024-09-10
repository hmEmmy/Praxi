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
public class EventMapSetSpawnCommand extends BaseCommand {

	@Command(name = "event.map.setspawn", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 2) {
			player.sendMessage(CC.translate("&cUsage: /event map set spawn <map> <field>"));
			return;
		}

		String mapName = args[0];
		String field = args[1];
		EventGameMap map = EventGameMap.getByName(mapName);

		if (map == null) {
			player.sendMessage(CC.translate("&cAn event map with that name does not exist."));
			return;
		}

		switch (field.toLowerCase()) {
			case "spectator":
				map.setSpectatorPoint(player.getLocation());
				player.sendMessage(CC.translate("&aYou successfully updated " + map.getMapName() + "'s " + field + " location."));
				break;

			case "a":
				if (!(map instanceof TeamEventGameMap)) {
					player.sendMessage(CC.translate("&cThat type of map only has spread locations!"));
					player.sendMessage(CC.translate("&cTo add a location to the spread list, use /event map set <map> spread."));
					break;
				}
				TeamEventGameMap teamMap = (TeamEventGameMap) map;
				teamMap.setSpawnPointA(player.getLocation());
				player.sendMessage(CC.translate("&aYou successfully updated " + map.getMapName() + "'s " + field + " location."));
				break;

			case "b":
				if (!(map instanceof TeamEventGameMap)) {
					player.sendMessage(CC.translate("&cThat type of map only has spread locations!"));
					player.sendMessage(CC.translate("&cTo add a location to the spread list, use /event map set <map> spread."));
					break;
				}
				TeamEventGameMap teamMapB = (TeamEventGameMap) map;
				teamMapB.setSpawnPointB(player.getLocation());
				player.sendMessage(CC.translate("&aYou successfully updated " + map.getMapName() + "'s " + field + " location."));
				break;

			case "spread":
				if (!(map instanceof SpreadEventGameMap)) {
					player.sendMessage(CC.translate("&cThat type of map does not have spread locations!"));
					player.sendMessage(CC.translate("&cTo set one of the locations, use /event map set <map> <a/b>."));
					break;
				}
				SpreadEventGameMap spreadMap = (SpreadEventGameMap) map;
				spreadMap.getSpawnLocations().add(player.getLocation());
				player.sendMessage(CC.translate("&aYou successfully added a location to " + map.getMapName() + "'s " + field + " list."));
				break;

			default:
				player.sendMessage(CC.translate("&cA field by that name does not exist."));
				player.sendMessage(CC.translate("&cFields: spectator, a, b"));
				return;
		}

		map.save();
	}
}

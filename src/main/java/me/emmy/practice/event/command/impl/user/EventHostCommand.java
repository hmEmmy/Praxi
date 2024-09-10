package me.emmy.practice.event.command.impl.user;

import me.emmy.practice.Practice;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.event.Event;
import me.emmy.practice.event.game.EventGame;
import me.emmy.practice.event.game.map.EventGameMap;
import me.emmy.practice.event.game.map.vote.EventGameMapVoteData;
import me.emmy.practice.event.game.menu.EventHostMenu;
import me.emmy.practice.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class EventHostCommand extends BaseCommand {

	@Command(name = "host", aliases = {"event.host"}, permission = "practice.event.host")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length == 0) {
			handleMenu(player);
		} else if (args.length == 1) {
			handleEvent(player, args[0]);
		} else if (args.length == 2) {
			handleEventWithSlots(player, args[0], Integer.parseInt(args[1]));
		} else {
			player.sendMessage(CC.translate("&cUsage: /event host [event] [slots]"));
		}
	}

	private void handleMenu(Player player) {
		if (player.hasMetadata("frozen")) {
			player.sendMessage(CC.translate("&cYou cannot host an event while frozen."));
			return;
		}

		new EventHostMenu().openMenu(player);
	}

	private void handleEvent(Player player, String eventName) {
		if (player.hasMetadata("frozen")) {
			player.sendMessage(CC.translate("&cYou cannot host an event while frozen."));
			return;
		}

		if (EventGame.getActiveGame() != null) {
			player.sendMessage(CC.translate("&cThere is already an active event."));
			return;
		}

		if (!EventGame.getCooldown().hasExpired()) {
			player.sendMessage(CC.translate("&cThe event cooldown is active."));
			return;
		}

		Event event = Event.getEventByName(eventName);

		if (event == null) {
			player.sendMessage(CC.translate("&cThat type of event does not exist."));
			player.sendMessage(CC.translate("&cTypes: sumo, corners"));
			return;
		}

		if (EventGameMap.getMaps().isEmpty()) {
			player.sendMessage(CC.translate("&cThere are no available event maps."));
			return;
		}

		List<EventGameMap> validMaps = getValidMaps(event);

		if (validMaps.isEmpty()) {
			player.sendMessage(CC.translate("&cThere are no available event maps."));
			return;
		}

		try {
			EventGame game = new EventGame(event, player, getHostSlots(player));

			for (EventGameMap gameMap : validMaps) {
				game.getVotesData().put(gameMap, new EventGameMapVoteData());
			}

			game.broadcastJoinMessage();
			game.start();
			game.getGameLogic().onJoin(player);
		} catch (Exception exception) {
			Bukkit.getConsoleSender().sendMessage(CC.translate("&cAn error occurred while starting an event: " + exception.getMessage()));
		}
	}

	private void handleEventWithSlots(Player player, String eventName, int slots) {
		if (!player.hasPermission("practice.event.host.slots")) {
			if (slots < 4 || slots > 200) {
				player.sendMessage(CC.translate("&cEvents can only hold 4-200 players."));
				return;
			}
		}

		if (EventGame.getActiveGame() != null) {
			player.sendMessage(CC.translate("&cThere is already an active event."));
			return;
		}

		if (!EventGame.getCooldown().hasExpired()) {
			player.sendMessage(CC.translate("&cThe event cooldown is active."));
			return;
		}

		Event event = Event.getEventByName(eventName);

		if (event == null) {
			player.sendMessage(CC.translate("&cThat type of event does not exist."));
			player.sendMessage(CC.translate("&cTypes: sumo, corners"));
			return;
		}

		if (EventGameMap.getMaps().isEmpty()) {
			player.sendMessage(CC.translate("&cThere are no available event maps."));
			return;
		}

		List<EventGameMap> validMaps = getValidMaps(event);

		if (validMaps.isEmpty()) {
			player.sendMessage(CC.translate("&cThere are no available event maps."));
			return;
		}

		try {
			EventGame game = new EventGame(event, player, slots);

			for (EventGameMap gameMap : validMaps) {
				game.getVotesData().put(gameMap, new EventGameMapVoteData());
			}

			game.broadcastJoinMessage();
			game.start();
			game.getGameLogic().onJoin(player);
		} catch (Exception exception) {
			Bukkit.getConsoleSender().sendMessage(CC.translate("&cAn error occurred while starting an event: " + exception.getMessage()));
		}
	}

	private List<EventGameMap> getValidMaps(Event event) {
		List<EventGameMap> validMaps = new ArrayList<>();

		for (EventGameMap gameMap : EventGameMap.getMaps()) {
			if (event.getAllowedMaps().contains(gameMap.getMapName())) {
				validMaps.add(gameMap);
			}
		}

		return validMaps;
	}

	private int getHostSlots(Player host) {
		int slots = 32;
		FileConfiguration config = Practice.getInstance().getEventsConfig().getConfiguration();

		for (String key : config.getConfigurationSection("HOST_SLOTS").getKeys(false)) {
			if (host.hasPermission(config.getString("HOST_SLOTS." + key + ".PERMISSION"))) {
				if (config.getInt("HOST_SLOTS." + key + ".SLOTS") > slots) {
					slots = config.getInt("HOST_SLOTS." + key + ".SLOTS");
				}
			}
		}

		return slots;
	}
}

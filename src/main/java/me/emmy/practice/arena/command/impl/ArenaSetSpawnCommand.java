package me.emmy.practice.arena.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.arena.Arena;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * Command to set spawn points A or B for a specified arena.
 * Requires admin permissions.
 *
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 09:40
 */
public class ArenaSetSpawnCommand extends BaseCommand {
	@Override
	@Command(name = "arena.setspawn", permission = "practice.admin.command")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 2) {
			player.sendMessage(CC.translate("&cUsage: /arena setspawn <name> <a/b>"));
			return;
		}

		String arenaName = args[0];
		String pos = args[1];

		Arena arena = Arena.getByName(arenaName);
		if (arena == null) {
			player.sendMessage(CC.translate("&cAn arena with that name does not exist."));
			return;
		}

		switch (pos.toLowerCase()) {
			case "a":
				arena.setSpawnA(player.getLocation());
				break;
			case "b":
				arena.setSpawnB(player.getLocation());
				break;
			default:
				player.sendMessage(CC.translate("&cInvalid spawn point. Try \"a\" or \"b\"."));
				return;
		}

		arena.save();
		player.sendMessage(CC.translate("&6Updated spawn point &e" + pos + " &6for arena &e" + arena.getName() + "&6."));
	}
}

package me.emmy.practice.arena.command.impl;

import me.emmy.practice.arena.Arena;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 08:55
 */
public class ArenaDeleteCommand extends BaseCommand {
	@Override
	@Command(name = "arena.delete", permission = "practice.admin.command")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 1) {
			player.sendMessage(CC.translate("&cUsage: /arena delete <name>"));
			return;
		}

		String arenaName = args[0];
		Arena arena = Arena.getByName(arenaName);
		if (arena == null) {
			player.sendMessage(CC.translate("&cAn arena with that name does not exist."));
			return;
		}

		arena.delete();
		player.sendMessage(CC.translate("&6Successfully deleted arena &e" + arena.getName() + "&6."));
	}
}

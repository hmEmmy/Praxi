package me.emmy.practice.arena.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.arena.Arena;
import me.emmy.practice.util.chat.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

/**
 * Command to display the status of a specified arena.
 * Requires admin permissions.
 *
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 09:45
 */
public class ArenaStatusCommand extends BaseCommand {
	@Override
	@Command(name = "arena.status", permission = "practice.admin.command")
	public void onCommand(CommandArgs command) {
		CommandSender sender = command.getSender();
		String[] args = command.getArgs();

		if (args.length < 1) {
			sender.sendMessage(CC.translate("&cUsage: /arena status <name>"));
			return;
		}

		String arenaName = args[0];
		Arena arena = Arena.getByName(arenaName);
		if (arena == null) {
			sender.sendMessage(CC.translate("&cAn arena with that name does not exist."));
			return;
		}

		sender.sendMessage(CC.translate("&6&lArena Status &7(&r" +
				(arena.isSetup() ? "&a" : "&c") + arena.getName() + "&7)"));

		sender.sendMessage(CC.translate("&aCuboid Lower Location: &e" +
				(arena.getLowerCorner() == null ? "\u2717" : "\u2713")));

		sender.sendMessage(CC.translate("&aCuboid Upper Location: &e" +
				(arena.getUpperCorner() == null ? "\u2717" : "\u2713")));

		sender.sendMessage(CC.translate("&aSpawn A Location: &e" +
				(arena.getSpawnA() == null ? "\u2717" : "\u2713")));

		sender.sendMessage(CC.translate("&aSpawn B Location: &e" +
				(arena.getSpawnB() == null ? "\u2717" : "\u2713")));

		sender.sendMessage(CC.translate("&aKits: &e" + StringUtils.join(arena.getKits(), ", ")));
	}
}

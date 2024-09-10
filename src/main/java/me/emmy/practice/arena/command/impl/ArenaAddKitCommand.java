package me.emmy.practice.arena.command.impl;

import me.emmy.practice.arena.Arena;
import me.emmy.practice.kit.Kit;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 08:50
 */
public class ArenaAddKitCommand extends BaseCommand {
	@Override
	@Command(name = "arena.addkit", permission = "practice.admin.command")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 2) {
			player.sendMessage(CC.translate("&cUsage: /arena addkit <name> <kit>"));
			return;
		}

		String arenaName = args[0];
		String kitName = args[1];

		Arena arena = Arena.getByName(arenaName);
		if (arena == null) {
			player.sendMessage(CC.translate("&cAn arena with that name does not exist."));
			return;
		}

		Kit kit = Kit.getByName(kitName);
		if (kit == null) {
			player.sendMessage(CC.translate("&cA kit with that name does not exist."));
			return;
		}

		arena.getKits().add(kit.getName());
		arena.save();

		player.sendMessage(CC.translate("&aSuccessfully added kit &e" + kit.getName() + " &ato arena &e" + arena.getName() + "&a."));
	}
}

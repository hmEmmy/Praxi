package me.emmy.practice.arena.command.impl;

import me.emmy.practice.arena.Arena;
import me.emmy.practice.kit.Kit;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * Command to remove a kit from an arena.
 * This command requires admin permissions.
 *
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 09:15
 */
public class ArenaRemoveKitCommand extends BaseCommand {
	@Override
	@Command(name = "arena.removekit", permission = "practice.admin.command")
	public void onCommand(CommandArgs command) {
		CommandSender sender = command.getSender();
		String[] args = command.getArgs();

		if (args.length < 2) {
			sender.sendMessage(CC.translate("&cUsage: /arena removekit <name> <kit>"));
			return;
		}

		String arenaName = args[0];
		String kitName = args[1];

		Arena arena = Arena.getByName(arenaName);
		if (arena == null) {
			sender.sendMessage(CC.translate("&cAn arena with that name does not exist."));
			return;
		}

		Kit kit = Kit.getByName(kitName);
		if (kit == null) {
			sender.sendMessage(CC.translate("&cA kit with that name does not exist."));
			return;
		}

		if (!arena.getKits().remove(kit.getName())) {
			sender.sendMessage(CC.translate("&cThe kit was not found in the arena."));
			return;
		}

		arena.save();
		sender.sendMessage(CC.translate("&aSuccessfully removed kit &e" + kit.getName() +
				" &afrom arena &e" + arena.getName() + "&a."));
	}
}

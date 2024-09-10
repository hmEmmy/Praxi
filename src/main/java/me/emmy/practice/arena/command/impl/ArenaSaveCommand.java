package me.emmy.practice.arena.command.impl;

import me.emmy.practice.arena.Arena;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;

/**
 * Command to save all arenas.
 * This command requires admin permissions.
 *
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 09:25
 */
public class ArenaSaveCommand extends BaseCommand {
	@Override
	@Command(name = "arena.save", permission = "practice.admin.command")
	public void onCommand(CommandArgs command) {
		for (Arena arena : Arena.getArenas()) {
			arena.save();
		}

		command.getSender().sendMessage(CC.translate("&aSaved all arenas!"));
	}
}

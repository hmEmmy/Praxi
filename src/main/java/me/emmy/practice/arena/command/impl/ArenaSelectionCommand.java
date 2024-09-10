package me.emmy.practice.arena.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.arena.selection.Selection;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * Command to toggle the selection wand in the player's inventory.
 * Requires admin permissions.
 *
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 09:35
 */
public class ArenaSelectionCommand extends BaseCommand {
	@Override
	@Command(name = "arena.wand", aliases = { "arena.selection" }, permission = "practice.admin.command")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		if (player.getInventory().first(Selection.SELECTION_WAND) != -1) {
			player.getInventory().remove(Selection.SELECTION_WAND);
			player.sendMessage(CC.translate("&cSelection wand removed from your inventory."));
		} else {
			player.getInventory().addItem(Selection.SELECTION_WAND);
			player.sendMessage(CC.translate("&aSelection wand added to your inventory."));
		}

		player.updateInventory();
	}
}

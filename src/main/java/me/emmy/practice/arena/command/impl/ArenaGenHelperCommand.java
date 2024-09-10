package me.emmy.practice.arena.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * Command to place a generator helper block and update its sign with pitch and yaw.
 * This command requires admin permissions.
 *
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 09:05
 */
public class ArenaGenHelperCommand extends BaseCommand {
	@Override
	@Command(name = "arena.genhelper", permission = "practice.admin.command")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		Block origin = player.getLocation().getBlock();
		Block up = origin.getRelative(BlockFace.UP);

		origin.setType(Material.SPONGE);
		up.setType(Material.SIGN_POST);

		if (!(up.getState() instanceof Sign)) {
			player.sendMessage(CC.translate("&cFailed to place generator helper. Sign not found."));
			return;
		}

		Sign sign = (Sign) up.getState();
		sign.setLine(0, String.valueOf((int) player.getLocation().getPitch()));
		sign.setLine(1, String.valueOf((int) player.getLocation().getYaw()));
		sign.update();

		player.sendMessage(CC.translate("&aGenerator helper placed."));
	}
}

package me.emmy.practice.kit.command;

import me.emmy.practice.kit.Kit;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * Command to set the loadout for a specified kit.
 * Requires the 'practice.kit.setloadout' permission.
 *
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:45
 */
public class KitSetLoadoutCommand extends BaseCommand {

	@Override
	@Command(name = "kit.setloadout", permission = "practice.kit.setloadout")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 1) {
			player.sendMessage(CC.translate("&cPlease specify the kit name."));
			return;
		}

		String kitName = args[0];
		Kit kit = Kit.getByName(kitName);

		if (kit == null) {
			player.sendMessage(CC.translate("&cA kit with that name does not exist."));
			return;
		}

		kit.getKitLoadout().setArmor(player.getInventory().getArmorContents());
		kit.getKitLoadout().setContents(player.getInventory().getContents());
		kit.save();

		player.sendMessage(CC.translate("&aYou updated the kit's loadout."));
	}
}

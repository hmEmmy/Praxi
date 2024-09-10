package me.emmy.practice.kit.command;

import me.emmy.practice.kit.Kit;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:15
 */
public class KitGetLoadoutCommand extends BaseCommand {

	@Override
	@Command(name = "kit.getloadout", permission = "practice.kit.getloadout")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length < 1) {
			player.sendMessage(CC.translate("&cUsage: /kit getloadout <kitName>"));
			return;
		}

		String kitName = args[0];
		Kit kit = Kit.getByName(kitName);

		if (kit == null) {
			player.sendMessage(CC.translate("&cA kit with that name does not exist."));
			return;
		}

		player.getInventory().setArmorContents(kit.getKitLoadout().getArmor());
		player.getInventory().setContents(kit.getKitLoadout().getContents());
		player.updateInventory();

		player.sendMessage(CC.translate("&aYou received the kit's loadout."));
	}
}

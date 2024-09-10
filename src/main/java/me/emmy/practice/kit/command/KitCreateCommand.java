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
 * @date 24/08/2024 - 10:00
 */
public class KitCreateCommand extends BaseCommand {

	@Override
	@Command(name = "kit.create", permission = "practice.kit.create")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.translate("&cPlease provide a kit name."));
			return;
		}

		String kitName = args[0];

		if (Kit.getByName(kitName) != null) {
			player.sendMessage(CC.translate("&cA kit with that name already exists."));
			return;
		}

		Kit kit = new Kit(kitName);
		kit.save();
		Kit.getKits().add(kit);

		player.sendMessage(CC.translate("&aYou created a new kit named &e" + kitName + "&a."));
	}
}

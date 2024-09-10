package me.emmy.practice.kit.command;

import me.emmy.practice.kit.Kit;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:30
 */
public class KitsCommand extends BaseCommand {

	@Override
	@Command(name = "command", permission = "practice.admin.kit")
	public void onCommand(CommandArgs command) {
		CommandSender sender = command.getSender();

		sender.sendMessage(CC.translate("&6&lKits List:"));

		if (Kit.getKits().isEmpty()) {
			sender.sendMessage(CC.translate("&7No command available."));
			return;
		}

		for (Kit kit : Kit.getKits()) {
			sender.sendMessage(CC.translate("&7- &a" + kit.getName()));
		}
	}
}

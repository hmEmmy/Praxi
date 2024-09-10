package me.emmy.practice.event.command.impl.user;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.event.game.EventGame;
import me.emmy.practice.util.Cooldown;
import me.emmy.practice.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class EventClearCooldownCommand extends BaseCommand {

	@Command(name = "event.clearcooldown", permission = "practice.admin.event")
	@Override
	public void onCommand(CommandArgs command) {
		CommandSender sender = command.getSender();
		EventGame.setCooldown(new Cooldown(0));
		sender.sendMessage(CC.translate("&aYou cleared the event cooldown."));
	}
}

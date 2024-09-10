package me.emmy.practice.arena.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.arena.Arena;
import me.emmy.practice.arena.ArenaType;
import me.emmy.practice.util.chat.CC;
import me.emmy.practice.util.chat.ChatComponentBuilder;
import me.emmy.practice.util.chat.ChatHelper;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 09:30
 */
public class ArenasCommand extends BaseCommand {
	@Override
	@Command(name = "arenas", permission = "practice.admin.command")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		player.sendMessage(CC.translate("&6Arenas:"));

		if (Arena.getArenas().isEmpty()) {
			player.sendMessage(CC.translate("&7There are no arenas."));
			return;
		}

		for (Arena arena : Arena.getArenas()) {
			if (arena.getType() == ArenaType.DUPLICATE) {
				continue;
			}

			ChatComponentBuilder builder = new ChatComponentBuilder("")
					.parse(CC.translate("&7- " + (arena.isSetup() ? "&a" : "&c") + arena.getName() +
							"&7 (" + arena.getType().name() + ") "));

			ChatComponentBuilder status = new ChatComponentBuilder("")
					.parse(CC.translate("&7[&6STATUS&7]"));
			status.attachToEachPart(ChatHelper.hover(CC.translate("&6Click to view this arena's status.")));
			status.attachToEachPart(ChatHelper.click("/arena status " + arena.getName()));

			builder.append(" ");

			for (BaseComponent component : status.create()) {
				builder.append((TextComponent) component);
			}

			player.spigot().sendMessage(builder.create());
		}
	}
}

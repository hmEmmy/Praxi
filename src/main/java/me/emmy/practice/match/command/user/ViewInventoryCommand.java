package me.emmy.practice.match.command.user;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.match.MatchSnapshot;
import me.emmy.practice.match.menu.MatchDetailsMenu;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class ViewInventoryCommand extends BaseCommand {

	@Command(name = "viewinv", permission = "practice.viewinv")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.translate("&cYou must specify an ID."));
			return;
		}

		String id = args[0];
		MatchSnapshot cachedInventory;

		try {
			cachedInventory = MatchSnapshot.getByUuid(UUID.fromString(id));
		} catch (IllegalArgumentException e) {
			cachedInventory = MatchSnapshot.getByName(id);
		}

		if (cachedInventory == null) {
			player.sendMessage(CC.translate("&cCouldn't find an inventory for that ID."));
			return;
		}

		new MatchDetailsMenu(cachedInventory).openMenu(player);
	}
}

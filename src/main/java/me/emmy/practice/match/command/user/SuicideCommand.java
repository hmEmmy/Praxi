package me.emmy.practice.match.command.user;

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
public class SuicideCommand extends BaseCommand {

    @Command(name = "suicide", permission = "practice.suicide")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.setHealth(0);
        player.sendMessage(CC.translate("&cYou have killed yourself! Oh noes"));
    }
}

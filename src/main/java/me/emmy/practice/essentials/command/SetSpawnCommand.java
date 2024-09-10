package me.emmy.practice.essentials.command;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.Practice;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class SetSpawnCommand extends BaseCommand {

    @Override
    @Command(name = "setspawn", permission = "practice.setspawn")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Practice.getInstance().getEssentials().setSpawn(player.getLocation());
        player.sendMessage(CC.translate("&bSpawn set successfully!"));
    }
}

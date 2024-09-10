package me.emmy.practice.arena.command;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 08:31
 */
public class ArenaCommand extends BaseCommand {
    @Override
    @Command(name = "arena", permission = "practice.admin.command")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&c&lArena Help"));
        player.sendMessage(CC.translate("&7- &c/arena addkit <name> <kit>"));
        player.sendMessage(CC.translate("&7- &c/arena create <name>"));
        player.sendMessage(CC.translate("&7- &c/arena delete <name>"));
        player.sendMessage(CC.translate("&7- &c/arena generate <name>"));
        player.sendMessage(CC.translate("&7- &c/arena genhelper"));
        player.sendMessage(CC.translate("&7- &c/arena removekit <name> <kit>"));
        player.sendMessage(CC.translate("&7- &c/arena save"));
        player.sendMessage(CC.translate("&7- &c/arena selectitem"));
        player.sendMessage(CC.translate("&7- &c/arena setspawn <name> <a/b>"));
        player.sendMessage(CC.translate("&7- &c/arena status <name>"));
    }
}

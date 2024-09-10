package me.emmy.practice.arena.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.arena.Arena;
import me.emmy.practice.arena.impl.SharedArena;
import me.emmy.practice.arena.selection.Selection;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 08:37
 */
public class ArenaCreateCommand extends BaseCommand {
    @Override
    @Command(name = "arena.create", permission = "practice.admin.command")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.RED + "Usage: /arena create <name>");
            return;
        }

        String arenaName = args[0];
        if (Arena.getByName(arenaName) != null) {
            player.sendMessage(CC.RED + "An arena with that name already exists.");
            return;
        }

        Selection selection = Selection.createOrGetSelection(player);
        if (!selection.isFullObject()) {
            player.sendMessage(CC.RED + "Your selection is incomplete.");
            return;
        }

        Arena arena = new SharedArena(arenaName, selection.getPoint1(), selection.getPoint2());
        Arena.getArenas().add(arena);

        player.sendMessage(CC.translate("&aSuccessfully created arena &e" + arenaName + "&a."));
    }
}

package me.emmy.practice.party.command.impl;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.profile.Profile;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:04
 */
public class PartyChatCommand extends BaseCommand {
    @Command(name = "party.chat", aliases = {"p.chat"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.getParty() != null) {
            profile.getParty().sendChat(player, message);
        }
    }
}

package me.emmy.practice.profile.option.command;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.locale.Locale;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class ToggleSpectatorsCommand extends BaseCommand {

    @Command(name = "togglespectators", aliases = {"togglespecs", "tgs"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().allowSpectators(!profile.getOptions().allowSpectators());

        if (profile.getOptions().allowSpectators()) {
            player.sendMessage(Locale.OPTIONS_SPECTATORS_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_SPECTATORS_DISABLED.format());
        }
    }

}

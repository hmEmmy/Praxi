package me.emmy.practice.party.command;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.locale.Locale;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 09:56
 */
public class PartyCommand extends BaseCommand {

    @Command(name = "party", aliases = {"p"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&7&m------------------------------------------------"));
        player.sendMessage(CC.translate("&6Party Help"));
        player.sendMessage(CC.translate("&9/party create &7- &fCreate a party"));
        player.sendMessage(CC.translate("&9/party disband &7- &fDisband your party"));
        player.sendMessage(CC.translate("&9/party leave &7- &fLeave your party"));
        player.sendMessage(CC.translate("&9/party join <name> &7- &fJoin a party"));
        player.sendMessage(CC.translate("&9/party kick <player> &7- &fKick a member"));
        player.sendMessage(CC.translate("&9/party open &7- &fMake your party open"));
        player.sendMessage(CC.translate("&9/party close &7- &fMake your party closed"));
        player.sendMessage(CC.translate("&9/party chat <msg> &7- &fSend a chat message"));
        player.sendMessage(CC.translate("&7&m------------------------------------------------"));
    }
}

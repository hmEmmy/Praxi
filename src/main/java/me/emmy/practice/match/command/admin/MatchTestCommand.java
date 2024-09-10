package me.emmy.practice.match.command.admin;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.locale.Locale;
import me.emmy.practice.match.Match;
import me.emmy.practice.match.participant.MatchGamePlayer;
import me.emmy.practice.participant.GameParticipant;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:15
 */
public class MatchTestCommand extends BaseCommand {

	@Override
	@Command(name = "command.test", permission = "practice.admin")
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		List<GameParticipant<MatchGamePlayer>> list = Arrays.asList(
				new GameParticipant<>(new MatchGamePlayer(UUID.randomUUID(), "Test1")),
				new GameParticipant<>(new MatchGamePlayer(UUID.randomUUID(), "Test2")),
				new GameParticipant<>(new MatchGamePlayer(UUID.randomUUID(), "Test3")),
				new GameParticipant<>(new MatchGamePlayer(UUID.randomUUID(), "Test4"))
		);

		BaseComponent[] components = Match.generateInventoriesComponents(
				Locale.MATCH_END_WINNER_INVENTORY.format("s"), list);

		player.spigot().sendMessage(components);
	}
}

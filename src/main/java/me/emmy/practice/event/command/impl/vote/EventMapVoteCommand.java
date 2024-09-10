package me.emmy.practice.event.command.impl.vote;

import me.emmy.practice.event.game.EventGame;
import me.emmy.practice.event.game.map.EventGameMap;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.profile.ProfileState;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.util.Cooldown;
import me.emmy.practice.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class EventMapVoteCommand extends BaseCommand {

	@Command(name = "event.mapvote", permission = "practice.event.mapvote")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.translate("&cYou need to specify a map to vote for."));
			return;
		}

		String mapName = args[0];
		EventGameMap gameMap = EventGameMap.getByName(mapName);

		if (gameMap == null) {
			player.sendMessage(CC.translate("&cYou cannot vote for a map that doesn't exist!"));
			return;
		}

		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (profile.getState() == ProfileState.EVENT && EventGame.getActiveGame() != null) {
			if (profile.getVoteCooldown().hasExpired()) {
				profile.setVoteCooldown(new Cooldown(5000));
				EventGame.getActiveGame().getGameLogic().onVote(player, gameMap);
			} else {
				player.sendMessage(CC.translate("&cYou can vote in another " + profile.getVoteCooldown().getTimeLeft() + "."));
			}
		} else {
			player.sendMessage(CC.translate("&cYou are not in an event."));
		}
	}
}

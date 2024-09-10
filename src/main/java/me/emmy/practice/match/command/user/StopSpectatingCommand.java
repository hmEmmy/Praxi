package me.emmy.practice.match.command.user;

import me.emmy.practice.profile.Profile;
import me.emmy.practice.profile.ProfileState;
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
public class StopSpectatingCommand extends BaseCommand {

	@Command(name = "stopspectating", permission = "practice.stopspectating")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (profile.getState() == ProfileState.FIGHTING && profile.getMatch().getGamePlayer(player).isDead()) {
			profile.getMatch().getGamePlayer(player).setDisconnected(true);
			profile.setState(ProfileState.LOBBY);
			profile.setMatch(null);
		} else if (profile.getState() == ProfileState.SPECTATING) {
			profile.getMatch().removeSpectator(player);
		} else {
			player.sendMessage(CC.translate("&cYou are not spectating a match."));
		}
	}
}

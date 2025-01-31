package me.emmy.practice.match.task;

import me.emmy.practice.locale.Locale;
import me.emmy.practice.match.Match;
import me.emmy.practice.match.MatchState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchLogicTask extends BukkitRunnable {

	private final Match match;
	private int totalTicked;
	@Getter @Setter private int nextAction;

	public MatchLogicTask(Match match) {
		this.match = match;

		if (match.getKit().getGameRules().isSumo()) {
			nextAction = 4;
		} else {
			nextAction = 6;
		}
	}

	@Override
	public void run() {
		totalTicked++;
		nextAction--;

		if (match.getState() == MatchState.STARTING_ROUND) {
			if (nextAction == 0) {
				match.onRoundStart();
				match.setState(MatchState.PLAYING_ROUND);
				match.sendMessage(Locale.MATCH_STARTED.format());
				match.sendSound(Sound.ORB_PICKUP, 1.0F, 1.0F);
			} else {
				match.sendMessage(Locale.MATCH_START_TIMER.format(nextAction, nextAction == 1 ? "" : "s"));
				match.sendSound(Sound.ORB_PICKUP, 1.0F, 15F);
			}
		} else if (match.getState() == MatchState.ENDING_ROUND) {
			if (nextAction == 0) {
				if (match.canStartRound()) {
					match.onRoundStart();
				}
			}
		} else if (match.getState() == MatchState.ENDING_MATCH) {
			if (nextAction == 0) {
				match.end();
			}
		}
	}

}

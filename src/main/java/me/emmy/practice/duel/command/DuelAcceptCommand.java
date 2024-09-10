package me.emmy.practice.duel.command;

import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.arena.Arena;
import me.emmy.practice.duel.DuelRequest;
import me.emmy.practice.match.Match;
import me.emmy.practice.match.impl.BasicTeamMatch;
import me.emmy.practice.match.participant.MatchGamePlayer;
import me.emmy.practice.participant.GameParticipant;
import me.emmy.practice.participant.TeamGameParticipant;
import me.emmy.practice.party.Party;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 10:00
 */
public class DuelAcceptCommand extends BaseCommand {

	@Command(name = "duel.accept")
	@Override
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			player.sendMessage(CC.translate("&cThat player is no longer online."));
			return;
		}

		if (player.hasMetadata("frozen")) {
			player.sendMessage(CC.translate("&cYou cannot duel while frozen."));
			return;
		}

		if (target.hasMetadata("frozen")) {
			player.sendMessage(CC.translate("&cYou cannot duel a frozen player."));
			return;
		}

		Profile playerProfile = Profile.getByUuid(player.getUniqueId());

		if (playerProfile.isBusy()) {
			player.sendMessage(CC.translate("&cYou cannot duel right now."));
			return;
		}

		Profile targetProfile = Profile.getByUuid(target.getUniqueId());

		if (targetProfile.isBusy()) {
			player.sendMessage(CC.translate(target.getDisplayName() + "&c is currently busy."));
			return;
		}

		DuelRequest duelRequest = playerProfile.getDuelRequest(target);

		if (duelRequest != null) {
			if (targetProfile.isDuelRequestExpired(duelRequest)) {
				player.sendMessage(CC.translate("&cThat duel request has expired!"));
				return;
			}

			if (duelRequest.isParty()) {
				if (playerProfile.getParty() == null) {
					player.sendMessage(CC.translate("&cYou do not have a party to duel with."));
					return;
				} else if (targetProfile.getParty() == null) {
					player.sendMessage(CC.translate("&cThat player does not have a party to duel with."));
					return;
				}
			} else {
				if (playerProfile.getParty() != null) {
					player.sendMessage(CC.translate("&cYou cannot duel while in a party."));
					return;
				} else if (targetProfile.getParty() != null) {
					player.sendMessage(CC.translate("&cThat player is in a party and cannot duel right now."));
					return;
				}
			}

			Arena arena = duelRequest.getArena();

			if (arena.isActive()) {
				arena = Arena.getRandomArena(duelRequest.getKit());
			}

			if (arena == null) {
				player.sendMessage(CC.translate("&cTried to start a duel but there are no available arenas."));
				return;
			}

			playerProfile.getDuelRequests().remove(duelRequest);

			arena.setActive(true);

			GameParticipant<MatchGamePlayer> participantA = null;
			GameParticipant<MatchGamePlayer> participantB = null;

			if (duelRequest.isParty()) {
				for (Party party : new Party[]{playerProfile.getParty(), targetProfile.getParty()}) {
					Player leader = party.getLeader();
					MatchGamePlayer gamePlayer = new MatchGamePlayer(leader.getUniqueId(), leader.getName());
					TeamGameParticipant<MatchGamePlayer> participant = new TeamGameParticipant<>(gamePlayer);

					for (Player partyPlayer : party.getListOfPlayers()) {
						if (!partyPlayer.getPlayer().equals(leader)) {
							participant.getPlayers().add(new MatchGamePlayer(partyPlayer.getUniqueId(), partyPlayer.getName()));
						}
					}

					if (participantA == null) {
						participantA = participant;
					} else {
						participantB = participant;
					}
				}
			} else {
				MatchGamePlayer playerA = new MatchGamePlayer(player.getUniqueId(), player.getName());
				MatchGamePlayer playerB = new MatchGamePlayer(target.getUniqueId(), target.getName());

				participantA = new GameParticipant<>(playerA);
				participantB = new GameParticipant<>(playerB);
			}

			Match match = new BasicTeamMatch(null, duelRequest.getKit(), arena, false, participantA, participantB);
			match.start();
		} else {
			player.sendMessage(CC.translate("&cYou do not have a duel request from that player."));
		}
	}
}

package com.minexd.praxi.profile.visibility;

import com.minexd.praxi.match.participant.MatchGamePlayer;
import com.minexd.praxi.nametag.NameTags;
import com.minexd.praxi.profile.Profile;
import com.minexd.praxi.profile.ProfileState;
import com.minexd.zoot.ZootAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VisibilityLogic {

	public static void handle(Player viewer) {
		for (Player target : Bukkit.getOnlinePlayers()) {
			handle(viewer, target);
		}
	}

	public static void handle(Player viewer, Player target) {
		if (viewer == null || target == null) {
			return;
		}

		Profile viewerProfile = Profile.getByUuid(viewer.getUniqueId());
		Profile targetProfile = Profile.getByUuid(target.getUniqueId());

		if (viewerProfile.getState() == ProfileState.LOBBY || viewerProfile.getState() == ProfileState.QUEUEING) {
			if (viewer.equals(target)) {
				NameTags.color(viewer, target, ZootAPI.getColorOfPlayer(viewer), false);
				return;
			}

			if (viewerProfile.getParty() != null && viewerProfile.getParty().containsPlayer(target.getUniqueId())) {
				viewer.showPlayer(target);
				NameTags.color(viewer, target, ChatColor.BLUE, false);
			} else {
				viewer.hidePlayer(target);
				NameTags.reset(viewer, target);
			}
		} else if (viewerProfile.getState() == ProfileState.FIGHTING) {
			if (viewer.equals(target)) {
				NameTags.color(viewer, target, ChatColor.GREEN, false);
				return;
			}

			MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);

			if (targetGamePlayer != null) {
				if (!targetGamePlayer.isDead()) {
					viewer.showPlayer(target);
					NameTags.color(viewer, target, viewerProfile.getMatch().getRelationColor(viewer, target), false);
				} else {
					viewer.hidePlayer(target);
					NameTags.reset(viewer, target);
				}
			} else {
				viewer.hidePlayer(target);
				NameTags.reset(viewer, target);
			}
		} else if (viewerProfile.getState() == ProfileState.EVENT) {
			if (targetProfile.getState() == ProfileState.EVENT) {
				viewer.showPlayer(target);
				NameTags.color(viewer, target, ChatColor.AQUA, false);
			} else {
				viewer.hidePlayer(target);
				NameTags.reset(viewer, target);
			}
		} else if (viewerProfile.getState() == ProfileState.SPECTATING) {
			MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);
			
			if (targetGamePlayer != null) {
				if (!targetGamePlayer.isDead() && !targetGamePlayer.isDisconnected()) {
					viewer.showPlayer(target);
					NameTags.color(viewer, target, viewerProfile.getMatch().getRelationColor(viewer, target), false);
				} else {
					viewer.hidePlayer(target);
					NameTags.reset(viewer, target);
				}
			} else {
				viewer.hidePlayer(target);
				NameTags.reset(viewer, target);
			}
		}
	}

}

package me.emmy.practice.profile.meta.option;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ProfileOptions {

	@Getter @Setter private boolean showScoreboard = true;
	@Getter @Setter private boolean receiveDuelRequests = true;
	@Getter @Setter private boolean allowSpectators = true;

}

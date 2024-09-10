package me.emmy.practice.profile.option;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.emmy.practice.util.BaseEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class OptionsOpenedEvent extends BaseEvent {

	private final Player player;
	private List<ProfileOptionButton> buttons = new ArrayList<>();

}

package me.emmy.practice.arena.menu;

import me.emmy.practice.api.menu.Button;
import me.emmy.practice.api.menu.Menu;
import java.util.Map;
import org.bukkit.entity.Player;

public class SelectArenaMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&6Select Arena";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		return super.getButtons();
	}

}

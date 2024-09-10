package me.emmy.practice.event;

import me.emmy.practice.Practice;
import me.emmy.practice.event.game.EventGame;
import me.emmy.practice.event.game.EventGameLogic;
import me.emmy.practice.event.impl.sumo.SumoEvent;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public interface Event {

	List<Event> events = new ArrayList<>();

	static void init() {
		events.add(new SumoEvent());

		for (Event event : events) {
			for (Listener listener : event.getListeners()) {
				Practice.getInstance().getServer().getPluginManager().registerEvents(listener, Practice.getInstance());
			}
		}
	}

	static <T extends Event> T getEvent(Class<? extends Event> clazz) {
		for (Event event : events) {
			if (event.getClass() == clazz) {
				return (T) clazz.cast(event);
			}
		}

		return null;
	}

	static Event getEventByName(String name) {
		for (Event event : events) {
			if (event.getDisplayName().equalsIgnoreCase(name)) {
				return event;
			}
		}

		return null;
	}

	String getDisplayName();

	String getDisplayName(EventGame game);

	List<String> getDescription();

	Location getLobbyLocation();

	void setLobbyLocation(Location location);

	ItemStack getIcon();

	boolean canHost(Player player);

	List<String> getAllowedMaps();

	List<Listener> getListeners();

	default List<Object> getCommands() {
		return new ArrayList<>();
	}

	EventGameLogic start(EventGame game);

	void save();

}

package me.emmy.practice;

import me.emmy.practice.api.aether.Aether;
import me.emmy.practice.api.aether.AetherOptions;
import me.emmy.practice.api.command.CommandFramework;
import me.emmy.practice.api.menu.MenuListener;
import me.emmy.practice.arena.Arena;
import me.emmy.practice.arena.ArenaListener;
import me.emmy.practice.essentials.Essentials;
import me.emmy.practice.event.Event;
import me.emmy.practice.event.game.EventGameListener;
import me.emmy.practice.event.game.map.EventGameMap;
import me.emmy.practice.kit.Kit;
import me.emmy.practice.kit.KitEditorListener;
import me.emmy.practice.match.Match;
import me.emmy.practice.match.MatchListener;
import me.emmy.practice.party.Party;
import me.emmy.practice.party.PartyListener;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.profile.ProfileListener;
import me.emmy.practice.profile.hotbar.Hotbar;
import me.emmy.practice.queue.QueueListener;
import me.emmy.practice.queue.QueueThread;
import me.emmy.practice.scoreboard.ScoreboardAdapter;
import me.emmy.practice.util.InventoryUtil;
import me.emmy.practice.util.WorldUtil;
import me.emmy.practice.util.config.BasicConfigurationFile;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
@Setter
public class Practice extends JavaPlugin {

	@Getter
	private static Practice instance;

	private CommandFramework commandFramework;
	private BasicConfigurationFile mainConfig;
	private BasicConfigurationFile arenasConfig;
	private BasicConfigurationFile kitsConfig;
	private BasicConfigurationFile eventsConfig;
	private MongoDatabase mongoDatabase;
	private Essentials essentials;

	@Override
	public void onEnable() {
		instance = this;

		this.commandFramework = new CommandFramework();
		this.commandFramework.registerCommandsInPackage("me.emmy.practice");

		this.mainConfig = new BasicConfigurationFile(this, "config");
		this.arenasConfig = new BasicConfigurationFile(this, "arenas");
		this.kitsConfig = new BasicConfigurationFile(this, "kits");
		this.eventsConfig = new BasicConfigurationFile(this, "events");

		this.essentials = new Essentials();

		loadMongo();

		Hotbar.init();
		Kit.init();
		Arena.init();
		Profile.init();
		Match.init();
		Party.init();
		Event.init();
		EventGameMap.init();

		new Aether(this, new ScoreboardAdapter(), new AetherOptions().hook(true));
		new QueueThread().start();

		Arrays.asList(
				new MenuListener(),
				new KitEditorListener(),
				new PartyListener(),
				new ProfileListener(),
				new PartyListener(),
				new MatchListener(),
				new QueueListener(),
				new ArenaListener(),
				new EventGameListener()
		).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

		Arrays.asList(
				Material.WORKBENCH,
				Material.STICK,
				Material.WOOD_PLATE,
				Material.WOOD_BUTTON,
				Material.SNOW_BLOCK
		).forEach(InventoryUtil::removeCrafting);

		getServer().getWorlds().forEach(world -> {
			world.setDifficulty(Difficulty.HARD);
			WorldUtil.clearEntities(world);
		});
	}

	@Override
	public void onDisable() {
		Match.cleanup();
	}

	private void loadMongo() {
		if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
			mongoDatabase = new MongoClient(
					new ServerAddress(
							mainConfig.getString("MONGO.HOST"),
							mainConfig.getInteger("MONGO.PORT")
					),
					MongoCredential.createCredential(
							mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"),
							"admin", mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray()
					),
					MongoClientOptions.builder().build()
			).getDatabase("praxi");
		} else {
			mongoDatabase = new MongoClient(mainConfig.getString("MONGO.HOST"), mainConfig.getInteger("MONGO.PORT"))
					.getDatabase("praxi");
		}
	}
}

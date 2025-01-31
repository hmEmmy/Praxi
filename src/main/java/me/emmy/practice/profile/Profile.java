package me.emmy.practice.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.emmy.practice.kit.Kit;
import me.emmy.practice.kit.KitLoadout;
import me.emmy.practice.profile.meta.ProfileKitEditorData;
import me.emmy.practice.profile.meta.ProfileKitData;
import me.emmy.practice.profile.meta.ProfileRematchData;
import me.emmy.practice.profile.meta.option.ProfileOptions;
import me.emmy.practice.util.InventoryUtil;
import me.emmy.practice.util.chat.CC;
import me.emmy.practice.util.Cooldown;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import me.emmy.practice.Practice;
import me.emmy.practice.duel.DuelProcedure;
import me.emmy.practice.duel.DuelRequest;
import me.emmy.practice.match.Match;
import me.emmy.practice.party.Party;
import me.emmy.practice.queue.QueueProfile;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Profile {

	@Getter private static Map<UUID, Profile> profiles = new HashMap<>();
	private static MongoCollection<Document> collection;

	@Getter private UUID uuid;
	@Getter @Setter private ProfileState state;
	@Getter private final ProfileOptions options;
	@Getter private final ProfileKitEditorData kitEditorData;
	@Getter private final Map<Kit, ProfileKitData> kitData;
	@Getter private final List<DuelRequest> duelRequests;
	@Getter @Setter private DuelProcedure duelProcedure;
	@Getter @Setter private ProfileRematchData rematchData;
	@Getter @Setter private Party party;
	@Getter @Setter private Match match;
	@Getter @Setter private QueueProfile queueProfile;
	@Getter @Setter private Cooldown enderpearlCooldown;
	@Getter @Setter private Cooldown voteCooldown;

	public Profile(UUID uuid) {
		this.uuid = uuid;
		this.state = ProfileState.LOBBY;
		this.options = new ProfileOptions();
		this.kitEditorData = new ProfileKitEditorData();
		this.kitData = new HashMap<>();
		this.duelRequests = new ArrayList<>();
		this.enderpearlCooldown = new Cooldown(0);
		this.voteCooldown = new Cooldown(0);

		for (Kit kit : Kit.getKits()) {
			this.kitData.put(kit, new ProfileKitData());
		}
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public DuelRequest getDuelRequest(Player sender) {
		for (DuelRequest duelRequest : duelRequests) {
			if (duelRequest.getSender().equals(sender.getUniqueId())) {
				return duelRequest;
			}
		}

		return null;
	}

	public boolean isDuelRequestExpired(DuelRequest duelRequest) {
		if (duelRequest != null) {
			if (duelRequest.isExpired()) {
				duelRequests.remove(duelRequest);
				return true;
			}
		}

		return false;
	}

	public boolean isBusy() {
		return state != ProfileState.LOBBY;
	}

	void load() {
		Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

		if (document == null) {
			this.save();
			return;
		}

		Document options = (Document) document.get("options");

		this.options.showScoreboard(options.getBoolean("showScoreboard"));
		this.options.allowSpectators(options.getBoolean("allowSpectators"));
		this.options.receiveDuelRequests(options.getBoolean("receiveDuelRequests"));

		Document kitStatistics = (Document) document.get("kitStatistics");

		for (String key : kitStatistics.keySet()) {
			Document kitDocument = (Document) kitStatistics.get(key);
			Kit kit = Kit.getByName(key);

			if (kit != null) {
				ProfileKitData profileKitData = new ProfileKitData();
				profileKitData.setElo(kitDocument.getInteger("elo"));
				profileKitData.setWon(kitDocument.getInteger("won"));
				profileKitData.setLost(kitDocument.getInteger("lost"));

				kitData.put(kit, profileKitData);
			}
		}

		Document kitsDocument = (Document) document.get("loadouts");

		for (String key : kitsDocument.keySet()) {
			Kit kit = Kit.getByName(key);

			if (kit != null) {
				JsonArray kitsArray = new JsonParser().parse(kitsDocument.getString(key)).getAsJsonArray();
				KitLoadout[] loadouts = new KitLoadout[4];

				for (JsonElement kitElement : kitsArray) {
					JsonObject kitObject = kitElement.getAsJsonObject();

					KitLoadout loadout = new KitLoadout(kitObject.get("name").getAsString());
					loadout.setArmor(InventoryUtil.deserializeInventory(kitObject.get("armor").getAsString()));
					loadout.setContents(InventoryUtil.deserializeInventory(kitObject.get("contents").getAsString()));

					loadouts[kitObject.get("index").getAsInt()] = loadout;
				}

				kitData.get(kit).setLoadouts(loadouts);
			}
		}
	}

	public void save() {
		Document document = new Document();
		document.put("uuid", uuid.toString());

		Document optionsDocument = new Document();
		optionsDocument.put("showScoreboard", options.showScoreboard());
		optionsDocument.put("allowSpectators", options.allowSpectators());
		optionsDocument.put("receiveDuelRequests", options.receiveDuelRequests());
		document.put("options", optionsDocument);

		Document kitStatisticsDocument = new Document();

		for (Map.Entry<Kit, ProfileKitData> entry : kitData.entrySet()) {
			Document kitDocument = new Document();
			kitDocument.put("elo", entry.getValue().getElo());
			kitDocument.put("won", entry.getValue().getWon());
			kitDocument.put("lost", entry.getValue().getLost());
			kitStatisticsDocument.put(entry.getKey().getName(), kitDocument);
		}

		document.put("kitStatistics", kitStatisticsDocument);

		Document kitsDocument = new Document();

		for (Map.Entry<Kit, ProfileKitData> entry : kitData.entrySet()) {
			JsonArray kitsArray = new JsonArray();

			for (int i = 0; i < 4; i++) {
				KitLoadout loadout = entry.getValue().getLoadout(i);

				if (loadout != null) {
					JsonObject kitObject = new JsonObject();
					kitObject.addProperty("index", i);
					kitObject.addProperty("name", loadout.getCustomName());
					kitObject.addProperty("armor", InventoryUtil.serializeInventory(loadout.getArmor()));
					kitObject.addProperty("contents", InventoryUtil.serializeInventory(loadout.getContents()));
					kitsArray.add(kitObject);
				}
			}

			kitsDocument.put(entry.getKey().getName(), kitsArray.toString());
		}

		document.put("loadouts", kitsDocument);

		collection.replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
	}

	public static void init() {
		collection = Practice.getInstance().getMongoDatabase().getCollection("profiles");

		// Players might have joined before the plugin finished loading
		for (Player player : Bukkit.getOnlinePlayers()) {
			Profile profile = new Profile(player.getUniqueId());

			try {
				profile.load();
			} catch (Exception e) {
				player.kickPlayer(CC.RED + "The server is loading...");
				continue;
			}

			profiles.put(player.getUniqueId(), profile);
		}

		// Expire duel requests
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Profile profile : Profile.getProfiles().values()) {
					Iterator<DuelRequest> iterator = profile.duelRequests.iterator();

					while (iterator.hasNext()) {
						DuelRequest duelRequest = iterator.next();

						if (duelRequest.isExpired()) {
							duelRequest.expire();
							iterator.remove();
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(Practice.getInstance(), 60L, 60L);

		// Save every 5 minutes to prevent data loss
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Profile profile : Profile.getProfiles().values()) {
					profile.save();
				}
			}
		}.runTaskTimerAsynchronously(Practice.getInstance(), 6000L, 6000L);
	}

	public static Profile getByUuid(UUID uuid) {
		Profile profile = profiles.get(uuid);

		if (profile == null) {
			profile = new Profile(uuid);
		}

		return profile;
	}

}

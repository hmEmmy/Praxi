package me.emmy.practice.arena.command.impl;

import me.emmy.practice.Practice;
import me.emmy.practice.api.command.BaseCommand;
import me.emmy.practice.api.command.CommandArgs;
import me.emmy.practice.api.command.annotation.Command;
import me.emmy.practice.arena.Arena;
import me.emmy.practice.arena.ArenaType;
import me.emmy.practice.arena.generator.ArenaGenerator;
import me.emmy.practice.arena.generator.Schematic;
import me.emmy.practice.arena.impl.StandaloneArena;
import me.emmy.practice.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

/**
 * @author Emmy
 * @project Practice
 * @date 24/08/2024 - 09:00
 */
public class ArenaGenerateCommand extends BaseCommand {
	@Override
	@Command(name = "arena.generate", permission = "practice.admin.command", inGameOnly = false)
	public void onCommand(CommandArgs command) {
		CommandSender sender = command.getSender();
		File schematicsFolder = new File(Practice.getInstance().getDataFolder(), "schematics");

		if (!schematicsFolder.exists()) {
			sender.sendMessage(CC.translate("&cThe schematics folder does not exist."));
			return;
		}

		File[] files = schematicsFolder.listFiles();
		if (files == null) {
			sender.sendMessage(CC.translate("&cNo schematic files found in the schematics folder."));
			return;
		}

		for (File file : files) {
			if (file.isDirectory() || !file.getName().endsWith(".schematic")) {
				continue;
			}

			boolean duplicate = file.getName().endsWith("_duplicate.schematic");
			String name = file.getName().replace(".schematic", "").replace("_duplicate", "");

			Arena parent = Arena.getByName(name);

			if (parent != null && !(parent instanceof StandaloneArena)) {
				System.out.println("Skipping " + name + " because it's not duplicate and an arena with that name already exists.");
				continue;
			}

			new BukkitRunnable() {
				@Override
				public void run() {
					try {
						new ArenaGenerator(name, Bukkit.getWorlds().get(0), new Schematic(file),
								duplicate ? (parent != null ? ArenaType.DUPLICATE : ArenaType.STANDALONE) : ArenaType.SHARED)
								.generate(file, (StandaloneArena) parent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.runTask(Practice.getInstance());
		}

		sender.sendMessage(CC.translate("&aGenerating arenas... See console for details."));
	}
}

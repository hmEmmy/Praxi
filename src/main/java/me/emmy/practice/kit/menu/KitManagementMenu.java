package me.emmy.practice.kit.menu;

import me.emmy.practice.locale.Locale;
import me.emmy.practice.kit.Kit;
import me.emmy.practice.kit.KitLoadout;
import me.emmy.practice.profile.Profile;
import me.emmy.practice.util.ItemBuilder;
import me.emmy.practice.api.menu.Button;
import me.emmy.practice.api.menu.Menu;
import me.emmy.practice.api.menu.button.BackButton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class KitManagementMenu extends Menu {

	private static Button PLACEHOLDER = Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 7, " ");

	private Kit kit;

	public KitManagementMenu(Kit kit) {
		this.kit = kit;

		setPlaceholder(true);
		setUpdateAfterClick(false);
	}

	@Override
	public String getTitle(Player player) {
		return "&6Viewing " + kit.getName() + " kits";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		Profile profile = Profile.getByUuid(player.getUniqueId());
		KitLoadout[] kitLoadouts = profile.getKitData().get(kit).getLoadouts();

		if (kitLoadouts == null) {
			return buttons;
		}

		int startPos = -1;

		for (int i = 0; i < 4; i++) {
			startPos += 2;

			KitLoadout kitLoadout = kitLoadouts[i];
			buttons.put(startPos, kitLoadout == null ? new CreateKitButton(i) : new KitDisplayButton(kitLoadout));
			buttons.put(startPos + 18, new LoadKitButton(i));
			buttons.put(startPos + 27, kitLoadout == null ? PLACEHOLDER : new RenameKitButton(kit, kitLoadout));
			buttons.put(startPos + 36, kitLoadout == null ? PLACEHOLDER : new DeleteKitButton(kit, kitLoadout));
		}

		buttons.put(36, new BackButton(new KitEditorSelectKitMenu()));

		return buttons;
	}

	@Override
	public void onClose(Player player) {
		if (!isClosedByMenu()) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.getKitEditorData().setSelectedKit(null);
		}
	}

	@AllArgsConstructor
	private class DeleteKitButton extends Button {

		private Kit kit;
		private KitLoadout kitLoadout;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.STAINED_CLAY)
					.name("&c&lDelete")
					.durability(14)
					.lore(Arrays.asList(
							"&cClick to delete this kit.",
							"&cYou will &lNOT &cbe able to",
							"&crecover this kitLoadout."
					))
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.getKitData().get(kit).deleteKit(kitLoadout);

			new KitManagementMenu(kit).openMenu(player);
		}

	}

	@AllArgsConstructor
	private class CreateKitButton extends Button {

		private int index;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.IRON_SWORD)
					.name("&a&lCreate Kit")
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			Kit kit = profile.getKitEditorData().getSelectedKit();

			// TODO: this shouldn't be null but sometimes it is?
			if (kit == null) {
				player.closeInventory();
				return;
			}

			KitLoadout kitLoadout = new KitLoadout("Kit " + (index + 1));

			if (kit.getKitLoadout() != null) {
				if (kit.getKitLoadout().getArmor() != null) {
					kitLoadout.setArmor(kit.getKitLoadout().getArmor());
				}

				if (kit.getKitLoadout().getContents() != null) {
					kitLoadout.setContents(kit.getKitLoadout().getContents());
				}
			}

			profile.getKitData().get(kit).replaceKit(index, kitLoadout);
			profile.getKitEditorData().setSelectedKitLoadout(kitLoadout);

			new KitEditorMenu(index).openMenu(player);
		}

	}

	@AllArgsConstructor
	private class RenameKitButton extends Button {

		private Kit kit;
		private KitLoadout kitLoadout;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.SIGN)
					.name("&e&lRename")
					.lore("&eClick to rename this kit.")
					.build();
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			player.closeInventory();
			player.sendMessage(Locale.KIT_EDITOR_START_RENAMING.format(kitLoadout.getCustomName()));

			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.getKitEditorData().setSelectedKit(kit);
			profile.getKitEditorData().setSelectedKitLoadout(kitLoadout);
			profile.getKitEditorData().setActive(true);
			profile.getKitEditorData().setRename(true);
		}

	}

	@AllArgsConstructor
	private class LoadKitButton extends Button {

		private int index;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.BOOK)
					.name("&a&lLoad/Edit")
					.lore("&eClick to edit this kit.")
					.build();
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
			Profile profile = Profile.getByUuid(player.getUniqueId());

			// TODO: this shouldn't be null but sometimes it is?
			if (profile.getKitEditorData().getSelectedKit() == null) {
				player.closeInventory();
				return;
			}

			KitLoadout kit = profile.getKitData().get(profile.getKitEditorData().getSelectedKit()).getLoadout(index);

			if (kit == null) {
				kit = new KitLoadout("Kit " + (index + 1));
				kit.setArmor(profile.getKitEditorData().getSelectedKit().getKitLoadout().getArmor());
				kit.setContents(profile.getKitEditorData().getSelectedKit().getKitLoadout().getContents());
				profile.getKitData().get(profile.getKitEditorData().getSelectedKit()).replaceKit(index, kit);
			}

			profile.getKitEditorData().setSelectedKitLoadout(kit);

			new KitEditorMenu(index).openMenu(player);
		}

	}

	@AllArgsConstructor
	private class KitDisplayButton extends Button {

		private KitLoadout kitLoadout;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.BOOK)
					.name("&a&l" + kitLoadout.getCustomName())
					.build();
		}

	}

}

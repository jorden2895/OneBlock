package Oneblock.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.cryptomorin.xseries.XMaterial;

import Oneblock.ChestItems;
import Oneblock.PlayerInfo;
import Oneblock.WorldGuard.OBWorldGuard;

public class GUI {
	public static boolean enabled = true;
	public static final GUIHolder holder = new GUIHolder();
	
	static Inventory baseGUI = null;
	static Inventory topGUI = null;
	
	public static void openGUI(Player p) {
		if (!enabled) return;
		if (baseGUI == null) {
			baseGUI = Bukkit.createInventory(holder, 9, String.format("%s[OneBlock GUI] %s- main", ChatColor.GREEN, ChatColor.WHITE));
	        baseGUI.addItem(setMeta(XMaterial.GRASS_BLOCK, ChatColor.GREEN + "/ob join"));
	        baseGUI.setItem(2, setMeta(XMaterial.PODZOL, ChatColor.GREEN + "/ob leave"));
	        baseGUI.setItem(4, setMeta(XMaterial.GOLD_BLOCK, ChatColor.GOLD + "/ob top"));
	        baseGUI.setItem(6, setMeta(XMaterial.MELON, ChatColor.GREEN + "/ob visit"));
	        baseGUI.setItem(8, setMeta(XMaterial.BARRIER, ChatColor.RED + "/ob idreset", String.format("%s[your island's data will be lost]", ChatColor.RED)));
        }
        p.openInventory(baseGUI);
	}
	
	public static void acceptGUI(Player p, String name) {
		if (!enabled) return;
		Inventory acceptGUI = Bukkit.createInventory(holder, 9, String.format("%sYou are invited to the island.", ChatColor.WHITE));
		acceptGUI.setItem(6, setMeta(XMaterial.REDSTONE_BLOCK, ChatColor.RED + "Ignore"));
		acceptGUI.setItem(2, setMeta(XMaterial.EMERALD_BLOCK, String.format("%sJoin %s%s%s's Island", ChatColor.GREEN, ChatColor.DARK_GREEN, name, ChatColor.GREEN), String.format("%s[your island's data will be lost]", ChatColor.RED)));
        p.openInventory(acceptGUI);
	}
	
	public static void topGUI(Player p) {
		if (!enabled) return;
		if (topGUI == null)
			topGUI = Bukkit.createInventory(holder, 27, String.format("%s[OneBlock GUI] %s- %sTop", ChatColor.GREEN, ChatColor.WHITE, ChatColor.BOLD));
		PlayerInfo inf = Oneblock.Oneblock.gettop(0);
		topGUI.setItem(4, setMeta(XMaterial.NETHERITE_BLOCK, String.format("%s1st - %s", ChatColor.GOLD, parseUUID(inf.uuid)), inf.lvl, parseUUIDs(inf.uuids)));
		inf = Oneblock.Oneblock.gettop(1);
		topGUI.setItem(12, setMeta(XMaterial.DIAMOND_BLOCK, String.format("%s2nd - %s", ChatColor.GRAY, parseUUID(inf.uuid)), inf.lvl, parseUUIDs(inf.uuids)));
		inf = Oneblock.Oneblock.gettop(2);
		topGUI.setItem(14, setMeta(XMaterial.IRON_BLOCK, String.format("%s3rd - %s", ChatColor.GRAY, parseUUID(inf.uuid)), inf.lvl, parseUUIDs(inf.uuids)));
		inf = Oneblock.Oneblock.gettop(3);
		topGUI.setItem(20, setMeta(XMaterial.GOLD_BLOCK, String.format("%s4th - %s", ChatColor.DARK_RED, parseUUID(inf.uuid)), inf.lvl, parseUUIDs(inf.uuids)));
		inf = Oneblock.Oneblock.gettop(4);
		topGUI.setItem(22, setMeta(XMaterial.COPPER_BLOCK, String.format("%s5th - %s", ChatColor.DARK_RED, parseUUID(inf.uuid)), inf.lvl, parseUUIDs(inf.uuids)));
		inf = Oneblock.Oneblock.gettop(5);
		topGUI.setItem(24, setMeta(XMaterial.COAL_BLOCK, String.format("%s6th - %s", ChatColor.DARK_RED, parseUUID(inf.uuid)), inf.lvl, parseUUIDs(inf.uuids)));
        p.openInventory(topGUI);
	}
	
	public static void visitGUI(Player p, List<Player> plonl) {
		if (!enabled) return;
		Inventory visitGUI = Bukkit.createInventory(holder, 54, String.format("%s[OneBlock GUI] %s- %sVisit", ChatColor.GREEN, ChatColor.WHITE, ChatColor.BOLD));
		ArrayList <PlayerInfo> list = new ArrayList<>();
		int size = 0;
		for (Player pl: plonl) {
			PlayerInfo inf = PlayerInfo.get(pl.getUniqueId());
			if (inf == null) continue;
			if (!inf.allow_visit) continue;
			list.add(inf);
			size++;
		}
		size = size > 54 ? 54 : size;
		for (int i = 0; i < size; i++)
			visitGUI.setItem(i, getPlayerHead(plonl.get(i), parseUUID(list.get(i).uuid)));
        p.openInventory(visitGUI);
	}
	
	public static ItemStack getPlayerHead(Player player, String title) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(title);
        skull.setItemMeta(skullMeta);
        return skull;
    }

	public static void chestGUI(Player p, String chestType) {
		List<ItemStack> list = ChestItems.getChest(chestType);
		
		Inventory chestGUI = Bukkit.createInventory(new ChestHolder(), 54, String.format("%s %schest. %s", chestType, ChatColor.BLACK
				, OBWorldGuard.canUse?"":"[Edit only in premium]"));
		for(ItemStack itm : list)
			if (itm != null)
				chestGUI.addItem(itm);
		p.openInventory(chestGUI);
	}
	
	private static String parseUUID(UUID uuid) {
		try { return Bukkit.getOfflinePlayer(uuid).getName();
		} catch (Exception e) {return "Unknown";}
	}
	
	private static String[] parseUUIDs(List<UUID> uuids) {
		String[] Lore = new String[uuids.size()];
		for (int i = 0; i < uuids.size(); i++)
			Lore[i] = parseUUID(uuids.get(i));
		return Lore;
	}
	
	private static ItemStack setMeta(XMaterial material, String title) {
		return setMeta(material, title, 1);
	}
	
	private static ItemStack setMeta(XMaterial material, String title, String ...Lore) {
		return setMeta(material, title, 1, Lore);
	}
	
	private static ItemStack setMeta(XMaterial material, String title, int amount, String ...Lore) {
		if (amount <= 0) amount = 1;
		Material m = material.parseMaterial();
		ItemStack join = new ItemStack(m == null ? Material.EMERALD_BLOCK : m, amount);
        ItemMeta meta = join.getItemMeta();
        meta.setDisplayName(title);
	    meta.setLore(Arrays.asList(Lore));
        join.setItemMeta(meta);
		return join;
	}
}
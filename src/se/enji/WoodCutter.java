package se.enji;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WoodCutter extends JavaPlugin implements Listener {
	FileConfiguration config;
	Random random;

	Boolean needAxe;
	Boolean mustSneak;
	
	List<?> breakable = Arrays.asList(new Material[] { Material.LOG, Material.LOG_2 });
	List<?> surroundable = Arrays.asList(new Material[] { Material.LOG, Material.LOG_2, Material.DIRT, Material.GRASS });

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		config = getConfig();
		config.options().copyDefaults(true);
		
		needAxe = config.getBoolean("needAxe");
		mustSneak = config.getBoolean("mustSneak");
		

		random = new Random();

		saveConfig();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		
		if (!p.hasPermission("woodcutter.use")) {
			return;
		}
		
		if (!breakable.contains(e.getBlock().getType()) || !isAxe(p.getItemInHand().getType())) {
			return;
		}
		
		Location l = e.getBlock().getLocation();
		
		if (!surroundable.contains(l.subtract(0.0, 1.0, 0.0).getBlock().getType()) || !surroundable.contains(l.add(0.0, 1.0, 0.0).getBlock().getType())) {
			return;
		}
		
		if (mustSneak && !p.isSneaking()) {
			return;
		}
		
		columnRemove(l, p);
	}

	private void columnRemove(Location location, Player player) {
		boolean logsLeft = true;
		int fallen = 0;
		location.subtract(0.0, 1.0, 0.0);
		
		while (logsLeft) {
			Block block = location.add(0.0,1.0,0.0).getBlock();
			
			if (breakable.contains(block.getType())) {
				block.breakNaturally();
				fallen++;
			}
			
			else logsLeft = false;

			columnCheck(block, player, 1.0, 0);
			columnCheck(block, player, 0, 1.0);
			columnCheck(block, player, -1.0, 0);
			columnCheck(block, player, 0, -1.0);
			columnCheck(block, player, 1.0, 1.0);
			columnCheck(block, player, 1.0, -1.0);
			columnCheck(block, player, -1.0, 1.0);
			columnCheck(block, player, -1.0, -1.0);
		}
		
		ItemStack handItem = player.getItemInHand();
		int unbreaking = handItem.getEnchantmentLevel(Enchantment.DURABILITY);

		if (unbreaking > 0) {
			// http://minecraft.gamepedia.com/Enchantment#Enchantments
			int chance = 100 / (unbreaking + 1);
			int oldFallen = fallen;

			for (int i = 0; i < oldFallen; i++) {
				if (random.nextInt(100) > chance) fallen--;
			}
		}
		
		short durability = (short)(player.getItemInHand().getDurability() + fallen);
		
		if (durability < maxDurability(handItem.getType())) {
			handItem.setDurability(durability);
		} else {
			handItem.setAmount(0);
			player.setItemInHand(null);
		}
		
	}

	private void columnCheck(Block block, Player player, double xOffset, double zOffset) {
		Location newLocation = block.getLocation().subtract(xOffset, 0, zOffset);

		if (breakable.contains(newLocation.getBlock().getType())) {
			columnRemove(newLocation, player);
		}
	}

	private boolean isAxe(Material a) {
		return !needAxe ? true : a.toString().endsWith("_AXE");
	}
	
	private short maxDurability(Material m) {
		short durability = 0;
		switch (m) {
			case GOLD_AXE:
				durability = 33;
				break;
			case WOOD_AXE:
				durability = 60;
				break;
			case STONE_AXE:
				durability = 132;
				break;
			case IRON_AXE:
				durability = 251;
				break;
			case DIAMOND_AXE:
				durability = 1562;
				break;
			default:
				durability = 0;
				break;
		}
		return durability;
	}
}
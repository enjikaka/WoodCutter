package se.enji;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WoodCutter extends JavaPlugin implements Listener {
	FileConfiguration config;
	List<?> breakable = Arrays.asList(new Material[]{Material.LOG, Material.LOG_2});
	List<?> surroundable = Arrays.asList(new Material[]{Material.LOG, Material.LOG_2, Material.DIRT, Material.GRASS});

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("woodcutter.use")) return;
		if (!breakable.contains(e.getBlock().getType()) || !isAxe(p.getItemInHand().getType())) return;
		Location l = e.getBlock().getLocation();
		if (!surroundable.contains(l.subtract(0.0, 1.0, 0.0).getBlock().getType()) || !surroundable.contains(l.add(0.0, 1.0, 0.0).getBlock().getType())) return;
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

			Location newLocation = block.getLocation().subtract(1.0, 0, 0);if (breakable.contains(newLocation.getBlock().getType())) columnRemove(newLocation, player);
			newLocation = block.getLocation().subtract(0, 0, 1.0);if (breakable.contains(newLocation.getBlock().getType())) columnRemove(newLocation, player);
			newLocation = block.getLocation().subtract(0, 0, -1.0);if (breakable.contains(newLocation.getBlock().getType())) columnRemove(newLocation, player);
			newLocation = block.getLocation().subtract(-1.0, 0, 0);if (breakable.contains(newLocation.getBlock().getType())) columnRemove(newLocation, player);
		}
		player.getItemInHand().setDurability((short)(player.getItemInHand().getDurability() + fallen));
	}

	private boolean isAxe(Material a) {
		return !config.getBoolean("needAxe") ? true : a.toString().endsWith("_AXE");
	}
}
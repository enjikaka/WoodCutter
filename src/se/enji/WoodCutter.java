package se.enji;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WoodCutter extends JavaPlugin implements Listener {
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!acceptsPlayer(p)) return;
		Block b = e.getBlock();
		if (!isAxe(p.getItemInHand().getType())) return;
		if (b.getType()!=Material.LOG) return;
		int x=b.getX();
		int y=b.getY();
		int z=b.getZ();
		if (b.getWorld().getBlockAt(x,(y-1),z).getType()!=Material.DIRT) return;
		World w = b.getWorld();
		boolean logsLeft=true;
		int fallen=0;
		while (logsLeft) {
			y++;
			Block d=w.getBlockAt(x,y,z);
			if (d.getType()==Material.LOG) {
				remove(d);
				fallen++;
			}
			else logsLeft=false;
		}
		p.getItemInHand().setDurability((short)(p.getItemInHand().getDurability()+fallen));
	}
	
	private boolean isAxe(Material a) {
		if (a==Material.IRON_AXE) return true;
		if (a==Material.WOOD_AXE) return true;
		if (a==Material.GOLD_AXE) return true;
		if (a==Material.DIAMOND_AXE) return true;
		if (a==Material.STONE_AXE) return true;
	    return false;
	}
	
	private boolean acceptsPlayer(Player p) {
		return p.hasPermission("woodcutter.use");
	}
	
	public void remove(Block b) {
		if (b.getType()==Material.LOG||b.getType()==Material.LEAVES) b.breakNaturally();
	}
}

package se.enji;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

public class WoodCutter extends JavaPlugin implements Listener {
	private FileConfiguration config;
	private Random random;

	private boolean needAxe;
	private boolean mustSneak;

	private boolean isLog(Block block) {
		return block.getType().name().endsWith("LOG");
	}

	private boolean acceptableSurroundable(Block block) {
		List<Material> surroundable = Arrays.asList(Material.DIRT, Material.GRASS_BLOCK);

		return isLog(block) || surroundable.contains(block.getType());
	}

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		config = getConfig();
		config.options().copyDefaults(true);

		needAxe = config.getBoolean("needAxe");
		mustSneak = config.getBoolean("mustSneak");

		random = new Random();

		saveConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();
		Block eventBlock = event.getBlock();

		if (!player.hasPermission("woodcutter.use"))
			return;

		if (mustSneak && !player.isSneaking())
			return;

		if (needAxe && !isHoldingAxe(player))
			return;

		if (!isLog(eventBlock))
			return;

		Location location = event.getBlock().getLocation();
		Block blockBeneath = location.subtract(0.0, 1.0, 0.0).getBlock();

		if (!acceptableSurroundable(blockBeneath))
			return;

		Block blockAbove = location.add(0.0, 1.0, 0.0).getBlock();

		if (!isLog(blockAbove))
			return;

		WoodCutterState state = new WoodCutterState(eventBlock, player);
		columnRemove(state, location);
	}

	private void columnRemove(WoodCutterState state, Location location) {
		boolean logsLeft = true;
		int fallen = 0;

		location.subtract(0.0, 1.0, 0.0);

		while (logsLeft) {
			Block block = location.add(0.0, 1.0, 0.0).getBlock();

			if (state.isSameTree(block)) {
				block.breakNaturally(state.heldItem);
				fallen++;
				state.totalFallen++;
			}

			else
				logsLeft = false;

			for (int x = -1; x <= 1; x++)
				for (int z = -1; z <= 1; z++) {
					columnCheck(state, block, x, z);
				}
		}

		durabilityCheck(state, fallen);
	}

	private void columnCheck(WoodCutterState state, Block block, double xOffset, double zOffset) {
		Location newLocation = block.getLocation().subtract(xOffset, 0, zOffset);

		int totalXOffset = Math.abs(state.origin.getBlockX() - newLocation.getBlockX());
		int totalZOffset = Math.abs(state.origin.getBlockZ() - newLocation.getBlockZ());

		if (totalXOffset > 5 || totalZOffset > 5) {
			return;
		}

		if (state.totalFallen < 150 && state.isSameTree(newLocation.getBlock())) {
			columnRemove(state, newLocation);
		}
	}

	private void durabilityCheck(WoodCutterState state, int fallenBefore) {
		boolean gameModeIsCreative = state.player.getGameMode() == GameMode.CREATIVE;
		boolean playerIsHoldingAxe = isHoldingAxe(state.player);
		boolean heldItemsAreZero = state.heldItem.getAmount() == 0;

		int fallen = fallenBefore;

		if (gameModeIsCreative || !playerIsHoldingAxe || heldItemsAreZero) {
			return;
		}

		if (state.heldItemUnbreaking > 0) {
			// http://minecraft.gamepedia.com/Enchantment#Enchantments
			int chance = 100 / (state.heldItemUnbreaking + 1);
			int oldFallen = fallenBefore;

			for (int i = 0; i < oldFallen; i++) {
				if (random.nextInt(100) > chance)
					fallen--;
			}
		}

		Damageable axeItemMeta = (Damageable) (state.heldItem.getItemMeta());

		short newDamage = (short) (axeItemMeta.getDamage() + fallen);
		int maxDamage = axeItemMeta.getMaxDamage();

		if (newDamage < maxDamage) {
			axeItemMeta.setDamage(newDamage);
		} else {
			state.heldItem.setAmount(0);
			state.player.getInventory().setItemInMainHand(null);
		}
	}

	private boolean isHoldingAxe(Player p) {
		Material held = p.getInventory().getItemInMainHand().getType();

		return held.toString().endsWith("AXE");
	}
}

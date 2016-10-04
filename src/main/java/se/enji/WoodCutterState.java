package se.enji;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WoodCutterState {
    private final int id;
    private final byte meta;
    private final Location origin;
    private final Player player;
    private final ItemStack heldItem;
    private final int heldItemUnbreaking;

    private int totalFallen = 0;

    @SuppressWarnings("deprecation")
    public WoodCutterState(Block block, Player player) {
        this.id = block.getTypeId();
        this.meta = block.getData();
        this.origin = block.getLocation();
        this.player = player;
        this.heldItem = player.getInventory().getItemInMainHand();
        this.heldItemUnbreaking = heldItem.getEnchantmentLevel(Enchantment.DURABILITY);
    }

    @SuppressWarnings("deprecation")
    public boolean isSameTree(Block block) {
        // Using deprecated ID and meta here, because the only alternative seems to be
        // creating new Tree objects. Seems too wasteful
        int blockId = block.getTypeId();
        int blockMeta = block.getData();
        
        // Handle special case for large oak trees, which uses horizontal logs
        if (id == 17 && meta == 0) {
            return blockId == 17 && (blockMeta == 0 || blockMeta == 4 || blockMeta == 8);
        }

        else return blockId == id && blockMeta == meta;
    }
}

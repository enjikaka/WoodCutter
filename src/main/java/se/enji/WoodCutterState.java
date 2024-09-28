package se.enji;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WoodCutterState {
    public final Material material;
    public final BlockFace face;
    public final Location origin;
    public final Player player;
    public final ItemStack heldItem;
    public final int heldItemUnbreaking;

    public int totalFallen = 0;

    public WoodCutterState(Block block, Player player) {
        this.material = block.getType();
        this.face = block.getFace(block);
        this.origin = block.getLocation();
        this.player = player;
        this.heldItem = player.getInventory().getItemInMainHand();
        this.heldItemUnbreaking = heldItem.getEnchantmentLevel(Enchantment.UNBREAKING);
    }

    public boolean isSameTree(Block block) {
        // Using deprecated ID and meta here, because the only alternative seems to be
        // creating new Tree objects. Seems too wasteful
        Material blockMaterial = block.getType();
        BlockFace blockFace = block.getFace(block);

        // Handle special case for large oak trees, which uses horizontal logs
        if (this.material.equals(Material.OAK_LOG) && this.face.equals(BlockFace.UP) || this.face.equals(BlockFace.DOWN)) {
            return blockMaterial.equals(Material.OAK_LOG) && (blockFace.equals(BlockFace.UP) || blockFace.equals(BlockFace.DOWN) || blockFace.equals(BlockFace.NORTH) || blockFace.equals(BlockFace.SOUTH) || blockFace.equals(BlockFace.WEST) || blockFace.equals(BlockFace.EAST));
        }

        else
            return blockMaterial.equals(this.material) && blockFace.equals(this.face);
    }
}

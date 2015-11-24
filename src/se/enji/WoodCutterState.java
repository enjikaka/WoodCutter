package se.enji;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WoodCutterState {
    int id;
    byte meta;
    Location origin;
    Player player;

    int totalFallen = 0;

    @SuppressWarnings("deprecation")
    public WoodCutterState(Block block, Player player) {
        this.id = block.getTypeId();
        this.meta = block.getData();
        this.origin = block.getLocation();
        this.player = player;
    }

    @SuppressWarnings("deprecation")
    public boolean isSameTree(Block block) {
        int blockId = block.getTypeId();
        int blockMeta = block.getData();
        // Handle special case for large oak trees, which uses horizontal logs
        if (id == 17 && meta == 0) {
            return blockId == 17 && (blockMeta == 0 || blockMeta == 4 || blockMeta == 8);
        }

        else return blockId == id && blockMeta == meta;
    }
}

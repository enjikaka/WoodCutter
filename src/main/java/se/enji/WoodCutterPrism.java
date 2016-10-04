package se.enji;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.RecordingQueue;
import me.botsko.prism.actions.BlockAction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

// Wrapper class is needed, to prevent ClassDefNotFound in WoodCutter.java if PRISM is
// not installed
public class WoodCutterPrism {
    private Prism prism;

    public WoodCutterPrism(Plugin plugin) {
        prism = (Prism) plugin;
    }

    // https://github.com/prism/PrismApiDemo/blob/master/src/main/java/me/botsko/prismapidemo/manual/CustomManualActionExample.java
    public void recordBreak(Block block, Player player) {
        BlockAction action = new BlockAction();

        action.setActionType("block-break");
        action.setBlock(block);
        action.setPlayerName(player.getName());

        RecordingQueue.addToQueue(action);
    }
}

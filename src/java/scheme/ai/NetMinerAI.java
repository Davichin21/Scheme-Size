package scheme.ai;

import mindustry.ai.types.MinerAI;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.type.Item;

import static mindustry.Vars.*;

/** Fix for working on servers. */
public class NetMinerAI extends MinerAI {

    public static Item priorityItem;

    @Override
    public void updateMovement() {
        Building core = unit.closestCore();
        if (!unit.canMine() || core == null) return;

        if (mining) {
            if (priorityItem != null && indexer.hasOre(priorityItem) && unit.canMine(priorityItem) && core.acceptStack(priorityItem, 1, unit) != 0)
                targetItem = priorityItem; // player can select an item to dig

            super.updateMovement();
        } else {
            unit.mineTile = null;

            if (unit.stack.amount == 0) {
                mining = true;
                return;
            }

            if (unit.within(core, unit.type.range)) {
                Call.transferInventory(player, core);
                mining = true;
            }

            circle(core, unit.type.range / 1.8f);
        }
    }
}

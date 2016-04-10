package eu.concord.worldutils.async.modifications;

import eu.concord.worldutils.async.IBlockModification;
import org.bukkit.World;

public class SchematicBlockModification implements IBlockModification {

    private boolean finished;

    public SchematicBlockModification() {

    }

    @Override
    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public int run(World w, int batchSize) {
        return 0;
    }
}

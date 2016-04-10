package eu.concord.worldutils.async.modifications;

import eu.concord.worldutils.WorldUtils;
import eu.concord.worldutils.async.IBlockModification;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * Created by wouter on 4/1/16.
 */
public class SingleBlockModification implements IBlockModification {

    private final int x;
    private final int y;
    private final int z;
    private final Material material;
    private final int metadata;
    private boolean finished;

    /**
     * Create a new SingleBlockModification instance
     * @param location The location of the block to change
     * @param material The material to set the block to
     */
    public SingleBlockModification(Location location, Material material) {
        this(location, material, 0);
    }

    /**
     * Create a new SingleBlockModification instance
     * @param location The location of the block to change
     * @param material The material to set the block to
     * @param metadata The metadata to set to the block
     */
    public SingleBlockModification(Location location, Material material, int metadata) {
        this(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                material,
                metadata
        );
    }

    /**
     * Create a new SingleBlockModification instance
     * @param x The x-coordinate of the block to change
     * @param y The y-coordinate of the block to change
     * @param z The z-coordinate of the block to change
     * @param material The material to set the block to
     */
    public SingleBlockModification(int x, int y, int z, Material material) {
        this(x, y, z, material, 0);
    }


    /**
     * Create a new SingleBlockModification instance
     * @param x The x-coordinate of the block to change
     * @param y The y-coordinate of the block to change
     * @param z The z-coordinate of the block to change
     * @param material The material to set the block to
     * @param metadata The metadata to set to the block
     */
    public SingleBlockModification(int x, int y, int z, Material material, int metadata) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
        this.metadata = metadata;
        this.finished = false;
    }

    /**
     * @return The x-position of this modification
     */
    public int getX() {
        return x;
    }

    /**
     * @return The y-position of this modification
     */
    public int getY() {
        return y;
    }

    /**
     * @return The z-position of this modification
     */
    public int getZ() {
        return z;
    }

    /**
     * @return The material that the block should be set to
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @return The metadata that the block should be set to
     */
    public int getMetadata() {
        return metadata;
    }

    @Override
    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public int run(World w, int batchSize) {
        WorldUtils.setBlock(w, this.x, this.y, this.z, this.material, this.metadata);
        this.finished = true;
        return 1;
    }

}

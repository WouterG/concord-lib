package eu.concord.worldutils.async.modifications;

import eu.concord.worldutils.WorldUtils;
import eu.concord.worldutils.async.IBlockModification;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.lang.reflect.Field;

public class CuboidBlockModification implements IBlockModification {

    private int xLow;
    private int xHigh;
    private int yLow;
    private int yHigh;
    private int zLow;
    private int zHigh;
    private Material material;
    private int metadata;

    private int x;
    private int y;
    private int z;

    private boolean finished = false;

    public CuboidBlockModification(Location corner1, Location corner2, Material material) {
        this(corner1, corner2, material, 0);
    }

    public CuboidBlockModification(Location corner1, Location corner2, Material material, int metadata) {
        this(
                corner1.getBlockX(), corner1.getBlockY(), corner1.getBlockZ(),
                corner2.getBlockX(), corner2.getBlockY(), corner2.getBlockZ(),
                material, metadata
        );
    }

    public CuboidBlockModification(int x1, int y1, int z1, int x2, int y2, int z2, Material material) {
        this(
                x1, y1, z1,
                x2, y2, z2,
                material, 0
        );
    }

    public CuboidBlockModification(int x1, int y1, int z1, int x2, int y2, int z2, Material material, int metadata) {
        this.xLow = (x1 <= x2 ? x1 : x2);
        this.xHigh = (x1 > x2 ? x1 : x2);
        this.yLow = (y1 <= y2 ? y1 : y2);
        this.yHigh = (y1 > y2 ? y1 : y2);
        this.zLow = (z1 <= z2 ? z1 : z2);
        this.zHigh = (z1 > z2 ? z1 : z2);
        this.material = material;
        this.metadata = metadata;

        this.x = this.xLow;
        this.y = this.yLow;
        this.z = this.zLow;
    }

    @Override
    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public int run(World w, int batchSize) {
        int edited = 0;
        while (y <= yHigh) {
            while (x <= xHigh) {
                while (z <= zHigh) {
                    if (!WorldUtils.isBlock(w, x, y, z, this.material, this.metadata)) {
                        if (WorldUtils.setBlock(w, x, y, z, this.material, this.metadata)) {
                            edited++;
                        }
                    }
                    if (edited >= batchSize) {
                        return edited;
                    }
                    z++;
                }
                x++;
                z = zLow;
            }
            y++;
            x = xLow;
        }
        this.finished = true;
        return edited;
    }

    @Override
    public String toString() {
        String s = "{" + this.getClass().getSimpleName() + ":{";
        for (Field f : this.getClass().getDeclaredFields()) {
            try {
                s += f.getName() + ":" + f.get(this).toString() + ",";
            } catch (Exception e) {
            }
        }
        s = s.substring(0, s.length() - 1);
        s += "}}";
        return s;
    }
}

/**
   The MIT License (MIT)

    Copyright (c) 2015 team-concord

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */
package eu.concord.worldutils.async;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * A single block modification, to be executed by the BlockChangeTask.
 * Not World-specific.
 * @author Wouter
 */
public class BlockModification {
    
    private final int x;
    private final int y;
    private final int z;
    private final Material material;
    private final int metadata;
    
    /**
     * Create a new BlockModification instance
     * @param location The location of the block to change
     * @param material The material to set the block to
     */
    public BlockModification(Location location, Material material) {
        this(location, material, 0);
    }
    
    /**
     * Create a new BlockModification instance
     * @param location The location of the block to change
     * @param material The material to set the block to
     * @param metadata The metadata to set to the block
     */
    public BlockModification(Location location, Material material, int metadata) {
        this(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                material,
                metadata
        );
    }
    
    /**
     * Create a new BlockModification instance
     * @param x The x-coordinate of the block to change
     * @param y The y-coordinate of the block to change
     * @param z The z-coordinate of the block to change
     * @param material The material to set the block to
     */
    public BlockModification(int x, int y, int z, Material material) {
        this(x, y, z, material, 0);
    }
    
    
    /**
     * Create a new BlockModification instance
     * @param x The x-coordinate of the block to change
     * @param y The y-coordinate of the block to change
     * @param z The z-coordinate of the block to change
     * @param material The material to set the block to
     * @param metadata The metadata to set to the block
     */
    public BlockModification(int x, int y, int z, Material material, int metadata) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
        this.metadata = metadata;
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
    
    
    
}

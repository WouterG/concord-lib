/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 team-concord
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eu.concord.worldutils;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.IBlockData;
import net.minecraft.server.v1_9_R1.PacketPlayOutMapChunk;
import net.minecraft.server.v1_9_R1.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_9_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.util.LongHash;
import org.bukkit.entity.Player;

/**
 * WorldUtils is a class with static functions for world modification methods.
 *
 * @author Wouter
 */
public class WorldUtils {

    /**
     * Set a block in the world through NMS, without updating the change on the
     * clients. You can use the {@link #reloadChunk(Player, Chunk) reloadChunk}
     * method to send the changes.
     *
     * @param world The world to which to apply the block change
     * @param x The x-coordinate of the block to be changed
     * @param y The y-coordinate of the block to be changed
     * @param z The z-coordinate of the block to be changed
     * @param material The new material for the block
     * @param metadata The metadata for the block
     * @return True if the block change was successful, false if the chunk has
     * not been generated yet
     */
    public static boolean setBlock(World world, int x, int y, int z, Material material, int metadata) {
        BlockPosition pos = new BlockPosition(x, y, z);
        int combinedID = material.getId() + ((short) metadata << 12);
        IBlockData block = net.minecraft.server.v1_9_R1.Block.getByCombinedId(combinedID);
        if (world.getChunkAt(x >> 4, z >> 4) == null) {
            // The entire 16x256x16 chunk doesn't exist. Abort this blockchange.
            return false;
        }
        if (((CraftChunk) world.getChunkAt(x >> 4, z >> 4)).getHandle().getSections()[y >> 4] == null) {
            // the 16x16x16 section in the 16x256x16 chunk isn't initialized yet.
            // This first creates that section and then sets the block
            ((CraftChunk) world.getChunkAt(x >> 4, z >> 4)).getHandle().a(pos, block);
            return true;
        }
        if (block.getBlock() instanceof IContainer) {
            TileEntity tileentity = ((CraftChunk) world.getChunkAt(x >> 4, z >> 4)).getHandle().a(pos, net.minecraft.server.v1_9_R1.Chunk.EnumTileEntityState.CHECK);
            if (tileentity != null) {
                tileentity.E();
            }
        }
        ((CraftChunk) world.getChunkAt(x >> 4, z >> 4)).getHandle().getSections()[y >> 4].setType(x & 15, y & 15, z & 15, block);
        if (block.getBlock() instanceof IContainer) {
            TileEntity tileentity = ((CraftChunk) world.getChunkAt(x >> 4, z >> 4)).getHandle().a(pos, net.minecraft.server.v1_9_R1.Chunk.EnumTileEntityState.CHECK);
            if (tileentity == null) {
                tileentity = ((IContainer) block.getBlock()).a(((CraftWorld) world).getHandle(), block.getBlock().toLegacyData(block));
                ((CraftWorld) world).getHandle().setTileEntity(pos, tileentity);
            }
            if (tileentity != null) {
                tileentity.E();
            }
        }
        return true;
    }

    /**
     * Change the biome at a specific coordinate using NMS, without updating the
     * change on the clients. You can use the
     * {@link #reloadChunk(Player, Chunk) reloadChunk} method to send the
     * changes.
     *
     * @param world The world to which to apply the biome change
     * @param x The x-coordinate of the biome to be changed
     * @param z The z-coordinate of the biome to be changed
     * @param biome The new biome for this position
     * @return True if the biome change was successful, false if the chunk has
     * not been generated yet
     */
    public static boolean setBiome(World world, int x, int z, Biome biome) {
        if (world.getChunkAt(x >> 4, z >> 4) == null || biome == null) {
            return false;
        }
        ((CraftChunk) world.getChunkAt(x >> 4, z >> 4)).getHandle().getBiomeIndex()[((z & 0xF) << 4) | (x & 0xF)] = (byte) CraftBlock.biomeToBiomeBase(biome).;
        return true;
    }

    /**
     * Resends the entire chunk to the player.
     *
     * @param player The player to resend the chunk to
     * @param chunk The chunk to be resent
     */
    public static void reloadChunk(Player player, Chunk chunk) {
        PacketPlayOutMapChunk unloadPacket = new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), true, 0);
        PacketPlayOutMapChunk reloadPacket = new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), true, 65535);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(unloadPacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(reloadPacket);
    }

    /**
     * Resends a partial chunk to the player. The partialIndex is an index
     * counting from the bottom of the pillar upwards, in blocks of 16. 0 will
     * update the bottom 16^3 blocks, 16 the top ones.
     *
     * @param player The player to resend the chunk to
     * @param chunk The chunk the part is in to be resent
     * @param partialIndex The index of the part of the chunk.
     */
    public static void reloadPartialChunk(Player player, Chunk chunk, int partialIndex) {
        if (partialIndex < 0 || partialIndex > 16) {
            return;
        }
        int partialIndexBitmask = 1 << partialIndex;
        PacketPlayOutMapChunk unloadPacket = new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), true, 0);
        PacketPlayOutMapChunk reloadPacket = new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), true, partialIndexBitmask);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(unloadPacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(reloadPacket);
    }
    
    /**
     * Regenerates a chunk entirely, optionally also sending it to the players online
     * @param world The world you want a chunk to be regenerated in
     * @param x The chunk's x-coordinate
     * @param z The chunk's z-coordinate
     * @param notifyClients Whether to send the players in this world to see the change.
     */
    public static void regenerateChunk(World world, int x, int z, boolean notifyClients) {
        ((CraftWorld)world).getHandle().getChunkProviderServer().chunks.remove(LongHash.toLong(x, z));
        net.minecraft.server.v1_9_R1.Chunk chunk = ((CraftWorld)world).getHandle().getChunkProviderServer().originalGetChunkAt(x, z);
        if (notifyClients) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld() == world) {
                    WorldUtils.reloadChunk(p, chunk.bukkitChunk);
                }
            }
        }
    }

}

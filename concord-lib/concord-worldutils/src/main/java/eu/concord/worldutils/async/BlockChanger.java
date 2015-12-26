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

import eu.concord.worldutils.WorldUtils;
import eu.concord.worldutils.async.events.BlockChangeTaskStatusEvent;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;

class BlockChanger implements Runnable {

    private final BlockChangeTask owner;
    private int currentIndex;
    private boolean running;
    private ConcurrentHashMap<ChangedChunk, net.minecraft.server.v1_8_R3.Chunk> editedChunks;

    BlockChanger(BlockChangeTask owner) {
        this.owner = owner;
        this.currentIndex = 0;
        this.running = true;
        this.editedChunks = new ConcurrentHashMap();
    }

    @Override
    public void run() {
        if (this.currentIndex >= this.owner.getEdits().size()) {
            this.owner.setFinished(true);
            if (this.owner.isUpdateChunksAfterwards()) {

            }
        }
        int e = this.currentIndex + this.owner.getBatchSize();
        List<BlockModification> currentBatch = this.owner.getEdits().subList(this.currentIndex, e);
        this.currentIndex = e;
        for (BlockModification modification : currentBatch) {
            WorldUtils.setBlock(
                    this.owner.getWorld(),
                    modification.getX(),
                    modification.getY(),
                    modification.getZ(),
                    modification.getMaterial(),
                    modification.getMetadata()
            );
            if (!this.editedChunks.containsKey(new ChangedChunk(modification.getX() >> 4, modification.getZ() >> 4))) {
                this.editedChunks.put(
                        new ChangedChunk(
                                modification.getX() >> 4, 
                                modification.getZ() >> 4
                        ), 
                        ((CraftChunk) this.owner.getWorld().getChunkAt(modification.getX() >> 4, modification.getZ() >> 4)).getHandle()
                );
            }
        }
        if (this.owner.emitsEvents()) {
            Bukkit.getPluginManager().callEvent(new BlockChangeTaskStatusEvent(this.owner.getStatus()));
        }
        if (this.running) {
            Bukkit.getScheduler().runTask(this.owner.getOwner(), this);
        }
    }

    int getCurrentIndex() {
        return this.currentIndex;
    }

    boolean isRunning() {
        return this.running;
    }

    void setRunning(boolean v) {
        this.running = v;
    }

    private static class ChangedChunk {

        private final int x;
        private final int z;

        ChangedChunk(int x, int z) {
            this.x = x;
            this.z = z;
        }

        int getX() {
            return x;
        }

        int getZ() {
            return z;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ChangedChunk) {
                ChangedChunk co = (ChangedChunk) o;
                return co.x == this.x && co.z == this.z;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 41 * hash + this.x;
            hash = 41 * hash + this.z;
            return hash;
        }

    }

}

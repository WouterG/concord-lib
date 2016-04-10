/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 team-concord
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eu.concord.worldutils.async;

import eu.concord.worldutils.async.events.BlockChangeTaskStatusEvent;
import org.bukkit.Bukkit;

class BlockChanger implements Runnable {

    private final BlockChangeTask owner;
    private int blockChangeWorkerIndex;
    private boolean running;

    BlockChanger(BlockChangeTask owner) {
        this.owner = owner;
        this.blockChangeWorkerIndex = 0;
        this.running = true;
    }

    @Override
    public void run() {
        int changesLeft = this.owner.getBatchSize();
        while (changesLeft > 0) {
            if (this.blockChangeWorkerIndex >= this.owner.getEdits().size()) {
                this.owner.setFinished(true);
                this.setRunning(false);
                if (this.owner.emitsEvents()) {
                    Bukkit.getPluginManager().callEvent(new BlockChangeTaskStatusEvent(this.owner.getStatus()));
                }
                return;
            }
            IBlockModification mod = this.owner.getEdits().get(this.blockChangeWorkerIndex);
            changesLeft -= mod.run(this.owner.getWorld(), changesLeft);
            if (mod.isFinished()) {
                this.blockChangeWorkerIndex++;
            }
        }
        if (this.owner.emitsEvents()) {
            Bukkit.getPluginManager().callEvent(new BlockChangeTaskStatusEvent(this.owner.getStatus()));
        }
        if (this.running) {
            Bukkit.getScheduler().runTaskLater(this.owner.getOwner(), this, 1L);
        }
    }

    int getBlockChangeWorkerIndex() {
        return this.blockChangeWorkerIndex;
    }

    boolean isRunning() {
        return this.running;
    }

    void setRunning(boolean v) {
        this.running = v;
    }

}

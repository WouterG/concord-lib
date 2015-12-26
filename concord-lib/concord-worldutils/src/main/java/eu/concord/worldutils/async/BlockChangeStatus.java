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

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The status of the BlockChangeTask
 * @author Wouter
 */
public class BlockChangeStatus {
    
    private final JavaPlugin ownerPlugin;
    private final int blocksChanged;
    private final int totalBlocks;
    private final boolean running;
    private final boolean finished;
    
    BlockChangeStatus(JavaPlugin ownerPlugin, int blocksChanged, int totalBlocks, boolean running, boolean finished) {
        this.ownerPlugin = ownerPlugin;
        this.blocksChanged = blocksChanged;
        this.totalBlocks = totalBlocks;
        this.running = running;
        this.finished = finished;
    }

    /**
     * @return The plugin that created the BlockChangeTask
     */
    public JavaPlugin getOwnerPlugin() {
        return this.ownerPlugin;
    }

    /**
     * @return The amount of blocks modified currently by the BlockChangeTask
     */
    public int getBlocksChanged() {
        return this.blocksChanged;
    }

    /**
     * @return The amount of blocks the BlockChangeTask has to change in total
     */
    public int getTotalBlocks() {
        return this.totalBlocks;
    }
    
    /**
     * @return Whether the BlockChangeTask is currently running
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * @return Whether the BlockChangeTask is done
     */
    public boolean isFinished() {
        return this.finished;
    }
    
}

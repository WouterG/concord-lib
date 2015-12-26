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

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Class providing a wrapper for mass block-edits, splitting it up in parts and applying the changes through NMS.
 * @author Wouter
 */
public class BlockChangeTask {
    
    private final JavaPlugin owner;
    private final World world;
    private final ArrayList<BlockModification> edits;
    private final int batchSize;
    private boolean updateChunksAfterwards;
    private boolean emitEvents;
    private boolean finished;
    private BlockChanger task;
    private BukkitTask bukkitTask;
    
    /**
     * Create new block change task.
     * @param owner The plugin creating this task
     * @param world The world this task should run in
     * @param edits The block changes to do in this task
     */
    public BlockChangeTask(JavaPlugin owner, World world, Collection<BlockModification> edits) {
        this(owner, world, edits, 1_000_000);
    }
    
    /**
     * Create new block change task.
     * @param owner The plugin creating this task
     * @param world The world this task should run in
     * @param edits The block changes to do in this task
     * @param batchSize The amount of blocks to be changed per batch. Default is 1,000,000
     */
    public BlockChangeTask(JavaPlugin owner, World world, Collection<BlockModification> edits, int batchSize) {
        this.owner = owner;
        this.world = world;
        this.edits = new ArrayList();
        this.edits.addAll(edits);
        this.batchSize = batchSize;
        this.updateChunksAfterwards = true;
        this.emitEvents = true;
        this.finished = false;
        this.task = new BlockChanger(this);
    }
    
    /**
     * Starts this task if it hasn't been started yet.
     */
    public void start() {
        if (this.bukkitTask != null) {
            return;
        }
        this.task.setRunning(true);
        this.bukkitTask = Bukkit.getScheduler().runTask(owner, this.task);
    }
    
    /**
     * Gracefully stops this task if it is running.
     * Lets the currently running batch finish.
     * Can be resumed.
     */
    public void stop() {
        if (this.bukkitTask == null) {
            return;
        }
        this.task.setRunning(false);
    }
    
    /**
     * Force-stops this task if it is running.
     * If a batch is currently running it will skip the remaining blocks in that part.
     * Can be resumed, but will likely miss some changes then.
     */
    public void forceStop() {
        if (this.bukkitTask == null) {
            return;
        }
        this.bukkitTask.cancel();
        this.bukkitTask = null;
    }
    
    void setFinished(boolean finished) {
        this.finished = finished;
    }
    
    /**
     * @return The status of this task
     */
    public BlockChangeStatus getStatus() {
        return new BlockChangeStatus(
                this.owner,
                this.edits.size(),
                this.task.getCurrentIndex(),
                this.task.isRunning(),
                this.finished
        );
    }

    /**
     * @return The plugin owning this task
     */
    public JavaPlugin getOwner() {
        return owner;
    }

    /**
     * @return The world this task modifies
     */
    public World getWorld() {
        return world;
    }

    /**
     * @return A copy of the modifications this task does
     */
    public ArrayList<BlockModification> getEdits() {
        ArrayList<BlockModification> editsClone = new ArrayList();
        editsClone.addAll(edits);
        return editsClone;
    }

    /**
     * @return The amount of blocks that are modified per batch
     */
    public int getBatchSize() {
        return batchSize;
    }

    /**
     * @return Whether the chunks should be resent to all players when the task is done
     */
    public boolean isUpdateChunksAfterwards() {
        return updateChunksAfterwards;
    }

    /**
     * @param updateChunksAfterwards Whether this task should update all players after the task is finished
     */
    public void setUpdateChunksAfterwards(boolean updateChunksAfterwards) {
        this.updateChunksAfterwards = updateChunksAfterwards;
    }
    
    /**
     * @return Whether this task emits events through Bukkit's event system, default is true
     */
    public boolean emitsEvents() {
        return this.emitEvents;
    }
    
    /**
     * @param value Whether this task should emit events through Bukkit's event system, default is true
     */
    public void setEmitsEvents(boolean value) {
        this.emitEvents = value;
    }
    
}

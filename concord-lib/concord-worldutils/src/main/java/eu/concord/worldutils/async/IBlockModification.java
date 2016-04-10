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

import org.bukkit.World;

/**
 * A single block modification, to be executed by the BlockChangeTask.
 * Not World-specific.
 * @author Wouter
 */
public interface IBlockModification {

    /**
     * Get whether the task is done
     * @return
     */
    public boolean isFinished();

    /**
     * Run the task on the provided World, and limit to batchSize if batchSize is less than getSize
     * The class has to keep track itself of it's current progress.
     * @param w The world to run the modification on
     * @param batchSize the amount of blocks that may be modified on this run
     * @return the amount of blocks that have been modified
     */
    public int run(World w, int batchSize);

}

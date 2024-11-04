package com.poniarcade.poniarcade_database;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by appledash on 7/27/16.
 * Blackjack is still best pony.
 */
public class ThreadRunDatabaseOperation extends Thread {
    private final Queue<Runnable> runnables;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ThreadRunDatabaseOperation(Queue<Runnable> runnables) {
        this.runnables = runnables;
    }

    @Override
    public void run() {
	    this.running.set(true);
        while (this.running.get()) {
            Runnable r = this.runnables.poll();

            if (r != null) {
                try {
                    r.run();
                } catch (Exception e) {
                    PoniArcade_Database.logger().severe("Exception occured running a database operation!");
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) { }
            }
        }
    }

    public void abort() {
	    this.running.set(false);
    }
}

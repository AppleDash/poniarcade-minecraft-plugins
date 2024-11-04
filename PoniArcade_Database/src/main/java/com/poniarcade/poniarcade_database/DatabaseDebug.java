package com.poniarcade.poniarcade_database;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by appledash on 7/24/16.
 * Blackjack is still best pony.
 */
public final class DatabaseDebug {
    private static final ThreadLocal<Map<String, Long>> threadLocalDebugs = ThreadLocal.withInitial(HashMap::new);

	private DatabaseDebug() {
	}

	public static synchronized void startDebug(String tag) {
        Map<String, Long> debugs = DatabaseDebug.threadLocalDebugs.get();

        if (debugs.containsKey(tag.toLowerCase())) {
            throw new IllegalStateException("Cannot start debugging when we're already debugging this tag!");
        }

        debugs.put(tag.toLowerCase(), System.currentTimeMillis());
    }

    public static synchronized void finishDebug(String tag) {
        Map<String, Long> debugs = DatabaseDebug.threadLocalDebugs.get();

        if (!debugs.containsKey(tag.toLowerCase())) {
            throw new IllegalStateException("Cannot finish debugging when we never started debugging this tag!");
        }

        long startTime = debugs.remove(tag.toLowerCase());
        long delta = System.currentTimeMillis() - startTime;

        PoniArcade_Database.logger().info("Database call " + tag + " finished in " + delta + "ms.");
    }

    public static void printStatement(PreparedStatement ps) {
        PoniArcade_Database.logger().info("Executing query: " + ps.toString());
    }
}

package com.poniarcade.core.utils;

import com.google.common.collect.ImmutableMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by appledash on 8/3/16.
 * Blackjack is still best pony.
 */
public final class ItemDatabase {
    private static Map<String, Pair<Integer, Short>> itemMap = new HashMap<>();

	private ItemDatabase() {
	}

	public static void initItemDB() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ItemDatabase.class.getResourceAsStream("/items.csv")))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || !line.contains(",")) {
                    continue;
                }

                String[] split = line.split(",");
                String name = split[0];
                int id = Integer.parseInt(split[1]);
                short damage = Short.parseShort(split[2]);

                ItemDatabase.itemMap.put(name, Pair.of(id, damage));
            }

            ItemDatabase.itemMap = ImmutableMap.copyOf(ItemDatabase.itemMap);
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Failed to load and/or parse items.csv from JAR!", e);
        }
    }

    public static Optional<Pair<Integer, Short>> getIDAndDamageForName(String name) {
        return Optional.ofNullable(ItemDatabase.itemMap.get(name));
    }
}

package com.poniarcade.core.structs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;

import java.util.List;

/**
 * Created by appledash on 7/24/17.
 * Blackjack is best pony.
 */
public class PowerTools {
    public static final PowerTools EMPTY = new PowerTools(ImmutableMap.of());
    private final ImmutableMap<Material, ImmutableList<String>> map;

    public PowerTools(ImmutableMap<Material, ImmutableList<String>> map) {
        this.map = map;
    }

    public List<String> getPowerTools(Material material) {
        return this.map.getOrDefault(material, ImmutableList.of());
    }

    public PowerTools addPowerTool(Material material, String text) {
        return new PowerTools(ImmutableMap.of(
            material, ImmutableList.<String>builder()
                        .addAll(this.getPowerTools(material))
                        .add(text)
                        .build()
        ));
    }

    public PowerTools clearPowerTool(Material material) {
        ImmutableMap.Builder<Material, ImmutableList<String>> builder = ImmutableMap.builder();
        this.map.forEach((mat, tools) -> {
            if (mat != material) {
                builder.put(mat, tools);
            }
        });

        return new PowerTools(builder.build());
    }
}
